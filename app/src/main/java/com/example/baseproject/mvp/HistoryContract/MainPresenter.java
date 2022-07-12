package com.example.baseproject.mvp.HistoryContract;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.ArrayMap;

import com.example.baseproject.mvvm.model.AppInfo;
import com.example.baseproject.mvvm.util.AppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View mView;
    private final Context mContext;

    public MainPresenter(MainContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void checkPermission() {
        AppOpsManager appOpsManagerCompat = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManagerCompat.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), mContext.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        if (granted) {
            mView.onHasPermission();
        } else {
            mView.requestPermission();
        }

    }

    @Override
    public void queryApp() {
        HandlerThread mHandlerThread = new HandlerThread("getAppInfoThread");
        mHandlerThread.start();
        Handler handler = new Handler(mHandlerThread.getLooper());
        handler.post(() -> {
            long mMaxUseDuration = 0;
            List<AppInfo> mAppInfoList = new ArrayList<>();
            ArrayMap<String, UsageStats> map = new ArrayMap<>();
            UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
            long mCurrentTime = System.currentTimeMillis();
            long mStartTime = 0;
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
            mView.onQueryAppComplete(mAppInfoList, mMaxUseDuration);
        });
    }

    private boolean isAppInfoAvailable(UsageStats usageStats) {
        try {
            mContext.getPackageManager().getApplicationInfo(usageStats.getPackageName(), 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
