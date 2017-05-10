package com.mzhguqvn.mzhguq.ui.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
import com.mzhguqvn.mzhguq.bean.GoodsInfo;
import com.mzhguqvn.mzhguq.bean.ReceiverInfo;
import com.mzhguqvn.mzhguq.config.AddressConfig;
import com.mzhguqvn.mzhguq.event.ChangeTabEvent;
import com.mzhguqvn.mzhguq.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Case By:购买商品成功的界面
 * package:com.mzhguqvn.mzhguq.ui.fragment.shop
 * Author：scene on 2017/5/10 15:37
 */

public class PaySuccessFragment extends BaseBackFragment {
    private static final String ARG_GOODS_INFO = "goods_info";
    private static final String ARG_RECEIVER_INFO = "receiver_info";
    private static final String ARG_BUY_NUMBER = "buy_number";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.order_id)
    TextView orderId;
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.receiver_name)
    TextView receiverName;
    @BindView(R.id.receiver_phone)
    TextView receiverPhone;
    @BindView(R.id.receiver_address)
    TextView receiverAddress;

    private GoodsInfo goodsInfo;
    private ReceiverInfo receiverInfo;

    private int buyNumber = 1;

    public static PaySuccessFragment newInstance(GoodsInfo goodsInfo, ReceiverInfo receiverInfo, int buyNumber) {
        PaySuccessFragment fragment = new PaySuccessFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GOODS_INFO, goodsInfo);
        args.putSerializable(ARG_RECEIVER_INFO, receiverInfo);
        args.putInt(ARG_BUY_NUMBER, buyNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            goodsInfo = (GoodsInfo) args.getSerializable(ARG_GOODS_INFO);
            receiverInfo = (ReceiverInfo) args.getSerializable(ARG_RECEIVER_INFO);
            buyNumber = args.getInt(ARG_BUY_NUMBER, 1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_pay_success, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("购买");
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
    }

    private void initView() {
        Glide.with(getContext()).load(goodsInfo.getThumb()).centerCrop().into(image);
        orderId.setText(App.order_id);
        goodsName.setText(goodsInfo.getName());
        price.setText("￥" + goodsInfo.getPrice());
        totalPrice.setText("￥" + (goodsInfo.getPrice() * buyNumber));
        number.setText(buyNumber + "件");
        receiverName.setText(receiverInfo.getReceiverName());
        receiverPhone.setText(receiverInfo.getReceiverPhone());
        receiverAddress.setText(receiverInfo.getReceiverProvince() + receiverInfo.getReceiverCity() + receiverInfo.getReceiverArea() + receiverInfo.getReceiverAddress());

        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_PROVINCE_KEY, receiverInfo.getReceiverProvince());
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_CITY_KEY, receiverInfo.getReceiverCity());
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_AREA_KEY, receiverInfo.getReceiverArea());
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_ADDRESS, receiverInfo.getReceiverAddress());
        SharedPreferencesUtil.putInt(getContext(), AddressConfig.ARG_PROVINCE_position, receiverInfo.getPositionProvince());
        SharedPreferencesUtil.putInt(getContext(), AddressConfig.ARG_CITY_POSITION, receiverInfo.getPositionCity());
        SharedPreferencesUtil.putInt(getContext(), AddressConfig.ARG_AREA_POSITION, receiverInfo.getPositionArea());
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_RECEIVER_NAME, receiverInfo.getReceiverName());
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_RECEIVER_PHONE, receiverInfo.getReceiverPhone());
    }

    @OnClick(R.id.ok)
    public void onClickOK() {
        if (App.isVip == 0) {
            App.isVip = 1;
            SharedPreferencesUtil.putInt(getContext(), App.ISVIP_KEY, App.isVip);
            //开通黄金会员
            if (_mActivity instanceof MainActivity) {
                ((MainActivity) _mActivity).changeTab(new ChangeTabEvent(App.isVip));
            }
        } else {
            _mActivity.onBackPressed();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if (App.isVip == 0) {
            App.isVip = 1;
            SharedPreferencesUtil.putInt(getContext(), App.ISVIP_KEY, App.isVip);
            //开通黄金会员
            if (_mActivity instanceof MainActivity) {
                ((MainActivity) _mActivity).changeTab(new ChangeTabEvent(App.isVip));
            }
        }
        return super.onBackPressedSupport();
    }
}
