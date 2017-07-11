package com.cl.cltv.ui.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cl.cltv.R;
import com.cl.cltv.base.BaseBackFragment;
import com.cl.cltv.bean.GoodsInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Case By:订单确定页面
 * package:com.mzhguqvn.mzhguq.ui.fragment.shop
 * Author：scene on 2017/5/12 13:35
 */

public class SubmitOrderFragment extends BaseBackFragment {
    private static final String ARG_GOODS_INFO = "arg_goods_info";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    Unbinder unbinder;

    private GoodsInfo info;

    public static SubmitOrderFragment newInstance(GoodsInfo goodsInfo) {
        SubmitOrderFragment fragment = new SubmitOrderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GOODS_INFO, goodsInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            info = (GoodsInfo) args.getSerializable(ARG_GOODS_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_submit_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("确认订单");
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
    }

    private void initView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
