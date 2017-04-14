package com.dhuadhuei.hdeuji.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhuadhuei.hdeuji.R;
import com.dhuadhuei.hdeuji.base.BaseBackFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户协议和免责声明
 * Created by Administrator on 2017/3/15.
 */

public class AgreementFragment extends BaseBackFragment {
    public static final int TYPE_AGREEMENT = 1;
    public static final int TYPE_DISCLAIME = 2;
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    private int type = TYPE_AGREEMENT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt(ARG_TYPE);
        }
    }

    public static AgreementFragment newInstance(int type) {
        AgreementFragment fragment = new AgreementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_agreement, container, false);
        ButterKnife.bind(this, view);
        switch (type) {
            case TYPE_AGREEMENT:
                toolbarTitle.setText("用户协议");
                content.setText(getString(R.string.agreement));
                break;
            case TYPE_DISCLAIME:
                toolbarTitle.setText("免责声明");
                content.setText(getString(R.string.disclaime));
                break;
        }
        initToolbarNav(toolbar);
        return view;
    }
}
