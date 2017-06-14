package com.mzhguqvn.mzhguq.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.config.PayConfig;
import com.mzhguqvn.mzhguq.pay.PayUtil;
import com.mzhguqvn.mzhguq.util.ScreenUtils;


/**
 * Case By:升级砖石会员
 * package:
 * Author：scene on 2017/4/18 13:53
 */
public class DiamondVipDialog extends Dialog {
    public DiamondVipDialog(@NonNull Context context) {
        super(context);
    }

    public DiamondVipDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected DiamondVipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
        private int payWaytype = PayConfig.DEFAULT_PAY_WAY;
        private int vipType = 1;
        private int position_id = PageConfig.CLICK_OPEN_VIP_DIAMOND_MONTH;

        private TextView text1, text2;
        private TextView diamondTitle;
        private TextView price1, price2;
        private TextView oldPrice1, oldPrice2;
        private RadioGroup radioGroup;
        private LinearLayout layoutVipMonth, layoutVipYear;
        private ImageView vipMonth, vipYear;
        private TextView zhekou1, zhekou2;
        private TextView openVip;
        private ImageView imageNotice;

        public Builder(Context context, int videoId, int pay_position_id) {
            this.context = context;
            this.videoId = videoId;
            this.pay_position_id = pay_position_id;
        }

        public DiamondVipDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final DiamondVipDialog dialog = new DiamondVipDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_diamond_vip, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //获取id
            text1 = (TextView) layout.findViewById(R.id.text_1);
            text2 = (TextView) layout.findViewById(R.id.text_2);
            radioGroup = (RadioGroup) layout.findViewById(R.id.radio_group);
            price1 = (TextView) layout.findViewById(R.id.price_1);
            price2 = (TextView) layout.findViewById(R.id.price_2);
            oldPrice1 = (TextView) layout.findViewById(R.id.old_price_1);
            oldPrice2 = (TextView) layout.findViewById(R.id.old_price_2);
            layoutVipMonth = (LinearLayout) layout.findViewById(R.id.layout_vip_month);
            layoutVipYear = (LinearLayout) layout.findViewById(R.id.layout_vip_year);
            vipMonth = (ImageView) layout.findViewById(R.id.vipMonth);
            vipYear = (ImageView) layout.findViewById(R.id.vipYear);
            diamondTitle = (TextView) layout.findViewById(R.id.diamond_title);
            zhekou1 = (TextView) layout.findViewById(R.id.zhekou1);
            zhekou2 = (TextView) layout.findViewById(R.id.zhekou2);
            openVip = (TextView) layout.findViewById(R.id.open_vip);
            imageNotice = (ImageView) layout.findViewById(R.id.image_notice);
            if (payWaytype == PayConfig.PAY_BY_WECHAT) {
                radioGroup.check(R.id.type_wechat);
            } else {
                radioGroup.check(R.id.type_alipay);
            }
            switch (App.role) {
                case 0:
                    //游客直接开通钻石会员
                    text1.setText("开通");
                    text2.setText("开通");
                    price1.setText("￥68.00");
                    oldPrice1.setText("原价:136.00");
                    price2.setText("￥98.00");
                    oldPrice2.setText("原价:196.00");
                    layoutVipMonth.setVisibility(View.VISIBLE);
                    layoutVipYear.setVisibility(View.VISIBLE);
                    zhekou1.setText(String.valueOf(5));
                    zhekou2.setText(String.valueOf(5));
                    imageNotice.setImageResource(R.drawable.ic_dialog_diamond_notice_open);
                    break;
                case 1:
                    //包月黄金升级钻石
                    text1.setText("升级");
                    text2.setText("升级");
                    price1.setText("￥30.00");
                    oldPrice1.setText("原价:60.00");
                    price2.setText("￥60.00");
                    oldPrice2.setText("原价:120.00");
                    layoutVipMonth.setVisibility(View.VISIBLE);
                    layoutVipYear.setVisibility(View.VISIBLE);
                    zhekou1.setText(String.valueOf(5));
                    zhekou2.setText(String.valueOf(5));
                    openVip.setText("立即升级");
                    imageNotice.setImageResource(R.drawable.ic_dialog_diamond_notice);
                    break;
                case 2:
                    //包年黄金升级钻石
                    text1.setText("升级");
                    text2.setText("升级");
                    price1.setText("￥30.00");
                    oldPrice1.setText("原价:60.00");
                    diamondTitle.setText("钻石包年");
                    layoutVipMonth.setVisibility(View.VISIBLE);
                    layoutVipYear.setVisibility(View.GONE);
                    zhekou1.setText(String.valueOf(5));
                    zhekou2.setText(String.valueOf(5));
                    openVip.setText("立即升级");
                    imageNotice.setImageResource(R.drawable.ic_dialog_diamond_notice);
                    break;
            }
            oldPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            oldPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //设置选中开通包年还是包月
            layoutVipMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vipMonth.setImageResource(R.drawable.ic_vip_type_s);
                    vipYear.setImageResource(R.drawable.ic_vip_type_d);
                    vipType = 1;
                }
            });

            layoutVipYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vipMonth.setImageResource(R.drawable.ic_vip_type_d);
                    vipYear.setImageResource(R.drawable.ic_vip_type_s);
                    vipType = 2;
                }
            });
            //关闭dialog
            layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.open_vip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int realVipType = 1;
                    switch (App.role) {
                        case 0:
                            //直接开通包年或者包月钻石会员
                            realVipType = vipType == 1 ? PayUtil.VIP_TYPE_OPEN_DIAMOND_MONTH : PayUtil.VIP_TYPE_OPEN_DIAMOND_YEAR;
                            position_id = vipType == 1 ? PageConfig.CLICK_OPEN_VIP_DIAMOND_MONTH_DIRECT : PageConfig.CLICK_OPEN_VIP_DIAMOND_YEAR_DIRECT;
                            break;
                        case 1:
                            //升级包年或者包月钻石
                            realVipType = vipType == 1 ? PayUtil.VIP_TYPE_UPDATE_DIAMOND_MONTH : PayUtil.VIP_TYPE_UPDATE_DIAMOND_YEAR_FROM_MONTH;
                            position_id = vipType == 1 ? PageConfig.CLICK_OPEN_VIP_DIAMOND_MONTH : PageConfig.CLICK_OPEN_VIP_DIAMOND_YEAR;
                            break;
                        case 2:
                            //升级包年
                            realVipType = PayUtil.VIP_TYPE_UPDATE_DIAMOND_YEAR_FROM_YEAR;
                            position_id = PageConfig.CLICK_OPEN_VIP_DIAMOND_YEAR;
                            break;
                    }

                    if (radioGroup.getCheckedRadioButtonId() == R.id.type_wechat) {
                        payWaytype = PayConfig.PAY_BY_WECHAT;
                    } else {
                        payWaytype = PayConfig.PAY_BY_ALIPAY;
                    }
                    if (payWaytype == PayConfig.PAY_BY_WECHAT) {
                        PayUtil.getInstance().payByWeChat(context, realVipType, videoId, pay_position_id);
                    } else {
                        PayUtil.getInstance().payByAliPay(context, realVipType, videoId, pay_position_id);
                    }

                    MainActivity.upLoadPageInfo(position_id, videoId, pay_position_id);

                }
            });
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
