package com.example.baseproject.mvvm.viewmodel;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.example.baseproject.BR;
import com.example.baseproject.R;
import com.example.baseproject.mvvm.model.AppInfo;
import com.example.baseproject.mvvm.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends BaseObservable {
    private static final String TAG = HistoryViewModel.class.getSimpleName();


    private final MutableLiveData<List<AppInfo>> mAppInfoMutableList;
    private final Context mContext;
    private long mMaxUseDuration;

    public MutableLiveData<List<AppInfo>> getMutableAppList() {
        return mAppInfoMutableList;
    }

    public long getMaxUseDuration() {
        return mMaxUseDuration;
    }

    public HistoryViewModel(Context mContext) {
        this.mContext = mContext;
        mAppInfoMutableList = new MutableLiveData<>();
        initData();
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

            UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
            long mCurrentTime = System.currentTimeMillis();
            long mStartTime = mCurrentTime / 2;
            List<UsageStats> mUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, mStartTime, mCurrentTime);
            for (UsageStats usageStats : mUsageStats) {
                if (!isAppInfoAvailable(usageStats)) {
                    continue;
                }
                AppInfo mAppInfo = new AppInfo();
                mAppInfo.setPackageName(usageStats.getPackageName());
                mAppInfo.setAppName(new AppUtils().getAppName(mContext,usageStats.getPackageName()));
                mAppInfo.setAppIcon(new AppUtils().getAppIconByPackageName(mContext, mAppInfo.getPackageName()));
                mAppInfo.setUsedDuration(usageStats.getTotalTimeInForeground());
                if (usageStats.getTotalTimeInForeground() > mMaxUseDuration) {
                    mMaxUseDuration = usageStats.getTotalTimeInForeground();
                }
                if (mAppInfo.getUsedDuration() != 0) {
                    mAppInfoList.add(mAppInfo);
                }
            }
            Handler mMainHandler = new Handler(mContext.getMainLooper());
            mMainHandler.post(() -> mAppInfoMutableList.setValue(mAppInfoList));
        });

    }
}
