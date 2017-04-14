package fm.jiecao.jcvideoplayer_lib;

import android.graphics.Bitmap;
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
//            if (this.danmaku.isTimeOut() || this.danmaku.isFiltered()) {
//                return true;
//            }
        if (this.danmaku.text.toString().contains("textview")) {
            Log.e("DFM", (SystemClock.uptimeMillis() - this.start) + "ms=====> inside" + danmaku.tag + ":" + danmaku.getActualTime() + ",url: bitmap" + (bitmap == null));
        }
        this.bitmap = bitmap;
        IDanmakuView danmakuView = danmakuViewRef.get();
        if (danmakuView != null) {
            danmakuView.invalidateDanmaku(danmaku, true);
        }
        return true;
    }
}