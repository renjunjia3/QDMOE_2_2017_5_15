package com.hfaufhreu.hjfeuio.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.hfaufhreu.hjfeuio.util.ScreenUtils;
import com.hfaufhreu.hjfeuio.util.ViewUtils;

/**
 * 观看完整视频需要支付的dialog
 * Created by Administrator on 2017/3/17.
 */

public class FullVideoPayDialog extends Dialog {
    public FullVideoPayDialog(@NonNull Context context) {
        super(context);
    }

    public FullVideoPayDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected FullVideoPayDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private OnClickListener aliPayClickListener;
        private OnClickListener weChatPayClickListener;
        private int type = 1;

        public Builder(Context context) {
            this.context = context;
        }

        public void setAliPayClickListener(OnClickListener aliPayClickListener) {
            this.aliPayClickListener = aliPayClickListener;
        }

        public void setWeChatPayClickListener(OnClickListener weChatPayClickListener) {
            this.weChatPayClickListener = weChatPayClickListener;
        }

        public FullVideoPayDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final FullVideoPayDialog dialog = new FullVideoPayDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_full_video_pay, null);
            ((TextView) layout.findViewById(R.id.oldPrice)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ((RadioGroup) layout.findViewById(R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                    if (checkedId == R.id.weChatPay) {
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
                        if (weChatPayClickListener != null) {
                            weChatPayClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    } else {
                        if (aliPayClickListener != null) {
                            aliPayClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ViewUtils.setViewHeightByViewGroup(layout, (int) (ScreenUtils.instance(context).getScreenWidth() * 0.9f));
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
