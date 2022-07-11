package com.example.baseproject.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.R;
import com.example.baseproject.mvp.HistoryContract.MainContract;
import com.example.baseproject.mvp.HistoryContract.MainPresenter;
import com.example.baseproject.mvvm.model.AppInfo;

import java.util.List;

public class HistoryMVPActivity extends AppCompatActivity implements MainContract.View {

    private MainPresenter mPresenter;
    private RecyclerView mRcvAppList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_mvp);

        mPresenter = new MainPresenter(this,this);

        initView();
    }

    private void initView() {

    }

    @Override
    public void onHasPermission() {

    }

    @Override
    public void requestPermission(String permission) {

    }

    @Override
    public void onQueryAppComplete(List<AppInfo> appInfoList) {

    }
}
