package com.example.baseproject.mvvm;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baseproject.R;
import com.example.baseproject.databinding.ActivityHistoryMvvmBinding;
import com.example.baseproject.mvvm.viewmodel.HistoryViewModel;

public class HistoryMVVMActivity extends AppCompatActivity {

    private ActivityHistoryMvvmBinding mBinding;
    private AppInfoAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_mvvm);
        checkPermission();
    }

    private void checkPermission() {
        AppOpsManager appOpsManagerCompat = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManagerCompat.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        if (granted) {
            initView();
        } else {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    private void initView() {
        mAdapter = new AppInfoAdapter();
        HistoryViewModel mViewModel = new ViewModelProvider(this, new HistoryViewModel.MyViewModelFactory(this.getApplication())).get(HistoryViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.executePendingBindings();

        mBinding.rcvAppInfo.setAdapter(mAdapter);

        mBinding.rcvAppInfo.setLayoutManager(new LinearLayoutManager(this));
        mViewModel.getMutableAppList().observe(this, appList -> mAdapter.setData(appList));
    }

}
