package com.mzhguqvn.mzhguq.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

}
