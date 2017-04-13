package com.heuewo.hiaodoipo.ui.fragment.index;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heuewo.hiaodoipo.R;
import com.heuewo.hiaodoipo.app.App;
import com.heuewo.hiaodoipo.base.BaseFragment;
import com.heuewo.hiaodoipo.pay.PayUtil;
import com.heuewo.hiaodoipo.ui.dialog.FunctionPayDialog;
import com.heuewo.hiaodoipo.ui.fragment.MainFragment;
import com.heuewo.hiaodoipo.util.ToastUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/24.
 */

public class SearchFragment extends BaseFragment {
    private FunctionPayDialog dialog;
    private FunctionPayDialog.Builder builder;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog();
        return view;
    }

    private void initDialog() {
        builder = new FunctionPayDialog.Builder(_mActivity);
        builder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByWeChat(_mActivity, 1, 0);
            }
        });

        builder.setAliPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ;
                PayUtil.getInstance().payByAliPay(_mActivity, 1, 0);
            }
        });
        dialog = builder.create();
    }

    @OnClick({R.id.search, R.id.hot1, R.id.hot2, R.id.hot3, R.id.hot4, R.id.hot5, R.id.hot6, R.id.hot7, R.id.hot8, R.id.hot9, R.id.hot10})
    public void onClickItem() {
        if (App.ISVIP == 0) {
            dialog.show();
            MainFragment.clickWantPay();
        } else {
            ToastUtils.getInstance(_mActivity).showToast("该功能完善中，敬请期待");
        }
    }

    @OnClick(R.id.cancel)
    public void onClickCancel() {
        _mActivity.onBackPressed();
    }
}
