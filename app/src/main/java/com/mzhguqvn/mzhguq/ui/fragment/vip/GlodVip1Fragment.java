package com.mzhguqvn.mzhguq.ui.fragment.vip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.VideoDetailActivity;
import com.mzhguqvn.mzhguq.adapter.TrySeeAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.TrySeeContentInfo;
import com.mzhguqvn.mzhguq.bean.VideoInfo;
import com.mzhguqvn.mzhguq.bean.VipInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DialogUtil;
import com.mzhguqvn.mzhguq.util.GlideImageLoader;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.mzhguqvn.mzhguq.util.SharedPreferencesUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:会员区
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/18 10:21
 */

public class GlodVip1Fragment extends BaseMainFragment {
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

    public static GlodVip1Fragment newInstance() {
        Bundle args = new Bundle();
        GlodVip1Fragment fragment = new GlodVip1Fragment();
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
        addFooterView();
        initView();
        getTrySeeData(true);
        MainActivity.upLoadPageInfo(PageConfig.GLOD_POSITOTN_ID, 0, 0);
    }

    private void addFooterView() {
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_vip_footer, null);
        TextView footerText = (TextView) footerView.findViewById(R.id.footer_text);
        if (App.role == 1 || App.role == 2) {
            footerText.setVisibility(View.VISIBLE);
            footerText.setBackgroundResource(R.drawable.ic_list_bottom_update_diamond);
        } else {
            footerText.setVisibility(View.GONE);
        }
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.role == 1 || App.role == 2) {
                    DialogUtil.getInstance().showDiamondVipDialog(getContext(), 0, PageConfig.GLOD_POSITOTN_ID);
                }
            }
        });
        listView.addFooterView(footerView);
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
        if (banner == null) {
            banner = (Banner) bannerView.findViewById(R.id.banner);
        }
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
            HashMap<String, String> params = API.createParams();
            params.put("position_id", "2");
            getDataCall = OkHttpUtils.get().url(API.URL_PRE + API.VIP_INDEX).params(params).build();
            getDataCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    try {
                        if (isShowLoad) {
                            if (statusViewLayout != null) {
                                statusViewLayout.showFailed(retryListener);
                            }
                        } else {
                            ptrLayout.refreshComplete();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
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
                        try {
                            if (isShowLoad) {
                                if (statusViewLayout != null) {
                                    statusViewLayout.showFailed(retryListener);
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    } finally {
                        try {
                            if (ptrLayout != null)
                                ptrLayout.refreshComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_FROM, PageConfig.GLOD_POSITOTN_ID);
        _mActivity.startActivityForResult(intent, 9999);
    }


    @Override
    public void onDestroyView() {
        if (getDataCall != null) {
            getDataCall.cancel();
        }
        listView.setAdapter(null);
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
