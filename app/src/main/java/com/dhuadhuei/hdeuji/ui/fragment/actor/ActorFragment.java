package com.dhuadhuei.hdeuji.ui.fragment.actor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.dhuadhuei.hdeuji.R;
import com.dhuadhuei.hdeuji.adapter.ActorAdapter;
import com.dhuadhuei.hdeuji.app.App;
import com.dhuadhuei.hdeuji.base.BaseMainFragment;
import com.dhuadhuei.hdeuji.base.BaseRecyclerAdapter;
import com.dhuadhuei.hdeuji.bean.Actor;
import com.dhuadhuei.hdeuji.event.StartBrotherEvent;
import com.dhuadhuei.hdeuji.pay.PayUtil;
import com.dhuadhuei.hdeuji.pull_loadmore.PtrClassicFrameLayout;
import com.dhuadhuei.hdeuji.pull_loadmore.PtrDefaultHandler;
import com.dhuadhuei.hdeuji.pull_loadmore.PtrFrameLayout;
import com.dhuadhuei.hdeuji.pull_loadmore.recyclerview.RecyclerAdapterWithHF;
import com.dhuadhuei.hdeuji.ui.dialog.FullVideoPayDialog;
import com.dhuadhuei.hdeuji.ui.fragment.MainFragment;
import com.dhuadhuei.hdeuji.util.API;
import com.dhuadhuei.hdeuji.util.NetWorkUtils;
import com.dhuadhuei.hdeuji.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by scene on 2017/3/13.
 * 女优
 */

public class ActorFragment extends BaseMainFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    wiki.scene.statuslib.StatusViewLayout statusViewLayout;

    private ActorAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private List<Actor> list;

    private FullVideoPayDialog dialog;
    private FullVideoPayDialog.Builder builder;

    public static ActorFragment newInstance() {

        Bundle args = new Bundle();
        ActorFragment fragment = new ActorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        getActorListData(true);
    }

    /*
    初始化RecyclerView
    */
    private void initRecyclerView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        list = new ArrayList<>();
        adapter = new ActorAdapter(ActorFragment.this, list);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        View footerView = LayoutInflater.from(_mActivity).inflate(R.layout.recycler_footer, null);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.ISVIP == 1) {
                    ToastUtils.getInstance(_mActivity).showToast("该功能完善中，敬请期待");
                } else {
                    if (builder == null) {
                        builder = new FullVideoPayDialog.Builder(_mActivity);
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
        ptrLayout.setLoadMoreEnable(false);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getActorListData(false);
            }
        });
        adapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

                if (App.ISVIP == 1) {
                    EventBus.getDefault().post(new StartBrotherEvent(ActorVideoListFragment.newInstance(list.get(position).getId(), list.get(position).getActor_name())));
                } else {
                    if (builder == null) {
                        builder = new FullVideoPayDialog.Builder(_mActivity);
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

    }


    /**
     * 获取数据
     */
    private void getActorListData(boolean isShowLoading) {
        if (NetWorkUtils.isNetworkConnected(_mActivity)) {
            if (isShowLoading) {
                statusViewLayout.showLoading();
            }
            OkHttpUtils.get().url(API.URL_PRE + API.ACTOR).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            if (list.size() <= 0) {
                                statusViewLayout.showFailed(onRetryClickListener);
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
                                List<Actor> actorList = JSON.parseArray(s, Actor.class);
                                list.clear();
                                list.addAll(actorList);
                                mAdapter.notifyDataSetChanged();
                                if (list.size() <= 0) {
                                    statusViewLayout.showNone(onRetryClickListener);
                                }
                                if (!statusViewLayout.isContent()) {
                                    statusViewLayout.showContent();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (list.size() <= 0) {
                                    statusViewLayout.showFailed(onRetryClickListener);
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
            if (list.size() <= 0) {
                statusViewLayout.showNetError(onRetryClickListener);
            } else {
                if (!statusViewLayout.isContent()) {
                    statusViewLayout.showContent();
                }
            }
            ptrLayout.refreshComplete();
        }
    }

    /**
     * 重试
     */
    View.OnClickListener onRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActorListData(true);
        }
    };

}
