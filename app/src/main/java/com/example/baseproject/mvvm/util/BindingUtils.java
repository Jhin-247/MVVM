package com.example.baseproject.mvvm.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingUtils {
    @BindingAdapter("image")
    public static void setImage(ImageView imageView, Drawable drawable){
        imageView.setImageDrawable(drawable);
    }
}
