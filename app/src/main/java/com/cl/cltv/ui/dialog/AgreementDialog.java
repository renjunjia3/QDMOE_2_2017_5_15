package com.cl.cltv.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cl.cltv.R;
import com.cl.cltv.util.GetAssestDataUtil;
import com.cl.cltv.util.SharedPreferencesUtil;

/**
 * Case By:用户协议的对话框
 * package:com.mzhguqvn.mzhguq.ui.dialog
 * Author：scene on 2017/5/22 18:06
 */

public class AgreementDialog extends Dialog implements View.OnClickListener {
    private TextView content;
    private CheckBox checkBox;
    private TextView agreement;
    private TextView cancel;
    private ImageView close;

    public AgreementDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public AgreementDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    /**
     * Case By:
     * Author: scene on 2017/5/22 18:08
     * 初始化view
     */
    private void initView() {
        setContentView(R.layout.dialog_argeement);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        agreement = (TextView) findViewById(R.id.agreement);
        cancel = (TextView) findViewById(R.id.cancel);
        content = (TextView) findViewById(R.id.content);
        content.setMovementMethod(new ScrollingMovementMethod());
        String str = GetAssestDataUtil.getString(getContext(), "agreement");
        content.setText(Html.fromHtml(str));
        close = (ImageView) findViewById(R.id.close);

        agreement.setOnClickListener(this);
        cancel.setOnClickListener(this);
        close.setOnClickListener(this);

        setCanceledOnTouchOutside(false);

    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public void onClick(View v) {
        if (v == agreement) {
            if (checkBox.isChecked()) {
                cancel();
                SharedPreferencesUtil.putBoolean(getContext(), "isFirst", false);
            }
        } else {
            cancel();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
