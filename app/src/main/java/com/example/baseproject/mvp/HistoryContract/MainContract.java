package com.example.baseproject.mvp.HistoryContract;

import com.example.baseproject.mvvm.model.AppInfo;

import java.util.List;

public interface MainContract {
    interface View{
        void onHasPermission();
        void requestPermission();

        void onQueryAppComplete(List<AppInfo> appInfoList, long mMaxDuration);
    }

    interface Presenter{
        void checkPermission();

        void queryApp();
    }
}
