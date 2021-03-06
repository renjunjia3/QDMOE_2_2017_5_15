package com.cl.cltv.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cl.cltv.R;
import com.cl.cltv.config.PayConfig;
import com.cl.cltv.pay.PayUtil;
import com.cl.cltv.util.ScreenUtils;


/**
 * Case By:VPN海外会员
 * package:
 * Author：scene on 2017/4/18 13:53
 */
public class CDNVipDialog extends Dialog {
    public CDNVipDialog(@NonNull Context context) {
        super(context);
    }

    public CDNVipDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CDNVipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenUtils.instance(getContext()).getScreenWidth() * 0.95f); // 宽度
        dialogWindow.setAttributes(lp);
        super.show();
    }

    public static class Builder {
        private Context context;
        private int videoId;
        private int pay_position_id;

        private int payWayType = PayConfig.DEFAULT_PAY_WAY;

        public Builder(Context context, int videoId, int pay_position_id) {
            this.context = context;
            this.videoId = videoId;
            this.pay_position_id = pay_position_id;
        }

        public CDNVipDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final CDNVipDialog dialog = new CDNVipDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.dialog_cdn_vip, null);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ImageView image = (ImageView) layout.findViewById(R.id.image);
            TextView oldPrice1 = (TextView) dialog.findViewById(R.id.old_price_1);
            oldPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            TextView discount = (TextView) dialog.findViewById(R.id.discount);
            SpannableStringBuilder builder = new SpannableStringBuilder(discount.getText().toString());
            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#D3121A"));
            builder.setSpan(redSpan, 10, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            discount.setText(builder);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height = (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(50)) * 3f / 5f);
            image.setLayoutParams(layoutParams);
            layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            final RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radio_group);
            if (payWayType == PayConfig.PAY_BY_WECHAT) {
                radioGroup.check(R.id.type_wechat);
            } else {
                radioGroup.check(R.id.type_alipay);
            }
            layout.findViewById(R.id.open_vip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (radioGroup.getCheckedRadioButtonId() == R.id.type_wechat) {
                        payWayType = PayConfig.PAY_BY_WECHAT;
                    } else {
                        payWayType = PayConfig.PAY_BY_ALIPAY;
                    }
                    if (payWayType == PayConfig.PAY_BY_WECHAT) {
                        PayUtil.getInstance().payByWeChat(context, PayUtil.VIP_TYPE_CDN, videoId, pay_position_id);
                    } else {
                        PayUtil.getInstance().payByAliPay(context, PayUtil.VIP_TYPE_CDN, videoId, pay_position_id);
                    }

                }
            });
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
