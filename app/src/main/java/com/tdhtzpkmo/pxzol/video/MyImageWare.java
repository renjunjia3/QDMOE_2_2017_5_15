package com.tdhtzpkmo.pxzol.video;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;

import java.lang.ref.WeakReference;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.util.SystemClock;

public class MyImageWare extends NonViewAware {

    public long start;
    public int id;
    public WeakReference<IDanmakuView> danmakuViewRef;
    public BaseDanmaku danmaku;
    public Bitmap bitmap;

    public MyImageWare(String imageUri, BaseDanmaku danmaku, int width, int height, IDanmakuView danmakuView) {
        this(imageUri, new ImageSize(width, height), ViewScaleType.FIT_INSIDE);
        if (danmaku == null) {
            throw new IllegalArgumentException("danmaku may not be null");
        }
        this.danmaku = danmaku;
        this.id = danmaku.hashCode();
        this.danmakuViewRef = new WeakReference<>(danmakuView);
        this.start = SystemClock.uptimeMillis();
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public MyImageWare(ImageSize imageSize, ViewScaleType scaleType) {
        super(imageSize, scaleType);
    }

    public MyImageWare(String imageUri, ImageSize imageSize, ViewScaleType scaleType) {
        super(imageUri, imageSize, scaleType);
    }

    @Override
    public boolean setImageDrawable(Drawable drawable) {
        return super.setImageDrawable(drawable);
    }

    @Override
    public boolean setImageBitmap(Bitmap bitmap) {
        if (this.danmaku.text.toString().contains("textview")) {
            Log.e("DFM", (SystemClock.uptimeMillis() - this.start) + "ms=====> inside" + danmaku.tag + ":" + danmaku.getActualTime() + ",url: bitmap" + (bitmap == null));
        }
        this.bitmap = toRoundBitmap(bitmap);
        IDanmakuView danmakuView = danmakuViewRef.get();
        if (danmakuView != null) {
            danmakuView.invalidateDanmaku(danmaku, true);
        }
        return true;
    }

    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}
