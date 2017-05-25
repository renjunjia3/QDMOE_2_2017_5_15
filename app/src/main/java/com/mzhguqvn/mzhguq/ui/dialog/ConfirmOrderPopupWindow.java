package com.mzhguqvn.mzhguq.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.VoucherInfo;
import com.mzhguqvn.mzhguq.util.DecimalUtils;

import java.util.List;

public class ConfirmOrderPopupWindow extends PopupWindow {

    private Context context;

    private SelectableRoundedImageView goodsImage;
    private TextView goodsPrice;
    private TextView goodsName;
    private TextView number;
    private TextView receiverName;
    private TextView receiverPhone;
    private TextView receiverAddress;
    private TextView voucherContent;
    private TextView totalPrice;

    private int payWayType = 1;
    private int buyNumber = 1;
    private double payTotalPrice = 0D;
    private double price = 0D;
    private int voucherId = 0;
    private VoucherInfo voucherInfo;

    public ConfirmOrderPopupWindow(final Context context, OnClickListener clickConfirmOrderListener, final OnClickListener clickChooseVoucherListener) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.pop_submit_dialog, null);

        receiverName = (TextView) mView.findViewById(R.id.receiver_name);
        receiverPhone = (TextView) mView.findViewById(R.id.receiver_phone);
        receiverAddress = (TextView) mView.findViewById(R.id.receiver_address);
        goodsImage = (SelectableRoundedImageView) mView.findViewById(R.id.goods_image);
        goodsName = (TextView) mView.findViewById(R.id.goods_name);
        goodsPrice = (TextView) mView.findViewById(R.id.goods_price);
        receiverAddress = (TextView) mView.findViewById(R.id.receiver_address);
        totalPrice = (TextView) mView.findViewById(R.id.total_price);
        number = (TextView) mView.findViewById(R.id.number);
        voucherContent = (TextView) mView.findViewById(R.id.voucher_content);
        mView.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrderPopupWindow.this.dismiss();
            }
        });
        mView.findViewById(R.id.confirm_pay).setOnClickListener(clickConfirmOrderListener);
        ((RadioGroup) mView.findViewById(R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                payWayType = checkedId == R.id.type_wechat ? 1 : 2;
            }
        });
        mView.findViewById(R.id.number_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNumber++;
                payTotalPrice = buyNumber * price;
                if (voucherInfo != null) {
                    payTotalPrice -= (voucherInfo.getMoney() / 100d);
                }
                totalPrice.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(payTotalPrice));
                number.setText(String.valueOf(buyNumber));
            }
        });
        mView.findViewById(R.id.number_less).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyNumber > 1) {
                    buyNumber--;
                    payTotalPrice = buyNumber * price;
                    if (voucherInfo != null) {
                        payTotalPrice -= (voucherInfo.getMoney() / 100d);
                    }
                    totalPrice.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(payTotalPrice));
                    number.setText(String.valueOf(buyNumber));
                }
            }
        });
        mView.findViewById(R.id.layout_voucher).setOnClickListener(clickChooseVoucherListener);

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //this.setHeight((int) (ScreenUtils.instance(context).getScreenHeight() * 1.0));
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.pop_animation);
        //设置SelectPicPopupWindow弹出窗体的背景
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
    }

    public void setReceiverName(String strReceiverName) {
        this.receiverName.setText(strReceiverName);
    }

    public void setReceiverPhone(String strReceiverPhone) {
        this.receiverPhone.setText(strReceiverPhone);
    }

    public void setReceiverAddress(String strReceiverAddress) {
        this.receiverAddress.setText(strReceiverAddress);
    }

    public void setGoodsImage(String strGoodsImage) {
        Glide.with(context).load(strGoodsImage).into(goodsImage);
    }

    public void setGoodsName(String strGoodsName) {
        this.goodsName.setText(strGoodsName);
    }

    public void setGoodsPrice(double goodsPrice) {
        this.price = goodsPrice;
        this.goodsPrice.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(price));
    }

    public void setGoodsNumber(int goodsNumber) {
        buyNumber = goodsNumber;
        this.number.setText(String.valueOf(buyNumber));
    }

    public void setTotalPrice(double goodsPrice, int goodsNumber) {
        payTotalPrice = goodsPrice * goodsNumber;
        this.totalPrice.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(payTotalPrice));
    }

    public void setVoucherList(List<VoucherInfo> voucherInfoList) {
        if (voucherInfoList == null || voucherInfoList.size() == 0) {
            voucherContent.setText("暂无代金券");
            voucherContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            voucherContent.setTextColor(Color.parseColor("#666666"));
        } else {
            voucherContent.setText(voucherInfoList.size() + "张");
            voucherContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_right_red, 0);
            voucherContent.setTextColor(Color.parseColor("#F63686"));
        }
    }

    public int getBuyNumber(){
        return buyNumber;
    }


    public void setVoucherInfo(VoucherInfo voucherInfo) {
        this.voucherInfo = voucherInfo;
        this.voucherId = voucherInfo.getId();
        voucherContent.setText("-￥" + DecimalUtils.formatPrice2BlankToBlank(voucherInfo.getMoney() / 100D));
        payTotalPrice = buyNumber * price;
        if (voucherInfo != null) {
            payTotalPrice -= (voucherInfo.getMoney() / 100d);
        }
        totalPrice.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(payTotalPrice));
    }


    public int getPayWayType() {
        return payWayType;
    }

    public int getVoucherId() {
        return voucherId;
    }


    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        setBackgroundAlpha(1.0f);
        super.dismiss();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setBackgroundAlpha(0.5f);
        super.showAtLocation(parent, gravity, x, y);
    }
}