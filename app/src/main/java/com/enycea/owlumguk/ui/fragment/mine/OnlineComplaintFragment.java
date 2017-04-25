package com.enycea.owlumguk.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.enycea.owlumguk.R;
import com.enycea.owlumguk.base.BaseBackFragment;
import com.enycea.owlumguk.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 在线投诉
 * Created by Administrator on 2017/3/16.
 */

public class OnlineComplaintFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    private ToastUtils toastUtils;

    public static OnlineComplaintFragment newInstance() {
        OnlineComplaintFragment fragment = new OnlineComplaintFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_online_complaint, container, false);
        ButterKnife.bind(this, view);
        toolbarTitle.setText("在线投诉");
        initToolbarNav(toolbar);
        toastUtils = ToastUtils.getInstance(_mActivity);
        return view;
    }

    @OnClick(R.id.submit)
    public void onClickSubmit() {
        if (number.getText().toString().trim().isEmpty()) {
            toastUtils.showToast("请输入您的联系方式");
            return;
        }
        if (content.getText().toString().trim().isEmpty()) {
            toastUtils.showToast("请输入您的投诉内容");
            content.setEnabled(true);
            return;
        }
        ToastUtils.getInstance(_mActivity).showToast("我们已收到您的投诉");
        _mActivity.onBackPressed();
    }


}
