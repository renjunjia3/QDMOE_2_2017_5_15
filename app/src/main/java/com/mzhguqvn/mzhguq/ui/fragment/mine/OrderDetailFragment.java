package com.mzhguqvn.mzhguq.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.OrderLogisticAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
import com.mzhguqvn.mzhguq.base.LogisticInfo;
import com.mzhguqvn.mzhguq.bean.OrderInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.pull_loadmore.recyclerview.RecyclerAdapterWithHF;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DecimalUtils;
import com.mzhguqvn.mzhguq.util.MD5Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private OrderInfo orderInfo;

    private List<LogisticInfo.TracesBean> list = new ArrayList<>();
    private OrderLogisticAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;

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
        MainActivity.upLoadPageInfo(PageConfig.ORDER_DETAIL_POSITOTN_ID, 0, 0);
    }

    private void initView() {
        adapter = new OrderLogisticAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        initHeaderView();

        if (!TextUtils.isEmpty(orderInfo.getDelivery_no()) && !TextUtils.isEmpty(orderInfo.getDelivery_code())) {
            getData();
        }
    }

    private void initHeaderView() {
        if (orderInfo != null) {

            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_order_detail_header, null);
            ImageView image = (ImageView) headerView.findViewById(R.id.image);
            TextView orderId = (TextView) headerView.findViewById(R.id.order_id);
            TextView goodsName = (TextView) headerView.findViewById(R.id.goods_name);
            TextView price = (TextView) headerView.findViewById(R.id.price);
            TextView totalPrice = (TextView) headerView.findViewById(R.id.total_price);
            TextView number = (TextView) headerView.findViewById(R.id.number);
            TextView receiverName = (TextView) headerView.findViewById(R.id.receiver_name);
            TextView receiverAddress = (TextView) headerView.findViewById(R.id.receiver_address);
            TextView receiverPhone = (TextView) headerView.findViewById(R.id.receiver_phone);
            TextView status = (TextView) headerView.findViewById(R.id.status);

            Glide.with(getContext()).load(orderInfo.getGoods_thumb()).centerCrop().into(image);
            orderId.setText(orderInfo.getOrder_id());
            goodsName.setText(orderInfo.getGood_name());
            price.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(orderInfo.getPrice()));
            totalPrice.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(orderInfo.getMoney()));
            number.setText(orderInfo.getNumber() + "件");
            receiverName.setText(orderInfo.getAddress_name());
            receiverAddress.setText(orderInfo.getAddress());
            receiverPhone.setText(orderInfo.getMobile());
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
                    //支付失败
                    status.setText("支付失败");
                    break;
            }
            mAdapter.addHeader(headerView);
        }
    }

    /**
     * Case By:获取物流信息
     * Author: scene on 2017/5/11 11:52
     */
    private void getData() {

        String requestData = "{'OrderCode':'','ShipperCode':'" + orderInfo.getDelivery_code() + "','LogisticCode':'" + orderInfo.getDelivery_no() + "'}";
        HashMap<String, String> params = new HashMap<>();
        params.put("RequestData", MD5Util.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", "1288065");
        params.put("RequestType", "1002");
        String dataSign = MD5Util.encrypt(requestData, "27b34826-a81f-4b7c-ad9e-4794c53220e5", "UTF-8");
        params.put("DataSign", dataSign);
        params.put("DataType", "2");
        OkHttpUtils.post().url(API.GET_LOGISTICS).params(params).tag("aa").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    LogisticInfo logisticInfo = JSON.parseObject(s, LogisticInfo.class);
                    if (logisticInfo.isSuccess()) {
                        list.addAll(logisticInfo.getTraces());
                        if (list != null && list.size() > 0) {
                            Collections.reverse(list);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag("aa");
        super.onDestroyView();
    }

}
