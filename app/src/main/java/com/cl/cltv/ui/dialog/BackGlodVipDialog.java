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

import com.cl.cltv.MainActivity;
import com.cl.cltv.R;
import com.cl.cltv.config.PageConfig;
import com.cl.cltv.config.PayConfig;
import com.cl.cltv.pay.PayUtil;
import com.cl.cltv.util.ScreenUtils;
import com.cl.cltv.util.ViewUtils;


/**
 * Case By:开通黄金VIP
 * package:
 * Author：scene on 2017/4/18 13:53
 */
public class BackGlodVipDialog extends Dialog {
    public BackGlodVipDialog(@NonNull Context context) {
        super(context);
    }

    public BackGlodVipDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected BackGlodVipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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

        private int payWayType = PayConfig.DEFAULT_PAY_WAY;
        private int vip_type = PayUtil.VIP_TYPE_OPEN_GLOD_DISCOUNT_MONTH;

        public Builder(Context context) {
            this.context = context;
        }

        public BackGlodVipDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final BackGlodVipDialog dialog = new BackGlodVipDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_back_glod_vip, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final ImageView glodChoosed = (ImageView) layout.findViewById(R.id.glod_choosed);
            final ImageView diamondChoosed = (ImageView) layout.findViewById(R.id.diamond_choosed);
            ImageView image = (ImageView) layout.findViewById(R.id.image);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height = (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(50)) * 3f / 5f);
            image.setLayoutParams(layoutParams);
            TextView discount = (TextView) dialog.findViewById(R.id.discount);
            SpannableStringBuilder builder = new SpannableStringBuilder(discount.getText().toString());
            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#D3121A"));
            builder.setSpan(redSpan, 10, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            discount.setText(builder);

            TextView oldPrice1 = (TextView) dialog.findViewById(R.id.old_price_1);
            TextView oldPrice2 = (TextView) dialog.findViewById(R.id.old_price_2);
            oldPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            oldPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.layout_type_glod).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vip_type = PayUtil.VIP_TYPE_OPEN_GLOD_DISCOUNT_MONTH;
                    glodChoosed.setImageResource(R.drawable.ic_vip_type_s);
                    diamondChoosed.setImageResource(R.drawable.ic_vip_type_d);
                }
            });
            layout.findViewById(R.id.layout_type_diamond).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vip_type = PayUtil.VIP_TYPE_OPEN_GLOD_DISCOUNT_YEAR;
                    glodChoosed.setImageResource(R.drawable.ic_vip_type_d);
                    diamondChoosed.setImageResource(R.drawable.ic_vip_type_s);
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
                        PayUtil.getInstance().payByWeChat(context, vip_type, 0, PageConfig.BACK_OPEN_VIP_POSITOTN_ID);
                    } else {
                        PayUtil.getInstance().payByAliPay(context, vip_type, 0, PageConfig.BACK_OPEN_VIP_POSITOTN_ID);
                    }
                    MainActivity.upLoadPageInfo(vip_type == PayUtil.VIP_TYPE_OPEN_GLOD_DISCOUNT_MONTH ?
                                    PageConfig.CLICK_OPEN_VIP_GLOD_MONTH_DIACOUNT : PageConfig.CLICK_OPEN_VIP_GLOD_YEAR_DIACOUNT,
                            0, PageConfig.BACK_OPEN_VIP_POSITOTN_ID);
                }
            });

            ViewUtils.setViewHeightByViewGroup(layout, (int) (ScreenUtils.instance(context).getScreenWidth() * 0.9f));
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
