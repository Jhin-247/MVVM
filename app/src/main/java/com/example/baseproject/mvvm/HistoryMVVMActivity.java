package com.example.baseproject.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baseproject.R;
import com.example.baseproject.databinding.ActivityHistoryMvvmBinding;
import com.example.baseproject.mvvm.viewmodel.HistoryViewModel;
import com.example.baseproject.view.LoadingDialog;

public class HistoryMVVMActivity extends AppCompatActivity {

    private ActivityHistoryMvvmBinding mBinding;
    private AppInfoAdapter mAdapter;

    private LoadingDialog mLoadingDialog;

    private HistoryViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_mvvm);
        initData();


        mBinding.btnShow.setOnClickListener(v -> {
            checkPermission();
        });

    }

    private void initData() {
        mViewModel = new HistoryViewModel(this);
        mAdapter = new AppInfoAdapter();
        mLoadingDialog = new LoadingDialog(this);
    }

    private void checkPermission() {
        boolean granted = mViewModel.checkPermission();
        if (granted) {
            mLoadingDialog.show();
            mViewModel.startService();
            initView();
        } else {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    private void initView() {
        mViewModel.initData();
        mBinding.rcvAppInfo.setAdapter(mAdapter);
        mBinding.rcvAppInfo.setLayoutManager(new LinearLayoutManager(this));
        mViewModel.getMutableAppList().observe(this, appList -> {
            mAdapter.setMaxDuration(mViewModel.getMaxUseDuration());
            mAdapter.setData(appList);
            mLoadingDialog.cancel();
        });
    }

}
