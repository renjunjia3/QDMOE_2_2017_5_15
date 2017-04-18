package com.hfaufhreu.hjfeuio.ui.fragment.vip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.base.BaseMainFragment;
import com.hfaufhreu.hjfeuio.ui.fragment.mine.MineFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class GlodVipFragment extends BaseMainFragment {
    private Unbinder unbinder;

    public static GlodVipFragment newInstance() {

        Bundle args = new Bundle();
        GlodVipFragment fragment = new GlodVipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_glod_vip, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
