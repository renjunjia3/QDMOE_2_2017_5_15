package com.mzhguqvn.mzhguq.ui.fragment.channel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.ui.fragment.mine.MineFragment;

import butterknife.ButterKnife;

/**
 * Case By:频道
 * package:com.mzhguqvn.mzhguq.ui.fragment.channel
 * Author：scene on 2017/6/1 12:06
 */

public class ChannelFragment extends BaseMainFragment {

    public static ChannelFragment newInstance() {
        return new ChannelFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.CHANNEL_POSITION_ID, 0, 0);
    }
}
