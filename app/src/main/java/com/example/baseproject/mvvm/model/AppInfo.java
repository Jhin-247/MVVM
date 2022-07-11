package com.example.baseproject.mvvm.model;

import android.graphics.drawable.Drawable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class AppInfo {
    private String mAppName;
    private String mPackageName;
    private Drawable mAppIcon;
    private long mUsedDuration;
    private int mProgressRatio;

    private String mTotalTimeString;
    public AppInfo() {
    }

    public String getTotalTimeString() {
        getTotalUsedTime();
        return mTotalTimeString;
    }

    public void setTotalTimeString(String mTotalTimeString) {
        this.mTotalTimeString = mTotalTimeString;
    }

    public void getTotalUsedTime() {
        String finalTimerString = "";
        String secondsString = "";
        int hours = (int) (mUsedDuration / (1000 * 60 * 60));
        int minutes = (int) (mUsedDuration % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((mUsedDuration % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            finalTimerString = hours + " hours ";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if (minutes < 10) {
            finalTimerString = finalTimerString + "0" + minutes + " minutes " + secondsString + " seconds";
        } else {
            finalTimerString = finalTimerString + minutes + " minutes " + secondsString + "seconds";
        }
        mTotalTimeString = finalTimerString;
    }

    public int getProgressRatio() {
        return mProgressRatio;
    }

    public long getUsedDuration() {
        return mUsedDuration;
    }

    public void setUsedDuration(long mUsedDuration) {
        this.mUsedDuration = mUsedDuration;
    }

    public String getUsedDurationAsString() {
        return String.valueOf(mProgressRatio);
    }

    public void setRatio(long maxDuration) {
        this.mProgressRatio = (int) ((float) ((float) mUsedDuration / (float) maxDuration) * 100000);
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public Drawable getAppIcon() {
        return mAppIcon;
    }

    public void setAppIcon(Drawable mAppIcon) {
        this.mAppIcon = mAppIcon;
    }

}
