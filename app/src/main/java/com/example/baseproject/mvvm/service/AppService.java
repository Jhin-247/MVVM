package com.example.baseproject.mvvm.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.example.baseproject.R;
import com.example.baseproject.mvvm.util.AppUtils;

import java.util.List;

public class AppService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotification();

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
        }

        if (appProcesses != null)
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i("AppService", "onStartCommand: " + new AppUtils().getAppName(this,appProcess.processName));
                }
            }

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.service_layout);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
