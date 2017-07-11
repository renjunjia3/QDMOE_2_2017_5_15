package com.cl.cltv.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cl.cltv.R;
import com.cl.cltv.base.BaseBackFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By: 空内页
 * package:com.mzhguqvn.mzhguq.ui.fragment.mine
 * Author：scene on 2017/6/7 12:41
 */
public class EmptyContentFragment extends BaseBackFragment {
    public static final int TYPE_SYSTEM_MESSAGE = 1;
    public static final int TYPE_MY_DISCOUNT = 2;
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    private int type = TYPE_SYSTEM_MESSAGE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt(ARG_TYPE);
        }
    }

    public static EmptyContentFragment newInstance(int type) {
        EmptyContentFragment fragment = new EmptyContentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_empty_content, container, false);
        ButterKnife.bind(this, view);
        switch (type) {
            case TYPE_SYSTEM_MESSAGE:
                toolbarTitle.setText("系统消息");
                break;
            case TYPE_MY_DISCOUNT:
                toolbarTitle.setText("我的优惠");
                break;
        }
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        switch (type) {
            case TYPE_SYSTEM_MESSAGE:
                message.setText("暂无系统消息");
                break;
            case TYPE_MY_DISCOUNT:
                message.setText("暂无优惠信息");
                break;
        }
    }
}
