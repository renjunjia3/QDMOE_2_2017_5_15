package com.hfaufhreu.hjfeuio.ui.fragment.index;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.VideoDetailActivity;
import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.base.BaseFragment;
import com.hfaufhreu.hjfeuio.bean.IndexInfo;
import com.hfaufhreu.hjfeuio.pay.PayUtil;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrClassicFrameLayout;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrDefaultHandler;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrFrameLayout;
import com.hfaufhreu.hjfeuio.ui.dialog.FunctionPayDialog;
import com.hfaufhreu.hjfeuio.ui.fragment.MainFragment;
import com.hfaufhreu.hjfeuio.ui.view.NewBanner;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.GlideImageLoader;
import com.hfaufhreu.hjfeuio.util.NetWorkUtils;
import com.hfaufhreu.hjfeuio.util.ScreenUtils;
import com.hfaufhreu.hjfeuio.util.ToastUtils;
import com.hfaufhreu.hjfeuio.util.ViewUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.VideoInfo;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * 热门推荐
 * Created by scene on 2017/3/13.
 */

public class HotRecommendFragment extends BaseFragment {

    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.banner)
    NewBanner banner;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.free_layout)
    LinearLayout freeLayout;
    @BindView(R.id.free_more)
    TextView freeMore;
    @BindView(R.id.free_image1)
    ImageView freeImage1;
    @BindView(R.id.free_tag1)
    TextView freeTag1;
    @BindView(R.id.free_name1)
    TextView freeName1;
    @BindView(R.id.free_play_time1)
    TextView freePlayTime1;
    @BindView(R.id.free_layout1)
    LinearLayout freeLayout1;
    @BindView(R.id.free_image2)
    ImageView freeImage2;
    @BindView(R.id.free_tag2)
    TextView freeTag2;
    @BindView(R.id.free_name2)
    TextView freeName2;
    @BindView(R.id.free_play_time2)
    TextView freePlayTime2;
    @BindView(R.id.free_layout2)
    LinearLayout freeLayout2;
    @BindView(R.id.free_image3)
    ImageView freeImage3;
    @BindView(R.id.free_tag3)
    TextView freeTag3;
    @BindView(R.id.free_name3)
    TextView freeName3;
    @BindView(R.id.free_play_time3)
    TextView freePlayTime3;
    @BindView(R.id.free_layout3)
    LinearLayout freeLayout3;
    @BindView(R.id.free_image4)
    ImageView freeImage4;
    @BindView(R.id.free_tag4)
    TextView freeTag4;
    @BindView(R.id.free_name4)
    TextView freeName4;
    @BindView(R.id.free_play_time4)
    TextView freePlayTime4;
    @BindView(R.id.free_layout4)
    LinearLayout freeLayout4;
    @BindView(R.id.free_image5)
    ImageView freeImage5;
    @BindView(R.id.free_tag5)
    TextView freeTag5;
    @BindView(R.id.free_name5)
    TextView freeName5;
    @BindView(R.id.free_play_time5)
    TextView freePlayTime5;
    @BindView(R.id.free_layout5)
    LinearLayout freeLayout5;
    @BindView(R.id.free_image6)
    ImageView freeImage6;
    @BindView(R.id.free_tag6)
    TextView freeTag6;
    @BindView(R.id.free_name6)
    TextView freeName6;
    @BindView(R.id.free_play_time6)
    TextView freePlayTime6;
    @BindView(R.id.free_layout6)
    LinearLayout freeLayout6;
    @BindView(R.id.vip_layout)
    LinearLayout vipLayout;
    @BindView(R.id.vip_more)
    TextView vipMore;
    @BindView(R.id.vip_layoutTop)
    RelativeLayout vipLayoutTop;
    @BindView(R.id.vip_imageTop)
    ImageView vipImageTop;
    @BindView(R.id.vip_nameTop)
    TextView vipNameTop;
    @BindView(R.id.vip_image1)
    ImageView vipImage1;
    @BindView(R.id.vip_tag1)
    TextView vipTag1;
    @BindView(R.id.vip_name1)
    TextView vipName1;
    @BindView(R.id.vip_play_time1)
    TextView vipPlayTime1;
    @BindView(R.id.vip_layout1)
    LinearLayout vipLayout1;
    @BindView(R.id.vip_image2)
    ImageView vipImage2;
    @BindView(R.id.vip_tag2)
    TextView vipTag2;
    @BindView(R.id.vip_name2)
    TextView vipName2;
    @BindView(R.id.vip_play_time2)
    TextView vipPlayTime2;
    @BindView(R.id.vip_layout2)
    LinearLayout vipLayout2;
    @BindView(R.id.vip_image3)
    ImageView vipImage3;
    @BindView(R.id.vip_tag3)
    TextView vipTag3;
    @BindView(R.id.vip_name3)
    TextView vipName3;
    @BindView(R.id.vip_play_time3)
    TextView vipPlayTime3;
    @BindView(R.id.vip_layout3)
    LinearLayout vipLayout3;
    @BindView(R.id.vip_image4)
    ImageView vipImage4;
    @BindView(R.id.vip_tag4)
    TextView vipTag4;
    @BindView(R.id.vip_name4)
    TextView vipName4;
    @BindView(R.id.vip_play_time4)
    TextView vipPlayTime4;
    @BindView(R.id.vip_layout4)
    LinearLayout vipLayout4;
    @BindView(R.id.vip_image5)
    ImageView vipImage5;
    @BindView(R.id.vip_tag5)
    TextView vipTag5;
    @BindView(R.id.vip_name5)
    TextView vipName5;
    @BindView(R.id.vip_play_time5)
    TextView vipPlayTime5;
    @BindView(R.id.vip_layout5)
    LinearLayout vipLayout5;
    @BindView(R.id.vip_image6)
    ImageView vipImage6;
    @BindView(R.id.vip_tag6)
    TextView vipTag6;
    @BindView(R.id.vip_name6)
    TextView vipName6;
    @BindView(R.id.vip_play_time6)
    TextView vipPlayTime6;
    @BindView(R.id.vip_layout6)
    LinearLayout vipLayout6;
    @BindView(R.id.recommend_layout)
    LinearLayout recommendLayout;
    @BindView(R.id.recommend_image)
    ImageView recommendImage;
    @BindView(R.id.recommend_name)
    TextView recommendName;
    @BindView(R.id.recommend_synop)
    TextView recommendSynop;
    @BindView(R.id.hot_layout)
    LinearLayout hotLayout;
    @BindView(R.id.hot_image1)
    ImageView hotImage1;
    @BindView(R.id.hot_name1)
    TextView hotName1;
    @BindView(R.id.hot_play_time1)
    TextView hotPlayTime1;
    @BindView(R.id.hot_layout1)
    LinearLayout hotLayout1;
    @BindView(R.id.hot_image2)
    ImageView hotImage2;
    @BindView(R.id.hot_name2)
    TextView hotName2;
    @BindView(R.id.hot_play_time2)
    TextView hotPlayTime2;
    @BindView(R.id.hot_layout2)
    LinearLayout hotLayout2;
    @BindView(R.id.hot_image3)
    ImageView hotImage3;
    @BindView(R.id.hot_name3)
    TextView hotName3;
    @BindView(R.id.hot_play_time3)
    TextView hotPlayTime3;
    @BindView(R.id.hot_layout3)
    LinearLayout hotLayout3;
    @BindView(R.id.hot_image4)
    ImageView hotImage4;
    @BindView(R.id.hot_name4)
    TextView hotName4;
    @BindView(R.id.hot_play_time4)
    TextView hotPlayTime4;
    @BindView(R.id.hot_layout4)
    LinearLayout hotLayout4;
    @BindView(R.id.hot_image5)
    ImageView hotImage5;
    @BindView(R.id.hot_name5)
    TextView hotName5;
    @BindView(R.id.hot_play_time5)
    TextView hotPlayTime5;
    @BindView(R.id.hot_layout5)
    LinearLayout hotLayout5;
    @BindView(R.id.hot_image6)
    ImageView hotImage6;
    @BindView(R.id.hot_name6)
    TextView hotName6;
    @BindView(R.id.hot_play_time6)
    TextView hotPlayTime6;
    @BindView(R.id.hot_layout6)
    LinearLayout hotLayout6;

    private List<IndexInfo> mLists = new ArrayList<>();

    private List<VideoInfo> bannerLists = new ArrayList<>();
    private List<String> bannerImages = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();

    private List<VideoInfo> freeList = new ArrayList<>();
    private List<VideoInfo> recommendList = new ArrayList<>();
    private List<VideoInfo> vipList = new ArrayList<>();
    private List<VideoInfo> hotList = new ArrayList<>();

    private FunctionPayDialog dialog;
    private FunctionPayDialog.Builder builder;

    private ScreenUtils screenUtils;

    private String colors[] = {"#3399FF", "#FF3300", "#00CC66", "#9966FF"};
    private Random random = new Random();

    public static HotRecommendFragment newInstance() {
        Bundle args = new Bundle();
        HotRecommendFragment fragment = new HotRecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index_hot_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListView();
        getIndexData(true);
    }

    private void initListView() {
        screenUtils = ScreenUtils.instance(_mActivity);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.disableWhenHorizontalMove(true);
        //initBanner();
        changeImageHeight();
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getIndexData(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !canChildScrollUp(scrollView);
            }
        });

    }

    /*
    加载banner
     */
    private void initBanner() {
        bannerImages.clear();
        banner.releaseBanner();
        for (VideoInfo info : bannerLists) {
            bannerImages.add(info.getSlider_thumb());
            bannerTitles.add(info.getTitle());
        }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(bannerImages);
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
                Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, bannerLists.get(position));
                _mActivity.startActivity(intent);
            }
        });

        banner.start();
    }


    /**
     * 获取数据
     */
    RequestCall requestCall;

    private void getIndexData(boolean isShowLoading) {
        if (NetWorkUtils.isNetworkConnected(_mActivity)) {
            if (isShowLoading) {
                statusViewLayout.showLoading();
            }

            requestCall = OkHttpUtils.get().url(API.URL_PRE + API.HOT_RECOMMEND).build();
            requestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    //如果index为空或者所有的数据都是0表示界面没有显示
                    if (mLists.size() == 0) {
                        statusViewLayout.showFailed(onClickRetryListener);
                    } else {
                        if (!statusViewLayout.isContent()) {
                            statusViewLayout.showContent();
                        }
                    }
                    ptrLayout.refreshComplete();
                }

                @Override
                public void onResponse(String s, int i) {
                    boolean flag = false;
                    try {
                        mLists.clear();
                        mLists.addAll(JSON.parseArray(s, IndexInfo.class));
                        if (mLists.size() == 0) {
                            statusViewLayout.showNone(onClickRetryListener);
                        } else {
                            for (IndexInfo info : mLists) {
                                switch (info.getType()) {
                                    case 1:
                                        bannerLists.clear();
                                        bannerLists.addAll(info.getData());
                                        break;
                                    case 2:
                                        freeList.clear();
                                        freeList.addAll(info.getData());
                                        break;
                                    case 3:
                                        vipList.clear();
                                        vipList.addAll(info.getData());
                                        break;
                                    case 4:
                                        recommendList.clear();
                                        recommendList.addAll(info.getData());
                                        break;
                                    case 5:
                                        hotList.clear();
                                        hotList.addAll(info.getData());
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (!statusViewLayout.isContent()) {
                                statusViewLayout.showContent();
                            }
                            if (bannerLists.size() > 0) {
                                flag = true;
                                banner.setVisibility(View.VISIBLE);
                                initBanner();
                            } else {
                                banner.setVisibility(View.GONE);
                            }
                            if (freeList.size() > 0) {
                                flag = true;
                                freeLayout.setVisibility(View.VISIBLE);
                                bindTypeFreeData();
                            } else {
                                freeLayout.setVisibility(View.GONE);
                            }
                            if (vipList.size() > 0) {
                                flag = true;
                                vipLayout.setVisibility(View.VISIBLE);
                                bindVIPData();
                            } else {
                                vipLayout.setVisibility(View.GONE);
                            }
                            if (recommendList.size() > 0) {
                                flag = true;
                                recommendLayout.setVisibility(View.VISIBLE);
                                bindVIPRecommendData();
                            } else {
                                recommendLayout.setVisibility(View.GONE);
                            }
                            if (hotList.size() > 0) {
                                flag = true;
                                hotLayout.setVisibility(View.VISIBLE);
                                bindVIPHotData();
                            } else {
                                hotLayout.setVisibility(View.GONE);
                            }
                            if (!flag) {
                                statusViewLayout.showNone(onClickRetryListener);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mLists.size() == 0) {
                            statusViewLayout.showFailed(onClickRetryListener);
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
            //如果index为空或者所有的数据都是0表示界面没有显示
            if (mLists.size() == 0) {
                statusViewLayout.showNetError(onClickRetryListener);
            } else {
                if (!statusViewLayout.isContent()) {
                    statusViewLayout.showContent();
                }
            }
            ptrLayout.refreshComplete();
        }
    }

    /**
     * 统一修改图片的高度
     */
    private void changeImageHeight() {
        int freeImageHeight = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(40f)) * 205f / 3f / 145f);
        ViewUtils.setViewHeightByViewGroup(freeImage1, freeImageHeight);
        ViewUtils.setViewHeightByViewGroup(freeImage2, freeImageHeight);
        ViewUtils.setViewHeightByViewGroup(freeImage3, freeImageHeight);
        ViewUtils.setViewHeightByViewGroup(freeImage4, freeImageHeight);
        ViewUtils.setViewHeightByViewGroup(freeImage5, freeImageHeight);
        ViewUtils.setViewHeightByViewGroup(freeImage6, freeImageHeight);

        int vipTopImageHeight = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(20f)) * 350f / 530f);
        ViewUtils.setViewHeightByViewGroup(vipImageTop, vipTopImageHeight);
        int vipImageHeight = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(40f)) * 205f / 3f / 145f);
        ViewUtils.setViewHeightByViewGroup(vipImage1, vipImageHeight);
        ViewUtils.setViewHeightByViewGroup(vipImage2, vipImageHeight);
        ViewUtils.setViewHeightByViewGroup(vipImage3, vipImageHeight);
        ViewUtils.setViewHeightByViewGroup(vipImage4, vipImageHeight);
        ViewUtils.setViewHeightByViewGroup(vipImage5, vipImageHeight);
        ViewUtils.setViewHeightByViewGroup(vipImage6, vipImageHeight);

        int vipRecommendImageHeight = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(20f)) * 400f / 525f);
        ViewUtils.setViewHeightByViewGroup(recommendImage, vipRecommendImageHeight);

        int vipHotImageHeight = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(30f)) * 200f / 260f / 2f);
        ViewUtils.setViewHeightByViewGroup(hotImage1, vipHotImageHeight);
        ViewUtils.setViewHeightByViewGroup(hotImage2, vipHotImageHeight);
        ViewUtils.setViewHeightByViewGroup(hotImage3, vipHotImageHeight);
        ViewUtils.setViewHeightByViewGroup(hotImage4, vipHotImageHeight);
        ViewUtils.setViewHeightByViewGroup(hotImage5, vipHotImageHeight);
        ViewUtils.setViewHeightByViewGroup(hotImage6, vipHotImageHeight);
    }


    /**
     * 绑定免费观看的数据
     */
    private void bindTypeFreeData() {
        try {
            int freeListSize = freeList.size();
            freeLayout1.setVisibility(freeListSize > 0 ? View.VISIBLE : View.GONE);
            freeLayout2.setVisibility(freeListSize > 1 ? View.VISIBLE : View.GONE);
            freeLayout3.setVisibility(freeListSize > 2 ? View.VISIBLE : View.GONE);
            freeLayout4.setVisibility(freeListSize > 3 ? View.VISIBLE : View.GONE);
            freeLayout5.setVisibility(freeListSize > 4 ? View.VISIBLE : View.GONE);
            freeLayout6.setVisibility(freeListSize > 5 ? View.VISIBLE : View.GONE);
            Glide.with(this).load(freeList.get(0).getThumb()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(freeImage1);
            freeName1.setText(freeList.get(0).getTitle());
            freePlayTime1.setText(freeList.get(0).getHits() + "次播放");
            freeTag1.setText(freeList.get(0).getAttr());
            freeTag1.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(freeList.get(1).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(freeImage2);
            freeName2.setText(freeList.get(1).getTitle());
            freePlayTime2.setText(freeList.get(1).getHits() + "次播放");
            freeTag2.setText(freeList.get(1).getAttr());
            freeTag2.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(freeList.get(2).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(freeImage3);
            freeName3.setText(freeList.get(2).getTitle());
            freePlayTime3.setText(freeList.get(2).getHits() + "次播放");
            freeTag3.setText(freeList.get(2).getAttr());
            freeTag3.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(freeList.get(3).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(freeImage4);
            freeName4.setText(freeList.get(3).getTitle());
            freePlayTime4.setText(freeList.get(3).getHits() + "次播放");
            freeTag4.setText(freeList.get(3).getAttr());
            freeTag4.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(freeList.get(4).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(freeImage5);
            freeName5.setText(freeList.get(4).getTitle());
            freePlayTime5.setText(freeList.get(4).getHits() + "次播放");
            freeTag5.setText(freeList.get(4).getAttr());
            freeTag5.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(freeList.get(5).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(freeImage6);
            freeName6.setText(freeList.get(5).getTitle());
            freePlayTime6.setText(freeList.get(5).getHits() + "次播放");
            freeTag6.setText(freeList.get(5).getAttr());
            freeTag6.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 绑定VIP最新上线的数据
     */
    private void bindVIPData() {
        try {
            int vipListSize = vipList.size();
            vipLayoutTop.setVisibility(vipListSize > 0 ? View.VISIBLE : View.GONE);
            vipLayout1.setVisibility(vipListSize > 1 ? View.VISIBLE : View.GONE);
            vipLayout2.setVisibility(vipListSize > 2 ? View.VISIBLE : View.GONE);
            vipLayout3.setVisibility(vipListSize > 3 ? View.VISIBLE : View.GONE);
            vipLayout4.setVisibility(vipListSize > 4 ? View.VISIBLE : View.GONE);
            vipLayout5.setVisibility(vipListSize > 5 ? View.VISIBLE : View.GONE);
            vipLayout6.setVisibility(vipListSize > 6 ? View.VISIBLE : View.GONE);

            Glide.with(this).load(vipList.get(0).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(vipImageTop);
            vipNameTop.setText(vipList.get(0).getTitle());

            Glide.with(this).load(vipList.get(1).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(vipImage1);
            vipName1.setText(vipList.get(1).getTitle());
            vipPlayTime1.setText(vipList.get(1).getHits() + "次播放");
            vipTag1.setText(vipList.get(1).getAttr());
            vipTag1.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(vipList.get(2).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(vipImage2);
            vipName2.setText(vipList.get(2).getTitle());
            vipPlayTime2.setText(vipList.get(2).getHits() + "次播放");
            vipTag2.setText(vipList.get(2).getAttr());
            vipTag2.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(vipList.get(3).getThumb()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(vipImage3);
            vipName3.setText(vipList.get(3).getTitle());
            vipPlayTime3.setText(vipList.get(3).getHits() + "次播放");
            vipTag3.setText(vipList.get(3).getAttr());
            vipTag3.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(vipList.get(4).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(vipImage4);
            vipName4.setText(vipList.get(4).getTitle());
            vipPlayTime4.setText(vipList.get(4).getHits() + "次播放");
            vipTag4.setText(vipList.get(4).getAttr());
            vipTag4.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(vipList.get(5).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(vipImage5);
            vipName5.setText(vipList.get(5).getTitle());
            vipPlayTime5.setText(vipList.get(5).getHits() + "次播放");
            vipTag5.setText(vipList.get(5).getAttr());
            vipTag5.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));

            Glide.with(this).load(vipList.get(6).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(vipImage6);
            vipName6.setText(vipList.get(6).getTitle());
            vipPlayTime6.setText(vipList.get(6).getHits() + "次播放");
            vipTag6.setText(vipList.get(6).getAttr());
            vipTag6.setBackgroundColor(Color.parseColor(colors[random.nextInt(4)]));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 绑定VIP每周推荐的数据
     */
    private void bindVIPRecommendData() {
        try {
            Glide.with(this).load(recommendList.get(0).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(recommendImage);
            recommendName.setText(recommendList.get(0).getTitle());
            recommendSynop.setText(recommendList.get(0).getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定VIP热播的数据
     */
    private void bindVIPHotData() {
        try {
            int hotListSize = hotList.size();
            hotLayout1.setVisibility(hotListSize > 0 ? View.VISIBLE : View.GONE);
            hotLayout2.setVisibility(hotListSize > 1 ? View.VISIBLE : View.GONE);
            hotLayout3.setVisibility(hotListSize > 2 ? View.VISIBLE : View.GONE);
            hotLayout4.setVisibility(hotListSize > 3 ? View.VISIBLE : View.GONE);
            hotLayout5.setVisibility(hotListSize > 4 ? View.VISIBLE : View.GONE);
            hotLayout6.setVisibility(hotListSize > 5 ? View.VISIBLE : View.GONE);

            Glide.with(this).load(hotList.get(0).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(hotImage1);
            hotName1.setText(hotList.get(0).getTitle());
            hotPlayTime1.setText(hotList.get(0).getHits() + "次播放");

            Glide.with(this).load(hotList.get(1).getThumb()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(hotImage2);
            hotName2.setText(hotList.get(1).getTitle());
            hotPlayTime2.setText(hotList.get(1).getHits() + "次播放");

            Glide.with(this).load(hotList.get(2).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(hotImage3);
            hotName3.setText(hotList.get(2).getTitle());
            hotPlayTime3.setText(hotList.get(2).getHits() + "次播放");

            Glide.with(this).load(hotList.get(3).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(hotImage4);
            hotName4.setText(hotList.get(3).getTitle());
            hotPlayTime4.setText(hotList.get(3).getHits() + "次播放");

            Glide.with(this).load(hotList.get(4).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(hotImage5);
            hotName5.setText(hotList.get(4).getTitle());
            hotPlayTime5.setText(hotList.get(4).getHits() + "次播放");

            Glide.with(this).load(hotList.get(5).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(hotImage6);
            hotName6.setText(hotList.get(5).getTitle());
            hotPlayTime6.setText(hotList.get(5).getHits() + "次播放");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        if (requestCall != null) {
            requestCall.cancel();
        }
        super.onDestroyView();
    }

    View.OnClickListener onClickRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getIndexData(true);
        }
    };


    /**
     * 点击的免费观看部分
     *
     * @param v
     */
    @OnClick({R.id.free_layout1, R.id.free_layout2, R.id.free_layout3, R.id.free_layout4, R.id.free_layout5, R.id.free_layout6})
    public void onClickFreeVideo(View v) {
        Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
        switch (v.getId()) {
            case R.id.free_layout1:
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, freeList.get(0));
                break;
            case R.id.free_layout2:
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, freeList.get(1));
                break;
            case R.id.free_layout3:
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, freeList.get(2));
                break;
            case R.id.free_layout4:
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, freeList.get(3));
                break;
            case R.id.free_layout5:
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, freeList.get(4));
                break;
            case R.id.free_layout6:
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, freeList.get(5));
                break;
            default:
                break;
        }
        _mActivity.startActivity(intent);
    }

    /**
     * vip最新上线
     *
     * @param v
     */
    @OnClick({R.id.vip_layoutTop, R.id.vip_layout1, R.id.vip_layout2, R.id.vip_layout3, R.id.vip_layout4, R.id.vip_layout5, R.id.vip_layout6})
    public void onClickVipVideo(View v) {
        try {
            Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
            switch (v.getId()) {
                case R.id.vip_layoutTop:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, vipList.get(0));
                    break;
                case R.id.vip_layout1:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, vipList.get(1));
                    break;
                case R.id.vip_layout2:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, vipList.get(2));
                    break;
                case R.id.vip_layout3:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, vipList.get(3));
                    break;
                case R.id.vip_layout4:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, vipList.get(4));
                    break;
                case R.id.vip_layout5:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, vipList.get(5));
                    break;
                case R.id.vip_layout6:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, vipList.get(6));
                    break;
                default:
                    break;
            }
            _mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * vip每周推荐
     */
    @OnClick(R.id.recommend_layout)
    public void onClickRecommend() {
        try {
            Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
            intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, recommendList.get(0));
            _mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击Vip热播
     */
    @OnClick({R.id.hot_layout1, R.id.hot_layout2, R.id.hot_layout3, R.id.hot_layout4, R.id.hot_layout5, R.id.hot_layout6})
    public void onClickHot(View v) {
        try {
            Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
            switch (v.getId()) {
                case R.id.hot_layout1:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, hotList.get(0));
                    break;
                case R.id.hot_layout2:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, hotList.get(1));
                    break;
                case R.id.hot_layout3:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, hotList.get(2));
                    break;
                case R.id.hot_layout4:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, hotList.get(3));
                    break;
                case R.id.hot_layout5:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, hotList.get(4));
                    break;
                case R.id.hot_layout6:
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, hotList.get(5));
                    break;


            }
            _mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击更多
     */
    @OnClick({R.id.free_more, R.id.vip_more, R.id.hot_more, R.id.footerImage})
    public void onClickMore() {
        if (App.ISVIP == 1) {
            ToastUtils.getInstance(_mActivity).showToast("该功能完善中，敬请期待");
        } else {
            if (builder == null) {
                builder = new FunctionPayDialog.Builder(_mActivity);
                builder.setAliPayClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PayUtil.getInstance().payByAliPay(_mActivity, 1, 0);
                    }
                });
                builder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PayUtil.getInstance().payByWeChat(_mActivity, 1, 0);
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

}
