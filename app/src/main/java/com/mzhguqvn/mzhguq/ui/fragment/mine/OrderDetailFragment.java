package com.mzhguqvn.mzhguq.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
import com.mzhguqvn.mzhguq.bean.OrderInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:订单详情页
 * package:com.mzhguqvn.mzhguq.ui.fragment.mine
 * Author：scene on 2017/5/10 18:01
 */

public class OrderDetailFragment extends BaseBackFragment {
    private static final String ARG_ORDER_INFO = "order_info";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.status)
    TextView status;
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
    private OrderInfo orderInfo;

    public static OrderDetailFragment newInstance(OrderInfo orderInfo) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER_INFO, orderInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            orderInfo = (OrderInfo) args.getSerializable(ARG_ORDER_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("订单详情");
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
    }

    private void initView() {
        if (orderInfo != null) {
            switch (orderInfo.getStatus()) {
                case 1://未付款
                    status.setText("未付款");
                    break;
                case 2:
                    //已付款
                    status.setText("卖家发货中");
                    break;
                case 3:
                    //已发货
                    status.setText("卖家已发货");
                    break;
                case 4:
                    //已完成
                    status.setText("已完成");
                    break;
                case 5:
                    //未付款
                    status.setText("未付款");
                    break;
            }


            Glide.with(getContext()).load(orderInfo.getGoods_thumb()).centerCrop().into(image);
            orderId.setText(orderInfo.getOrder_id());
            goodsName.setText(orderInfo.getGood_name());
            price.setText("￥" + orderInfo.getPrice());
            totalPrice.setText("￥" + orderInfo.getMoney());
            number.setText(orderInfo.getNumber() + "件");
            receiverName.setText(orderInfo.getAddress_name());
            receiverAddress.setText(orderInfo.getAddress());
            receiverPhone.setText(orderInfo.getMobile());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
