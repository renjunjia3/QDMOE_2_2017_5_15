package com.mzhguqvn.mzhguq.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.util.ScreenUtils;
import com.mzhguqvn.mzhguq.util.ViewUtils;

/**
 * 观看完整视频需要支付的dialog
 * Created by Administrator on 2017/3/17.
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
            RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radio_group);
            if (radioGroup.getCheckedRadioButtonId() == R.id.weChatPay) {
                type = 1;
            } else {
                type = 2;
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
