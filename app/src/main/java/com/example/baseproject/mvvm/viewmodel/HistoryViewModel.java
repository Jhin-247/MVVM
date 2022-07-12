package com.example.baseproject.mvvm.viewmodel;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.ArrayMap;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;

import com.example.baseproject.mvvm.model.AppInfo;
import com.example.baseproject.mvvm.service.AppService;
import com.example.baseproject.mvvm.util.AppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryViewModel extends BaseObservable {

    private final MutableLiveData<List<AppInfo>> mAppInfoMutableList;
    private final Context mContext;
    private long mMaxUseDuration;

    public HistoryViewModel(Context mContext) {
        this.mContext = mContext;
        mAppInfoMutableList = new MutableLiveData<>();
        initData();
    }

    public MutableLiveData<List<AppInfo>> getMutableAppList() {
        return mAppInfoMutableList;
    }

    public long getMaxUseDuration() {
        return mMaxUseDuration;
    }

    private boolean isAppInfoAvailable(UsageStats usageStats) {
        try {
            mContext.getPackageManager().getApplicationInfo(usageStats.getPackageName(), 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void initData() {
        HandlerThread mHandlerThread = new HandlerThread("getAppInfoThread");
        mHandlerThread.start();
        Handler handler = new Handler(mHandlerThread.getLooper());
        handler.post(() -> {
            mMaxUseDuration = 0;
            List<AppInfo> mAppInfoList = new ArrayList<>();
            ArrayMap<String, UsageStats> map = new ArrayMap<>();
            UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
            long mCurrentTime = System.currentTimeMillis();
            long mStartTime = mCurrentTime - 24 * 60 * 60 * 1000;
            List<UsageStats> mUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, mStartTime, mCurrentTime);
            for (UsageStats usageStats : mUsageStats) {
                if (!isAppInfoAvailable(usageStats)) {
                    continue;
                }
                UsageStats mExistingStat = map.get(usageStats.getPackageName());
                if (mExistingStat == null) {
                    map.put(usageStats.getPackageName(), usageStats);
                } else {
                    mExistingStat.add(usageStats);
                }
            }

            mUsageStats.clear();
            mUsageStats.addAll(map.values());

            for (UsageStats usageStats : mUsageStats) {
                AppInfo mAppInfo = new AppInfo();
                mAppInfo.setPackageName(usageStats.getPackageName());
                mAppInfo.setAppName(new AppUtils().getAppName(mContext, usageStats.getPackageName()));
                mAppInfo.setAppIcon(new AppUtils().getAppIconByPackageName(mContext, mAppInfo.getPackageName()));
                mAppInfo.setUsedDuration(usageStats.getTotalTimeInForeground());
                if (usageStats.getTotalTimeInForeground() > mMaxUseDuration) {
                    mMaxUseDuration = usageStats.getTotalTimeInForeground();
                }
                if (mAppInfo.getUsedDuration() != 0) {
                    mAppInfoList.add(mAppInfo);
                }
            }

            Collections.sort(mAppInfoList, (o1, o2) -> Long.compare(o2.getUsedDuration(), o1.getUsedDuration()));
            Handler mMainHandler = new Handler(mContext.getMainLooper());
            mMainHandler.post(() -> mAppInfoMutableList.setValue(mAppInfoList));
        });

    }

    public boolean checkPermission() {
        AppOpsManager appOpsManagerCompat = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManagerCompat.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), mContext.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public void startService() {
        Intent intent = new Intent(mContext, AppService.class);
        mContext.startService(intent);
    }
}
