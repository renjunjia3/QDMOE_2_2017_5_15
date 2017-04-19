package com.hfaufhreu.hjfeuio.ui.fragment.vip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.VideoDetailActivity;
import com.hfaufhreu.hjfeuio.adapter.BlackGlodAdapter;
import com.hfaufhreu.hjfeuio.base.BaseMainFragment;
import com.hfaufhreu.hjfeuio.bean.VideoInfo;
import com.hfaufhreu.hjfeuio.bean.VipInfo;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.NetWorkUtils;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:黑金会员专区
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class BlackGlodVipFragment extends BaseMainFragment {

    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.ultraViewPager)
    UltraViewPager ultraViewPager;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.score)
    MaterialRatingBar score;
    @BindView(R.id.update_number)
    TextView updateNumber;
    @BindView(R.id.update_time)
    TextView updateTime;

    private BlackGlodAdapter adapter;

    private RequestCall requestCall;

    public static BlackGlodVipFragment newInstance() {
        Bundle args = new Bundle();
        BlackGlodVipFragment fragment = new BlackGlodVipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_black_glod_vip, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getBlackGloadVipData();
    }

    private void initView(final List<VideoInfo> list) {
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        adapter = new BlackGlodAdapter(getContext(), list);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMultiScreen(0.8f);
        ultraViewPager.setPageTransformer(false, new UltraScaleTransformer());
        title.setText(list.get(0).getTitle());
        score.setRating(list.get(0).getScore());

        ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(list.get(position).getTitle());
                score.setRating(list.get(position).getScore());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter.setOnItemClickBlackGlodListener(new BlackGlodAdapter.OnItemClickBlackGlodListener() {
            @Override
            public void onItemClickBlackGlod(int position) {
                toVideoDetail(list.get(position));
            }
        });
    }

    private void getBlackGloadVipData() {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            statusViewLayout.showLoading();
            requestCall = OkHttpUtils.get().url(API.URL_PRE + API.VIP_INDEX + "4").build();
            requestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    e.printStackTrace();
                    statusViewLayout.showFailed(retryListener);
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        VipInfo vipInfo = JSON.parseObject(s, VipInfo.class);
                        if (vipInfo.getOther().get(0) != null && vipInfo.getOther().size() > 0
                                && vipInfo.getOther().get(0).getData() != null && vipInfo.getOther().get(0).getData().size() > 0) {
                            initView(vipInfo.getOther().get(0).getData());
                        }
                        statusViewLayout.showContent();
                    } catch (Exception e) {
                        e.printStackTrace();
                        statusViewLayout.showFailed(retryListener);
                    }
                }
            });

        } else {
            statusViewLayout.showNetError(retryListener);
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
        if (requestCall != null) {
            requestCall.cancel();
        }
        super.onDestroyView();
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getBlackGloadVipData();
        }
    };
}
