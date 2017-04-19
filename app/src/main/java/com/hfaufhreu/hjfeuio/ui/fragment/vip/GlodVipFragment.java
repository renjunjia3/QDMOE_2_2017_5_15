package com.hfaufhreu.hjfeuio.ui.fragment.vip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.VideoDetailActivity;
import com.hfaufhreu.hjfeuio.adapter.GlodVipAdpter;
import com.hfaufhreu.hjfeuio.base.BaseMainFragment;
import com.hfaufhreu.hjfeuio.bean.TrySeeContentInfo;
import com.hfaufhreu.hjfeuio.bean.VideoInfo;
import com.hfaufhreu.hjfeuio.bean.VipInfo;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrClassicFrameLayout;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrDefaultHandler;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrFrameLayout;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.NetWorkUtils;
import com.hfaufhreu.hjfeuio.util.ScreenUtils;
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
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private GlodVipAdpter adapter;
    private List<TrySeeContentInfo> list;

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
        list=new ArrayList<>();
        adapter = new GlodVipAdpter(getContext(), list);
        listview.setAdapter(adapter);
        adapter.setOnGlodVipItemClickListener(new GlodVipAdpter.OnGlodVipItemClickListener() {
            @Override
            public void onGlodVipItemClick(int position, int childPosition) {
                toVideoDetail(list.get(position).getData().get(childPosition));
            }
        });
    }

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
            ViewGroup.LayoutParams layoutparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutparams.height=(int) (ScreenUtils.instance(getContext()).getScreenWidth() * 9f / 16f);
            headerView.setLayoutParams(layoutparams);
        }
        if (listview.getHeaderViewsCount() > 0) {
            listview.removeHeaderView(headerView);
        }
        listview.addHeaderView(headerView);
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
                public void onResponse(String s, int i) {
                    try {
                        VipInfo vipInfo = JSON.parseObject(s, VipInfo.class);
                        initHeaderView(vipInfo.getBanner());
                        list.clear();
                        list.addAll(vipInfo.getOther());
                        adapter.notifyDataSetChanged();
                        if (isShowLoad) {
                            statusViewLayout.showContent();
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
        _mActivity.startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        if(getDataCall!=null){
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
