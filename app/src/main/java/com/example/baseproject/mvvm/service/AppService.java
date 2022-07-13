package com.example.baseproject.mvvm.service;

import static com.example.baseproject.mvvm.service.MyApplication.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.baseproject.R;
import com.example.baseproject.mvvm.util.AppUtils;

import java.util.List;

public class AppService extends Service {
    private Handler mHandler;
    private boolean mIsAlive;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIsAlive = true;
        if (intent != null && intent.hasExtra("ACTION")) {
            stopSelf();
            stopForeground(true);
            mIsAlive = false;
        } else {
            createNotification();
            mHandler = new Handler();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateNotification();
                    mHandler.postDelayed(this, 2000);
                }
            });
        }
        return START_NOT_STICKY;
    }

    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    private void updateNotification() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long mCurrentTime = System.currentTimeMillis();
        long mStartTime = mCurrentTime - 24 * 60 * 60 * 1000;
        List<UsageStats> mUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, mStartTime, mCurrentTime);
        UsageStats mRecentStat = null;
        for (UsageStats usageStats : mUsageStats) {
            if (mRecentStat == null || mRecentStat.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                mRecentStat = usageStats;
            }
        }
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.service_layout);

        if (mRecentStat != null && mRecentStat.getPackageName() != null) {
            mRemoteViews.setTextViewText(R.id.tv_app_name, new AppUtils().getAppName(getApplicationContext(), mRecentStat.getPackageName()));
            Drawable mDrawable = new AppUtils().getAppIconByPackageName(getApplication(), mRecentStat.getPackageName());
            Bitmap mBitmap = getBitmapFromDrawable(mDrawable);
            mRemoteViews.setImageViewBitmap(R.id.iv_app_icon, mBitmap);
        }

        Intent mIntent = new Intent(this, AppService.class);
        mIntent.putExtra("ACTION", 1);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_close, pendingIntent);

        Notification mNotification = new NotificationCompat.Builder(getApplication(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(mRemoteViews)
                .setCustomBigContentView(mRemoteViews)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(NOTIFICATION_SERVICE);

        if (mIsAlive)
            mNotificationManager.notify(1, mNotification);
    }

    private void createNotification() {
        RemoteViews mRemoteView = new RemoteViews(getPackageName(), R.layout.service_layout);
        Intent mIntent = new Intent(this, AppService.class);
        mIntent.putExtra("ACTION", 1);
        PendingIntent mPendingIntent = PendingIntent.getService(this, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        mRemoteView.setOnClickPendingIntent(R.id.btn_close, mPendingIntent);

        Notification mNotification = new NotificationCompat.Builder(getApplication(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(mRemoteView)
                .setCustomBigContentView(mRemoteView)
                .build();
        startForeground(1, mNotification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
