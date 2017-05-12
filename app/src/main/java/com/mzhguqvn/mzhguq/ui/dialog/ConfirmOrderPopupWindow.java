package com.mzhguqvn.mzhguq.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.ui.fragment.shop.ShopFragment;
import com.mzhguqvn.mzhguq.util.ScreenUtils;

import java.math.BigDecimal;

public class ConfirmOrderPopupWindow extends PopupWindow {

    private Context context;
    private View mView;

    private ImageView close;
    private TextView receiverName;
    private TextView receiverPhone;
    private TextView receiverAddress;
    private ImageView goodsImage;
    private TextView goodsName;
    private TextView goodsPrice;
    private TextView goodsNumber;
    private TextView totalPrice;
    private RadioGroup radioGroup;
    private TextView confirmPay;

    private int payWayType = 1;


    public ConfirmOrderPopupWindow(Context context, OnClickListener clickConfirmOrderListener) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_submit_dialog, null);

        close = (ImageView) mView.findViewById(R.id.close);
        receiverName = (TextView) mView.findViewById(R.id.receiver_name);
        receiverPhone = (TextView) mView.findViewById(R.id.receiver_phone);
        receiverAddress = (TextView) mView.findViewById(R.id.receiver_address);
        goodsImage = (ImageView) mView.findViewById(R.id.goods_image);
        goodsName = (TextView) mView.findViewById(R.id.goods_name);
        goodsPrice = (TextView) mView.findViewById(R.id.goods_price);
        receiverAddress = (TextView) mView.findViewById(R.id.receiver_address);
        goodsNumber = (TextView) mView.findViewById(R.id.goods_number);
        totalPrice = (TextView) mView.findViewById(R.id.total_price);
        radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);
        confirmPay = (TextView) mView.findViewById(R.id.confirm_pay);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrderPopupWindow.this.dismiss();
            }
        });
        confirmPay.setOnClickListener(clickConfirmOrderListener);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                payWayType = checkedId == R.id.pay_way_wechat ? 1 : 2;
            }
        });
        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight((int) (ScreenUtils.instance(context).getScreenHeight() * 0.8));
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.pop_animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
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
        this.goodsPrice.setText("￥：" + (App.isVip > 0 ?
                new BigDecimal(goodsPrice * ShopFragment.DISCOUNT).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() : goodsPrice));
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber.setText("" + goodsNumber);
    }

    public void setTotalPrice(double goodsPrice, int goodsNumber) {
        this.totalPrice.setText("￥：" + (App.isVip > 0 ?
                new BigDecimal(goodsPrice * ShopFragment.DISCOUNT * goodsNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
                : new BigDecimal(goodsPrice * goodsNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
    }

    public int getPayWayType() {
        return payWayType;
    }
}