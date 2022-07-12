package com.example.baseproject.mvp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baseproject.R;
import com.example.baseproject.mvvm.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppUsageAdapter extends RecyclerView.Adapter<AppUsageAdapter.AppUsageHolder> {
    private List<AppInfo> mAppList;
    private long mMaxDuration;

    public void setMaxDuration(long mMaxDuration){
        this.mMaxDuration = mMaxDuration;
    }

    public AppUsageAdapter() {
        mAppList = new ArrayList<>();
        mMaxDuration = 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAppList(List<AppInfo> appList){
        this.mAppList = appList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppUsageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_usage,parent,false);
        return new AppUsageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppUsageHolder holder, int position) {
        AppInfo mAppInfo = mAppList.get(position);
        mAppInfo.setRatio(mMaxDuration);
        Glide.with(holder.itemView.getContext()).load(mAppInfo.getAppIcon()).into(holder.mAppIcon);

        holder.mAppName.setText(mAppInfo.getAppName());
        holder.mAppUsage.setText(mAppInfo.getTotalTimeString());
        holder.mProgressBar.setProgress(mAppInfo.getProgressRatio());

    }

    @Override
    public int getItemCount() {
        return mAppList == null ? 0 : mAppList.size();
    }

    public static class AppUsageHolder extends RecyclerView.ViewHolder{
        ImageView mAppIcon;
        TextView mAppName;
        TextView mAppUsage;
        ProgressBar mProgressBar;

        public AppUsageHolder(@NonNull View itemView) {
            super(itemView);
            mAppIcon = itemView.findViewById(R.id.iv_app_icon);
            mAppName = itemView.findViewById(R.id.tv_app_name);
            mProgressBar = itemView.findViewById(R.id.current_progress);
            mAppUsage = itemView.findViewById(R.id.tv_app_duration);
        }
    }
}
