package com.example.baseproject.mvvm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.R;
import com.example.baseproject.databinding.ItemAppInfoBinding;
import com.example.baseproject.mvvm.model.AppInfo;

import java.util.List;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoHolder> {
    private LayoutInflater layoutInflater;

    private List<AppInfo> mAppList;

    public AppInfoAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<AppInfo> mAppList){
        this.mAppList = mAppList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemAppInfoBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_app_info, parent, false);
        return new AppInfoHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppInfoHolder holder, int position) {
        holder.bind(mAppList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAppList == null ? 0 : mAppList.size();
    }

    public static class AppInfoHolder extends RecyclerView.ViewHolder {
        ItemAppInfoBinding mBinding;

        public AppInfoHolder(@NonNull ItemAppInfoBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void bind(AppInfo appInfo) {
            mBinding.setAppInfo(appInfo);
            mBinding.executePendingBindings();
        }


    }
}
