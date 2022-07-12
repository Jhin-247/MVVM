package com.example.baseproject.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.R;
import com.example.baseproject.mvp.HistoryContract.MainContract;
import com.example.baseproject.mvp.HistoryContract.MainPresenter;
import com.example.baseproject.mvvm.model.AppInfo;
import com.example.baseproject.mvvm.service.AppService;
import com.example.baseproject.view.LoadingDialog;

import java.util.List;

public class HistoryMVPActivity extends AppCompatActivity implements MainContract.View {

    private static final String TAG = HistoryMVPActivity.class.getSimpleName();
    private MainPresenter mPresenter;
    private AppUsageAdapter mAdapter;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_mvp);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mPresenter = new MainPresenter(this, this);

        initView();
    }

    private void initView() {
        RecyclerView mRcvAppList = findViewById(R.id.rcv_app_info);
        AppCompatButton mButton = findViewById(R.id.btn_show);
        mAdapter = new AppUsageAdapter();
        mLoadingDialog = new LoadingDialog(this);

        mRcvAppList.setLayoutManager(new LinearLayoutManager(this));
        mRcvAppList.setAdapter(mAdapter);

        mButton.setOnClickListener(v -> {
            mPresenter.checkPermission();
        });
    }

    @Override
    public void onHasPermission() {
        mLoadingDialog.show();
        mPresenter.queryApp();
        startService(new Intent(this, AppService.class));
    }

    @Override
    public void requestPermission() {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    @Override
    public void onQueryAppComplete(List<AppInfo> appInfoList, long mMaxDuration) {
        runOnUiThread(() -> {
            mLoadingDialog.cancel();
            mAdapter.setMaxDuration(mMaxDuration);
            mAdapter.setAppList(appInfoList);
        });
    }
}
