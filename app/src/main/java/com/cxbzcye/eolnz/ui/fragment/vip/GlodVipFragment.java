package com.cxbzcye.eolnz.ui.fragment.vip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cxbzcye.eolnz.R;
import com.cxbzcye.eolnz.VideoDetailActivity;
import com.cxbzcye.eolnz.adapter.GlodVipAdapter;
import com.cxbzcye.eolnz.app.App;
import com.cxbzcye.eolnz.base.BaseMainFragment;
import com.cxbzcye.eolnz.bean.VideoInfo;
import com.cxbzcye.eolnz.bean.VipInfo;
import com.cxbzcye.eolnz.itemdecoration.DiamondItemDecoration;
import com.cxbzcye.eolnz.pull_loadmore.PtrClassicFrameLayout;
import com.cxbzcye.eolnz.pull_loadmore.PtrDefaultHandler;
import com.cxbzcye.eolnz.pull_loadmore.PtrFrameLayout;
import com.cxbzcye.eolnz.pull_loadmore.recyclerview.RecyclerAdapterWithHF;
import com.cxbzcye.eolnz.util.API;
import com.cxbzcye.eolnz.util.DialogUtil;
import com.cxbzcye.eolnz.util.NetWorkUtils;
import com.cxbzcye.eolnz.util.ScreenUtils;
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
 * Case By:黄金会员专区
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class GlodVipFragment extends BaseMainFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private GlodVipAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private List<VideoInfo> list;

    private RequestCall getDataCall;

    private ImageView headerView;


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
        initView();
        getGlodVipData(true);
    }


    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getGlodVipData(false);
            }
        });

        list = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (App.isVip < 1) {
                    if (position == 0) {
                        return 2;
                    } else if (position > list.size()) {
                        return 2;
                    } else {
                        return list.get(position - 1).isTilteType() ? 2 : 1;
                    }
                } else {
                    if (position == 0) {
                        return 2;
                    } else {
                        return list.get(position - 1).isTilteType() ? 2 : 1;
                    }
                }

            }
        });
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GlodVipAdapter(getContext(), list);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        if (App.isVip < 1) {
            View footerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_vip_footer, null);
            //设置footerView的事件
            footerView.findViewById(R.id.page1).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page2).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page3).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page4).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page5).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page_next).setOnClickListener(pageClickListener);
            mAdapter.addFooter(footerView);
        }
        recyclerView.addItemDecoration(new DiamondItemDecoration((int) ScreenUtils.instance(getContext()).dip2px(3), list, App.isVip < 1));
        recyclerView.setAdapter(mAdapter);
        adapter.setOnClickGlodVipItemListener(new GlodVipAdapter.OnClickGlodVipItemListener() {
            @Override
            public void onClickGlodVipItem(int position) {
                toVideoDetail(list.get(position));
            }
        });

    }

    private View.OnClickListener pageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (App.isVip < 1) {
                DialogUtil.getInstance().showGoldVipDialog(getContext(), 0, false);
            }
        }
    };

    /**
     * Case By:初始化Header
     * Author: scene on 2017/4/19 11:26
     */
    private void initHeaderView(final List<VideoInfo> headerLists) {
        if (headerLists.get(0) == null) {
            return;
        }
        final VideoInfo info = headerLists.get(0);
        if (headerView == null) {
            headerView = new ImageView(getContext());
            AbsListView.LayoutParams layoutparams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            layoutparams.height = (int) (ScreenUtils.instance(getContext()).getScreenWidth() * 9f / 16f);
            headerView.setLayoutParams(layoutparams);
        }
        if (mAdapter.getHeadSize() > 0) {
            mAdapter.removeHeader(headerView);
        }
        mAdapter.addHeader(headerView);
        Glide.with(getContext()).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(headerView);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toVideoDetail(info);
            }
        });
    }

    /**
     * Case By:获取数据
     * Author: scene on 2017/4/19 11:23
     *
     * @param isShowLoad 是否显示加载中
     */
    private void getGlodVipData(final boolean isShowLoad) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoad) {
                statusViewLayout.showLoading();
            }
            getDataCall = OkHttpUtils.get().url(API.URL_PRE + API.VIP_INDEX + 2).build();
            getDataCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (isShowLoad) {
                        statusViewLayout.showFailed(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int code) {
                    try {
                        VipInfo vipInfo = JSON.parseObject(s, VipInfo.class);
                        initHeaderView(vipInfo.getBanner());
                        list.clear();
                        list.clear();
                        for (int i = 0; i < vipInfo.getOther().size(); i++) {
                            VideoInfo videoInfo = new VideoInfo();
                            videoInfo.setTilteType(true);
                            videoInfo.setTitle(vipInfo.getOther().get(i).getTitle());
                            list.add(videoInfo);
                            for (int j = 0; j < vipInfo.getOther().get(i).getData().size(); j++) {
                                VideoInfo videoInfo2 = vipInfo.getOther().get(i).getData().get(j);
                                videoInfo2.setTilteType(false);
                                list.add(videoInfo2);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (isShowLoad) {
                            if (statusViewLayout != null) {
                                statusViewLayout.showContent();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isShowLoad) {
                            statusViewLayout.showFailed(retryListener);
                        }
                    } finally {
                        ptrLayout.refreshComplete();
                    }
                }
            });

        } else {
            //网络未连接
            if (isShowLoad) {
                statusViewLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
        }
    }


    /**
     * Case By:跳转到视频详情页
     * Author: scene on 2017/4/19 9:33
     *
     * @param videoInfo 视频信息
     */
    private void toVideoDetail(VideoInfo videoInfo) {
        Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
        intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, videoInfo);
        intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_FROM_TRY_SEE, false);
        _mActivity.startActivityForResult(intent, 9999);
    }

    @Override
    public void onDestroyView() {
        if (getDataCall != null) {
            getDataCall.cancel();
        }
        super.onDestroyView();
    }

    //重试监听
    View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getGlodVipData(true);
        }
    };

}
