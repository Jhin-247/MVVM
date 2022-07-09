package com.example.baseproject.mvvm.model;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String mAppName;
    private String mPackageName;
    private Drawable mAppIcon;

    public AppInfo() {
    }

    public AppInfo(String mAppName, String mPackageName, Drawable mAppIcon) {
        this.mAppName = mAppName;
        this.mPackageName = mPackageName;
        this.mAppIcon = mAppIcon;
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
