package com.ofgvyiss.ofgvyi.video.danmu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.ofgvyiss.ofgvyi.R;


/**
 * 圆形的Drawable
 * Created by feiyang on 16/2/18.
 */
public class CircleDrawable extends Drawable {

    private Paint mPaint;
    private Bitmap mBitmap;
    private Bitmap mBitmapHeart;
    private boolean mHasHeart;

    private static final int BLACK_COLOR = 0xb2000000;//黑色 背景
    private static final int BLACKGROUDE_ADD_SIZE = 4;//背景比图片多出来的部分

    public CircleDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    /**
     * 右下角包含一个‘心’的圆形drawable
     *
     * @param context
     * @param bitmap
     * @param hasHeart
     */
    public CircleDrawable(Context context, Bitmap bitmap, boolean hasHeart) {
        this(bitmap);
        mHasHeart = hasHeart;
        if (hasHeart) {
            setBitmapHeart(context);
        }
    }

    private void setBitmapHeart(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_user_avatar);
        if (bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postScale(0.8f, 0.8f);
            mBitmapHeart = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawCircle(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2, getIntrinsicWidth() / 2, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

