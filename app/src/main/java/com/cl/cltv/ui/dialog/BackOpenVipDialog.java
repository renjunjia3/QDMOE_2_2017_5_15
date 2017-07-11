package com.cl.cltv.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.cl.cltv.R;
import com.cl.cltv.util.ScreenUtils;
import com.cl.cltv.util.ViewUtils;


/**
 * Case By: 退出的时候提示的界面
 * package:com.mzhguqvn.mzhguq.ui.dialog
 * Author：scene on 2017/5/22 10:13
 */
public class BackOpenVipDialog extends Dialog {


    public BackOpenVipDialog(@NonNull Context context) {
        super(context);
    }

    public BackOpenVipDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected BackOpenVipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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

        public BackOpenVipDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final BackOpenVipDialog dialog = new BackOpenVipDialog(context, R.style.Dialog);
            final View layout = inflater.inflate(R.layout.dialog_back_open_vip, null);
            final RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radio_group);

            layout.findViewById(R.id.open_vip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (radioGroup.getCheckedRadioButtonId() == R.id.weChatPay) {
                        type = 1;
                    } else {
                        type = 2;
                    }
                    if (type == 1) {
                        if (weChatPayClickListener != null) {
                            weChatPayClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    } else {
                        if (aliPayClickListener != null) {
                            aliPayClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    }
                }
            });
            layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ViewUtils.setDialogViewWidth(layout.findViewById(R.id.rootView), (int) (ScreenUtils.instance(context).getScreenWidth() * 0.6f));
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
