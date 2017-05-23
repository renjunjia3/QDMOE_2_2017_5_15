package com.mzhguqvn.mzhguq.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.util.SharedPreferencesUtil;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Case By:查看大图
 * package:com.mzhguqvn.mzhguq.ui.dialog
 * Author：scene on 2017/5/22 18:06
 */

public class BigImageDialog extends Dialog {
    private ImageView imageView;
    private String url="";

    public BigImageDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public BigImageDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    /**
     * Case By:
     * Author: scene on 2017/5/22 18:08
     * 初始化view
     */
    private void initView() {
        setContentView(R.layout.dialog_big_image);
        imageView = (ImageView) findViewById(R.id.image);
        if (!StringUtils.isEmpty(url))
            Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    public void setUrl(String url) {
        if (!this.url.equals(url)) {
            this.url = url;
            if (!StringUtils.isEmpty(url)) {
                Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        }

    }
}
