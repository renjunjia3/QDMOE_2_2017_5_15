package com.ofgvyiss.ofgvyi.ui.fragment.film;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ofgvyiss.ofgvyi.R;
import com.ofgvyiss.ofgvyi.VideoDetailActivity;
import com.ofgvyiss.ofgvyi.adapter.FlimDetailAdapter;
import com.ofgvyiss.ofgvyi.app.App;
import com.ofgvyiss.ofgvyi.base.BaseBackFragment;
import com.ofgvyiss.ofgvyi.bean.VideoInfo;
import com.ofgvyiss.ofgvyi.itemdecoration.CustomItemDecotation;
import com.ofgvyiss.ofgvyi.pull_loadmore.PtrClassicFrameLayout;
import com.ofgvyiss.ofgvyi.pull_loadmore.PtrDefaultHandler;
import com.ofgvyiss.ofgvyi.pull_loadmore.PtrFrameLayout;
import com.ofgvyiss.ofgvyi.util.API;
import com.ofgvyiss.ofgvyi.util.DialogUtil;
import com.ofgvyiss.ofgvyi.util.NetWorkUtils;
import com.ofgvyiss.ofgvyi.util.ScreenUtils;
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
 * Case By:
 * package:com.hfaufhreu.hjfeuio.ui.fragment.film
 * Author：scene on 2017/4/19 17:36
 */

public class FlimDetailFragment extends BaseBackFragment {
    private static final String ARG_FLIM_ID = "arg_flim_id";
    private static final String ARG_FLIM_name = "arg_flim_name";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private int flimId = 0;
    private String flimName = "";

    private List<VideoInfo> list;
    private FlimDetailAdapter adapter;

    private RequestCall requestCall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            flimId = args.getInt(ARG_FLIM_ID);
            flimName = args.getString(ARG_FLIM_name);
        }
    }

    public static FlimDetailFragment newInstance(int filmId, String filmName) {
        FlimDetailFragment fragment = new FlimDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLIM_ID, filmId);
        args.putString(ARG_FLIM_name, filmName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_detail, container, false);
        ButterKnife.bind(this, view);
        toolbarTitle.setText(flimName);
        initToolbarNav(toolbar);
        return view;
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
        getData(true);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        list = new ArrayList<>();
        adapter = new FlimDetailAdapter(getContext(), list);
        int space = (int) ScreenUtils.instance(getContext()).dip2px(3);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new CustomItemDecotation(space, space, 2, true));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickItemVideoListener(new FlimDetailAdapter.OnClickItemVideoListener() {
            @Override
            public void onClickItemVideo(int positon) {
                if (App.isVip > 0) {
                    toVideoDetail(list.get(positon));
                } else {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该片为会员视频，请开通会员后观看", App.isVip, false, true, list.get(positon).getVideo_id());
                }
            }
        });
    }

    private void getData(final boolean isShowLoad) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoad) {
                statusViewLayout.showLoading();
            }
            requestCall = OkHttpUtils.get().url(API.URL_PRE + API.CATE_VIDEOS + flimId).build();
            requestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    e.printStackTrace();
                    if (isShowLoad) {
                        statusViewLayout.showFailed(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        list.clear();
                        list.addAll(JSON.parseArray(s, VideoInfo.class));
                        adapter.notifyDataSetChanged();
                        if (isShowLoad) {
                            statusViewLayout.showContent();
                        } else {
                            ptrLayout.refreshComplete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isShowLoad) {
                            statusViewLayout.showFailed(retryListener);
                        } else {
                            ptrLayout.refreshComplete();
                        }
                    }
                }
            });
        } else {
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
        if (requestCall != null)
            requestCall.cancel();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9999 && resultCode == RESULT_OK) {
            _mActivity.onBackPressed();
        }
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getData(true);
        }
    };
}
