package com.heuewo.hiaodoipo.ui.fragment.live;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.heuewo.hiaodoipo.R;
import com.heuewo.hiaodoipo.adapter.LiveAdapter;
import com.heuewo.hiaodoipo.app.App;
import com.heuewo.hiaodoipo.base.BaseMainFragment;
import com.heuewo.hiaodoipo.base.BaseRecyclerAdapter;
import com.heuewo.hiaodoipo.bean.LiveInfo;
import com.heuewo.hiaodoipo.event.StartBrotherEvent;
import com.heuewo.hiaodoipo.pay.PayUtil;
import com.heuewo.hiaodoipo.pull_loadmore.PtrClassicFrameLayout;
import com.heuewo.hiaodoipo.pull_loadmore.PtrDefaultHandler;
import com.heuewo.hiaodoipo.pull_loadmore.PtrFrameLayout;
import com.heuewo.hiaodoipo.pull_loadmore.recyclerview.RecyclerAdapterWithHF;
import com.heuewo.hiaodoipo.ui.dialog.LivePayDialog;
import com.heuewo.hiaodoipo.ui.fragment.MainFragment;
import com.heuewo.hiaodoipo.util.API;
import com.heuewo.hiaodoipo.util.NetWorkUtils;
import com.heuewo.hiaodoipo.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Created by scene on 2017/3/13.
 * 我的真实显示的fragment
 */

public class LiveFragment extends BaseMainFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;


    private List<LiveInfo> lists;

    private LiveAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;

    private LivePayDialog dialog;
    private LivePayDialog.Builder builder;

    public static LiveFragment newInstance() {

        Bundle args = new Bundle();
        LiveFragment fragment = new LiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        getLiveData(true);
    }

    private void initRecyclerView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);

        lists = new ArrayList<>();
        adapter = new LiveAdapter(LiveFragment.this, lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mAdapter = new RecyclerAdapterWithHF(adapter);
        View footerView = LayoutInflater.from(_mActivity).inflate(R.layout.recycler_footer, null);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.ISVIP == 1) {
                    ToastUtils.getInstance(_mActivity).showToast("该功能完善中，敬请期待");
                } else {
                    if (builder == null) {
                        builder = new LivePayDialog.Builder(_mActivity);
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
                                PayUtil.getInstance().payByAliPay(_mActivity, 1, 0);
                            }
                        });
                    }
                    if (dialog == null) {
                        dialog = builder.create();
                    }
                    dialog.show();
                    MainFragment.clickWantPay();
                }
            }
        });
        mAdapter.addFooter(footerView);
        recyclerView.setAdapter(mAdapter);
        View headView = LayoutInflater.from(_mActivity).inflate(R.layout.fragment_live_header, null);
        mAdapter.addHeader(headView);
        adapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                if (App.ISVIP == 0) {
                    EventBus.getDefault().post(new StartBrotherEvent(LiveDetailFragment.newInstance(lists.get(position))));

//                    if (builder == null) {
//                        builder = new LivePayDialog.Builder(_mActivity);
//                        builder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                PayUtil.getInstance().payByWeChat(getContext(), 1, 0);
//                            }
//                        });
//
//                        builder.setAliPayClickListener(new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                PayUtil.getInstance().payByAliPay(getContext(), 1, 0);
//                            }
//                        });
//                    }
//                    if (dialog == null) {
//                        dialog = builder.create();
//                    }
//                    dialog.show();
//                    MainFragment.clickWantPay();
                } else {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(lists.get(position).getUrl());
                    intent.setData(content_url);
                    startActivity(intent);
                }
            }
        });

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getLiveData(false);
            }
        });
    }

    private void getLiveData(boolean isShowLoading) {
        if (NetWorkUtils.isNetworkConnected(_mActivity)) {
            if (isShowLoading) {
                statusViewLayout.showLoading();
            }
            OkHttpUtils.get().url(API.URL_PRE + API.LIVE).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (lists.size() <= 0) {
                        statusViewLayout.showFailed(onClickRrtryListener);
                    } else {
                        if (!statusViewLayout.isContent()) {
                            statusViewLayout.showContent();
                        }
                    }
                    ptrLayout.refreshComplete();
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        List<LiveInfo> tempList = JSON.parseArray(s, LiveInfo.class);
                        lists.clear();
                        lists.addAll(tempList);
                        adapter.notifyDataSetChanged();
                        if (lists.size() == 0) {
                            statusViewLayout.showNone(onClickRrtryListener);
                        } else {
                            if (!statusViewLayout.isContent()) {
                                statusViewLayout.showContent();
                            }
                        }
                    } catch (Exception e) {
                        if (lists.size() <= 0) {
                            statusViewLayout.showFailed(onClickRrtryListener);
                        } else {
                            if (!statusViewLayout.isContent()) {
                                statusViewLayout.showContent();
                            }
                        }
                    } finally {
                        ptrLayout.refreshComplete();
                    }
                }
            });

        } else {
            if (lists.size() <= 0) {
                statusViewLayout.showNetError();
            } else {
                if (!statusViewLayout.isContent()) {
                    statusViewLayout.showContent();
                }
                ptrLayout.refreshComplete();
            }
        }
    }

    View.OnClickListener onClickRrtryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getLiveData(true);
        }
    };

}
