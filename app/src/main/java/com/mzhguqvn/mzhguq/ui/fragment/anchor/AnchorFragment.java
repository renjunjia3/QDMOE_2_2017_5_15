package com.mzhguqvn.mzhguq.ui.fragment.anchor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.config.PageConfig;

import butterknife.ButterKnife;

/**
 * Case By:主播
 * package:com.mzhguqvn.mzhguq.ui.fragment.anchor
 * Author：scene on 2017/5/19 10:10
 */

public class AnchorFragment extends BaseMainFragment {

    public static AnchorFragment newInstance() {
        AnchorFragment fragment = new AnchorFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anchor, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.ANCHOR_POSITOTN_ID, 0, 0);
    }
}
