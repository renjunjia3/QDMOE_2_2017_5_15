package com.cl.cltv.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.cltv.MainActivity;
import com.cl.cltv.R;
import com.cl.cltv.base.BaseBackFragment;
import com.cl.cltv.config.PageConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 投诉热线
 * Created by Administrator on 2017/3/16.
 */

public class HotLineFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.top_image)
    ImageView topImage;

    public static HotLineFragment newInstance() {
        HotLineFragment fragment = new HotLineFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_hot_line, container, false);
        ButterKnife.bind(this, view);
        toolbarTitle.setText("客服");
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.REBACK_POSITOTN_ID, 0, 0);
        Glide.with(getActivity()).load(R.drawable.bg_hot_line_top).into(topImage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
