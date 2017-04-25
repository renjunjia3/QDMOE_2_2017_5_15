package com.eexikn.uxcebc.ui.fragment.bbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.eexikn.uxcebc.R;
import com.eexikn.uxcebc.adapter.BBSAdapter;
import com.eexikn.uxcebc.app.App;
import com.eexikn.uxcebc.base.BaseMainFragment;
import com.eexikn.uxcebc.bean.BBSInfo;
import com.eexikn.uxcebc.itemdecoration.CustomItemDecotation;
import com.eexikn.uxcebc.pull_loadmore.PtrClassicFrameLayout;
import com.eexikn.uxcebc.pull_loadmore.PtrDefaultHandler;
import com.eexikn.uxcebc.pull_loadmore.PtrFrameLayout;
import com.eexikn.uxcebc.util.API;
import com.eexikn.uxcebc.util.DialogUtil;
import com.eexikn.uxcebc.util.NetWorkUtils;
import com.eexikn.uxcebc.util.ScreenUtils;
import com.eexikn.uxcebc.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:福利社
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class BBSFragment extends BaseMainFragment implements BBSAdapter.BBSItemOnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;

    private RequestCall call;

    private List<BBSInfo> lists;

    private BBSAdapter adapter;

    public static BBSFragment newInstance() {
        Bundle args = new Bundle();
        BBSFragment fragment = new BBSFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        getBBSData(true);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getBBSData(false);
            }
        });

        lists = new ArrayList<>();
        adapter = new BBSAdapter(getContext(), lists);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        ScreenUtils screenUtils = ScreenUtils.instance(getContext());
        recyclerView.addItemDecoration(new CustomItemDecotation(screenUtils.dp2px(10f), screenUtils.dp2px(10), 2, true));
        recyclerView.setAdapter(adapter);

        adapter.setBbsItemOnClickListener(this);

    }

    /**
     * Case By:
     * Author: scene on 2017/4/18 13:05
     * 获取bbs数据
     */
    private void getBBSData(final boolean isShowLoading) {
        if (NetWorkUtils.isNetworkConnected(_mActivity)) {
            if (isShowLoading) {
                statusViewLayout.showLoading();
            }

            call = OkHttpUtils.get().url(API.URL_PRE + API.BBS_LIST).build();
            call.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (isShowLoading) {
                        statusViewLayout.showFailed(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        lists.clear();
                        lists.addAll(JSON.parseArray(s, BBSInfo.class));
                        adapter.notifyDataSetChanged();
                        statusViewLayout.showContent();
                        ptrLayout.refreshComplete();
                    } catch (Exception e) {
                        if (isShowLoading) {
                            statusViewLayout.showFailed(retryListener);
                        } else {
                            ptrLayout.refreshComplete();
                        }
                    }
                }
            });
        } else {
            if (isShowLoading) {
                statusViewLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
        }


    }


    /**
     * Case By:列表的点击事件
     * Author: scene on 2017/4/18 13:52
     *
     * @param position 下标
     */
    @Override
    public void onBBsItemOnClick(int position) {
        if (App.isVip == 0) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为会员功能，请成为会员后使用", App.isVip, true);
        } else if (App.isVip == 1) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为钻石会员功能，请升级钻石会员后使用", App.isVip, true);
        } else {
            ToastUtils.getInstance(getContext()).showToast("该功能完善中，敬请期待");
        }
    }

    @Override
    public void onDestroyView() {
        if (call != null) {
            call.cancel();
        }
        super.onDestroyView();
    }

    View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getBBSData(true);
        }
    };

}
