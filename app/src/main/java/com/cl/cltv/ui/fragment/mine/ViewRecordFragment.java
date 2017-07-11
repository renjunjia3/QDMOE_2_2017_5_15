package com.cl.cltv.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cl.cltv.R;
import com.cl.cltv.base.BaseBackFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Case By: 观看记录等
 * package:
 * Author：scene on 2017/5/5 14:18
 */
public class ViewRecordFragment extends BaseBackFragment {
    public static final int TYPE_VIEW_RECORD = 1;
    public static final int TYPE_FRAVOTER = 2;
    public static final int TYPE_OFFLINE_VIDEO = 3;
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int type = TYPE_VIEW_RECORD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt(ARG_TYPE);
        }
    }

    public static ViewRecordFragment newInstance(int type) {
        ViewRecordFragment fragment = new ViewRecordFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_view_record, container, false);
        ButterKnife.bind(this, view);
        switch (type) {
            case TYPE_VIEW_RECORD:
                toolbar.setTitle("观看记录");
                break;
            case TYPE_FRAVOTER:
                toolbar.setTitle("我的收藏");
                break;
            case TYPE_OFFLINE_VIDEO:
                toolbar.setTitle("离线视频");
                break;
        }
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }
}
