package com.mzhguqvn.mzhguq.ui.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.GoodsComment2Adapter;
import com.mzhguqvn.mzhguq.adapter.GoodsCommentAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
import com.mzhguqvn.mzhguq.bean.GoodsCommentInfo;
import com.mzhguqvn.mzhguq.util.API;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:商品评论
 * package:com.mzhguqvn.mzhguq.ui.fragment.shop
 * Author：scene on 2017/5/10 12:45
 */

public class GoodsCommentFragment extends BaseBackFragment {
    private static final String ARG_COMMENT_LIST = "arg_comment_list";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listview)
    ListView listview;

    private List<GoodsCommentInfo> list;
    private GoodsComment2Adapter adapter;

    public static GoodsCommentFragment newInstance(ArrayList<GoodsCommentInfo> lists) {
        GoodsCommentFragment fragment = new GoodsCommentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_COMMENT_LIST, lists);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            list = (List<GoodsCommentInfo>) args.getSerializable(ARG_COMMENT_LIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_goods_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("买家评价");
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        if (list != null) {
            adapter = new GoodsComment2Adapter(getContext(), list);
            listview.setAdapter(adapter);
        }
        uploadCurrentPage();
    }

    /**
     * Case By:上报当前页面
     * Author: scene on 2017/4/27 17:05
     */
    private void uploadCurrentPage() {
        Map<String, String> params = new HashMap<>();
        params.put("position_id", "19");
        params.put("user_id", App.USER_ID + "");
        OkHttpUtils.post().url(API.URL_PRE + API.UPLOAD_CURRENT_PAGE).params(params).build().execute(null);
    }

}
