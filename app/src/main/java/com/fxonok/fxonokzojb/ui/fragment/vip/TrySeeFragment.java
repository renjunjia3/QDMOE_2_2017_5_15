package com.fxonok.fxonokzojb.ui.fragment.vip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.fxonok.fxonokzojb.R;
import com.fxonok.fxonokzojb.VideoDetailActivity;
import com.fxonok.fxonokzojb.adapter.TrySeeAdapter;
import com.fxonok.fxonokzojb.app.App;
import com.fxonok.fxonokzojb.base.BaseMainFragment;
import com.fxonok.fxonokzojb.bean.TrySeeContentInfo;
import com.fxonok.fxonokzojb.bean.VideoInfo;
import com.fxonok.fxonokzojb.bean.VipInfo;
import com.fxonok.fxonokzojb.pull_loadmore.PtrClassicFrameLayout;
import com.fxonok.fxonokzojb.pull_loadmore.PtrDefaultHandler;
import com.fxonok.fxonokzojb.pull_loadmore.PtrFrameLayout;
import com.fxonok.fxonokzojb.util.API;
import com.fxonok.fxonokzojb.util.GlideImageLoader;
import com.fxonok.fxonokzojb.util.NetWorkUtils;
import com.fxonok.fxonokzojb.util.SharedPreferencesUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:试看专区
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/18 10:21
 */

public class TrySeeFragment extends BaseMainFragment {
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.listview)
    ListView listView;

    //banner
    private Banner banner;
    private List<String> bannerImageUrls = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();

    //接口回调
    private RequestCall getDataCall;

    //adapter
    private TrySeeAdapter adapter;
    private List<TrySeeContentInfo> lists;

    //banner
    private View bannerView;

    public static TrySeeFragment newInstance() {
        Bundle args = new Bundle();
        TrySeeFragment fragment = new TrySeeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_try_see, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        statusViewLayout.showContent();
        initView();
        getTrySeeData(true);
        uploadCurrentPage();
    }

    /**
     * Case By:上报当前页面
     * Author: scene on 2017/4/27 17:05
     */
    private void uploadCurrentPage() {
        Map<String, String> params = new HashMap<>();
        params.put("position_id", "1");
        params.put("user_id", App.USER_ID + "");
        OkHttpUtils.post().url(API.URL_PRE + API.UPLOAD_CURRENT_PAGE).params(params).build().execute(null);
    }


    private void initView() {
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getTrySeeData(false);
            }
        });
        bannerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_banner, null);
        lists = new ArrayList<>();
        adapter = new TrySeeAdapter(getContext(), lists);
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrySeeAdapter.OnItemClickListener() {
            @Override
            public void onTrySeeItemClick(int position, int childPosition) {
                toVideoDetail(lists.get(position).getData().get(childPosition));
            }
        });
    }

    /**
     * Case By:加载banner
     * Author: scene on 2017/4/18 18:09
     * 需要在数据获取到之后再调用 不然会有异常 但是不会崩溃
     */
    private void initBanner(final List<VideoInfo> bannerList) {
        if (bannerList == null || bannerList.size() == 0) {
            return;
        }
        bannerImageUrls.clear();
        bannerTitles.clear();
        for (VideoInfo info : bannerList) {
            bannerImageUrls.add(info.getThumb());
            bannerTitles.add(info.getTitle());
        }
        if (listView.getHeaderViewsCount() > 0) {
            listView.removeHeaderView(bannerView);
        }
        listView.addHeaderView(bannerView);
        banner = (Banner) bannerView.findViewById(R.id.banner);
        banner.releaseBanner();
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(bannerImageUrls);
        //设置标题集合
        banner.setBannerTitles(bannerTitles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置监听
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                toVideoDetail(bannerList.get(position));
            }
        });
        banner.start();
    }

    /**
     * Case By:
     * Author: scene on 2017/4/18 18:10
     *
     * @param isShowLoad 是否需要显示loading页
     */

    private void getTrySeeData(final boolean isShowLoad) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoad) {
                statusViewLayout.showLoading();
            }
            getDataCall = OkHttpUtils.get().url(API.URL_PRE + API.VIP_INDEX + 1).build();
            getDataCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (isShowLoad) {
                        if (statusViewLayout != null) {
                            statusViewLayout.showFailed(retryListener);
                        }
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        VipInfo vipInfo = JSON.parseObject(s, VipInfo.class);
                        initBanner(vipInfo.getBanner());
                        lists.clear();
                        lists.addAll(vipInfo.getOther());
                        adapter.notifyDataSetChanged();
                        SharedPreferencesUtil.putString(getContext(), "NOTIFY_DATA", s);
                        if (isShowLoad) {
                            if (statusViewLayout != null) {
                                statusViewLayout.showContent();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isShowLoad) {
                            if (statusViewLayout != null) {
                                statusViewLayout.showFailed(retryListener);
                            }
                        }
                    } finally {
                        ptrLayout.refreshComplete();
                    }
                }
            });

        } else {
            //网络未连接
            if (isShowLoad) {
                if (statusViewLayout != null) {
                    statusViewLayout.showNetError(retryListener);
                }
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
        intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_FROM_TRY_SEE, true);
        _mActivity.startActivityForResult(intent, 9999);
    }


    @Override
    public void onDestroyView() {
        if (getDataCall != null) {
            getDataCall.cancel();
        }
        super.onDestroyView();
    }

    /**
     * Case By:重试的监听
     * Author: scene on 2017/4/18 18:12
     */
    View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getTrySeeData(true);
        }
    };
}
