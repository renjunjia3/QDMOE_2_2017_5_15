package com.hfaufhreu.hjfeuio.ui.fragment.vip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.VideoDetailActivity;
import com.hfaufhreu.hjfeuio.adapter.DiamondVipAdpter;
import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.base.BaseMainFragment;
import com.hfaufhreu.hjfeuio.bean.TrySeeContentInfo;
import com.hfaufhreu.hjfeuio.bean.VideoInfo;
import com.hfaufhreu.hjfeuio.bean.VipInfo;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrClassicFrameLayout;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrDefaultHandler;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrFrameLayout;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.DialogUtil;
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
 * Case By:钻石会员专区
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class DiamondVipFragment extends BaseMainFragment {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private DiamondVipAdpter adapter;
    private List<TrySeeContentInfo> list;

    private RequestCall getDataCall;

    private ImageView headerView;

    public static DiamondVipFragment newInstance() {
        Bundle args = new Bundle();
        DiamondVipFragment fragment = new DiamondVipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diamond_vip, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        getDiamondVipData(true);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getDiamondVipData(false);
            }
        });
        list = new ArrayList<>();
        adapter = new DiamondVipAdpter(getContext(), list);
        listview.setAdapter(adapter);
        adapter.setOnDiamondVipItemClickListener(new DiamondVipAdpter.OnDiamondVipItemClickListener() {
            @Override
            public void onDiamondVipItemClick(int position, int childPosition) {
                toVideoDetail(list.get(position).getData().get(childPosition));
            }

            @Override
            public void onDiamondVipMoreClick() {
                //弹出开通VIP
            }
        });


        if (App.isVip < 2) {
            View footerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_vip_footer, null);
            //设置footerView的事件
            footerView.findViewById(R.id.page1).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page2).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page3).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page4).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page5).setOnClickListener(pageClickListener);
            footerView.findViewById(R.id.page_next).setOnClickListener(pageClickListener);
            listview.addFooterView(footerView);
        }
    }

    private View.OnClickListener pageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (App.isVip < 2) {
                DialogUtil.getInstance().showDiamondVipDialog(getContext(), 0, false);
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
    private void getDiamondVipData(final boolean isShowLoad) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoad) {
                statusViewLayout.showLoading();
            }
            getDataCall = OkHttpUtils.get().url(API.URL_PRE + API.VIP_INDEX + 3).build();
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
            getDiamondVipData(true);
        }
    };
}
