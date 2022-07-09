package com.example.baseproject.mvvm.viewmodel;

import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.baseproject.R;
import com.example.baseproject.mvvm.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private final MutableLiveData<List<AppInfo>> mAppInfoMutableList;

    

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        mAppInfoMutableList = new MutableLiveData<>();
        initData();
    }

    @BindingAdapter("bind:image")
    public static void setImage(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    public MutableLiveData<List<AppInfo>> getMutableAppList() {
        return mAppInfoMutableList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private boolean isAppInfoAvailable(UsageStats usageStats) {
        try {
            getApplication().getPackageManager().getApplicationInfo(usageStats.getPackageName(), 0);
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
            List<AppInfo> mAppInfoList = new ArrayList<>();

            UsageStatsManager usageStatsManager = (UsageStatsManager) getApplication().getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
            long mCurrentTime = System.currentTimeMillis();
            long mStartTime = mCurrentTime / 2;
            List<UsageStats> mUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, mStartTime, mCurrentTime);
            for (UsageStats usageStats : mUsageStats) {
                if (!isAppInfoAvailable(usageStats)) {
                    continue;
                }
                PackageManager packageManager = getApplication().getPackageManager();
                ApplicationInfo applicationInfo;
                AppInfo mAppInfo = new AppInfo();
                mAppInfo.setPackageName(usageStats.getPackageName());
                try {
                    applicationInfo = packageManager.getApplicationInfo(mAppInfo.getPackageName(), 0);
                    mAppInfo.setAppName(packageManager.getApplicationLabel(applicationInfo).toString());
                } catch (final PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                mAppInfo.setAppIcon(getAppIconByPackageName(getApplication(), mAppInfo.getPackageName()));
                mAppInfoList.add(mAppInfo);
            }
            Handler mMainHandler = new Handler(getApplication().getMainLooper());
            mMainHandler.post(() -> {
                mAppInfoMutableList.setValue(mAppInfoList);
            });
        });

    }

    private Drawable getAppIconByPackageName(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return AppCompatResources.getDrawable(getApplication(), R.drawable.ic_launcher_foreground);
    }

    public static class MyViewModelFactory implements ViewModelProvider.Factory {
        private final Application mApplication;

        public MyViewModelFactory(Application application) {
            mApplication = application;
        }


        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new HistoryViewModel(mApplication);
        }
    }


}
