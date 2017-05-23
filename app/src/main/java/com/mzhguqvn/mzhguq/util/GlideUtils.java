package com.mzhguqvn.mzhguq.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mzhguqvn.mzhguq.R;

/**
 * Case By:glide工具类
 * package:com.mzhguqvn.mzhguq.util
 * Author：scene on 2017/5/16 10:47
 */

public class GlideUtils {
    public static void loadImage(Context context, ImageView imageView, String url) {
        if (url.endsWith("gif")) {
            Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imageView);
        } else {
            Glide.with(context).load(url).asBitmap().centerCrop().placeholder(R.drawable.bg_error).error(R.drawable.bg_error).into(imageView);
        }
    }

    public static void loadImage(Fragment context, ImageView imageView, String url) {
        if (url.endsWith("gif")) {
            Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imageView);
        } else {
            Glide.with(context).load(url).asBitmap().centerCrop().placeholder(R.drawable.bg_error).error(R.drawable.bg_error).into(imageView);
        }
    }

    public static void loadImage(Activity context, ImageView imageView, String url) {
        if (url.endsWith("gif")) {
            Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imageView);
        } else {
            Glide.with(context).load(url).asBitmap().centerCrop().placeholder(R.drawable.bg_error).error(R.drawable.bg_error).into(imageView);
        }
    }

    public static void loadImage(FragmentActivity context, ImageView imageView, String url) {
        if (url.endsWith("gif")) {
            Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imageView);
        } else {
            Glide.with(context).load(url).asBitmap().centerCrop().placeholder(R.drawable.bg_error).error(R.drawable.bg_error).into(imageView);
        }
    }

    public static void loadImage(android.app.Fragment context, ImageView imageView, String url) {
        if (url.endsWith("gif")) {
            Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imageView);
        } else {
            Glide.with(context).load(url).asBitmap().centerCrop().placeholder(R.drawable.bg_error).error(R.drawable.bg_error).into(imageView);
        }
    }

}
