package com.mzhguqvn.mzhguq.ui.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
import com.mzhguqvn.mzhguq.bean.GoodsInfo;
import com.mzhguqvn.mzhguq.bean.ReceiverInfo;
import com.mzhguqvn.mzhguq.config.AddressConfig;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.event.ChangeTabEvent;
import com.mzhguqvn.mzhguq.util.DecimalUtils;
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
    private static final String ARG_VOUCHER_MONEY = "ARG_VOUCHER_MONEY";
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
    @BindView(R.id.voucher_money)
    TextView voucherMoneyView;
    @BindView(R.id.layout_voucher)
    LinearLayout layoutVoucher;

    private GoodsInfo goodsInfo;
    private ReceiverInfo receiverInfo;

    private int buyNumber = 1;
    private int voucherMoney = 0;

    public static PaySuccessFragment newInstance(GoodsInfo goodsInfo, ReceiverInfo receiverInfo, int buyNumber, int voucherMoney) {
        PaySuccessFragment fragment = new PaySuccessFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GOODS_INFO, goodsInfo);
        args.putSerializable(ARG_RECEIVER_INFO, receiverInfo);
        args.putInt(ARG_BUY_NUMBER, buyNumber);
        args.putInt(ARG_VOUCHER_MONEY, voucherMoney);
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
            voucherMoney = args.getInt(ARG_VOUCHER_MONEY, 0);
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
        MainActivity.upLoadPageInfo(PageConfig.SHOP_BUY_SUCCESS_POSITOTN_ID, 0, 0);
    }

    private void initView() {
        Glide.with(getContext()).load(goodsInfo.getThumb()).centerCrop().into(image);
        orderId.setText(App.order_id);
        goodsName.setText(goodsInfo.getName());
        price.setText("￥" + goodsInfo.getPrice());
        layoutVoucher.setVisibility(voucherMoney == 0 ? View.GONE : View.VISIBLE);
        voucherMoneyView.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(voucherMoney / 100D));
        totalPrice.setText("￥" + (goodsInfo.getPrice() * buyNumber - voucherMoney / 100D));

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
        if (App.role == 0) {
            App.role = 1;
            SharedPreferencesUtil.putInt(getContext(), App.ROLE_KEY, App.role);
            //开通黄金会员
            if (_mActivity instanceof MainActivity) {
                ((MainActivity) _mActivity).changeTab(new ChangeTabEvent(App.role));
            }
        } else {
            _mActivity.onBackPressed();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if (App.role == 0) {
            App.role = 1;
            SharedPreferencesUtil.putInt(getContext(), App.ROLE_KEY, App.role);
            //开通黄金会员
            if (_mActivity instanceof MainActivity) {
                ((MainActivity) _mActivity).changeTab(new ChangeTabEvent(App.role));
            }
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
