package com.hfaufhreu.hjfeuio.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.pay.PayUtil;


/**
 * Case By:开通黑金会员
 * package:
 * Author：scene on 2017/4/18 13:53
 */
public class BlackGlodVipDialog extends Dialog {
    public BlackGlodVipDialog(@NonNull Context context) {
        super(context);
    }

    public BlackGlodVipDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected BlackGlodVipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private int videoId;

        private int type = 1;

        public Builder(Context context, int videoId) {
            this.context = context;
            this.videoId = videoId;
        }

        public BlackGlodVipDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final BlackGlodVipDialog dialog = new BlackGlodVipDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.dialog_black_glod_vip, null);

            ((TextView) layout.findViewById(R.id.diamond_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ((RadioGroup) layout.findViewById(R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                    if (checkedId == R.id.type_wechat) {
                        type = 1;
                    } else {
                        type = 2;
                    }
                }
            });
            layout.findViewById(R.id.open_vip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        PayUtil.getInstance().payByWeChat(context, dialog, 7, videoId);
                    } else {
                        PayUtil.getInstance().payByAliPay(context, dialog, 7, videoId);
                    }

                }
            });
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
