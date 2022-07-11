package com.example.baseproject.mvp.HistoryContract;

import com.example.baseproject.mvvm.model.AppInfo;

import java.util.List;

public interface MainContract {
    interface View{
        void onHasPermission();
        void requestPermission(String permission);

        void onQueryAppComplete(List<AppInfo> appInfoList);
    }

    interface Presenter{
        void checkPermission(String permission);

        void queryApp();
    }
}
