package com.mzhguqvn.mzhguq.ui.fragment.anchor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.VideoDetailActivity;
import com.mzhguqvn.mzhguq.adapter.AnchorAdapter;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.VideoInfo;
import com.mzhguqvn.mzhguq.bean.VipInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.itemdecoration.AnchorItemDecoration;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.recyclerview.RecyclerAdapterWithHF;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.mzhguqvn.mzhguq.util.ScreenUtils;
import com.mzhguqvn.mzhguq.util.SharedPreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:主播
 * package:com.mzhguqvn.mzhguq.ui.fragment.anchor
 * Author：scene on 2017/5/19 10:10
 */

public class AnchorFragment extends BaseMainFragment {
    private static final String TAG = "AnchorFragment_DATA";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private AnchorAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private List<VideoInfo> list;
    private ProgressDialog progressDialog;

    public static AnchorFragment newInstance() {
        return new AnchorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anchor, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.ANCHOR_POSITOTN_ID, 0, 0);
        initView();
        getAnchorData(true);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getAnchorData(false);
            }
        });

        list = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0 || position == list.size()) ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AnchorAdapter(getContext(), list);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        addFooterView();
        int space = (int) ScreenUtils.instance(getContext()).dip2px(5);
        AnchorItemDecoration itemDecotation = new AnchorItemDecoration(space);
        recyclerView.addItemDecoration(itemDecotation);
        recyclerView.setAdapter(mAdapter);
        adapter.setOnClickAnchorItemListener(new AnchorAdapter.OnClickAnchorItemListener() {
            @Override
            public void onClickAnchorItem(int position) {
                toVideoDetail(list.get(position));
            }
        });
    }

    private void getAnchorData(final boolean isShowLoading) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoading) {
                statusViewLayout.showLoading();
            }
            HashMap<String, String> params = API.createParams();
            params.put("position_id", "4");
            OkHttpUtils.get().url(API.URL_PRE + API.VIP_INDEX).params(params).tag(TAG).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (isShowLoading) {
                        if (statusViewLayout != null) {
                            statusViewLayout.showFailed(retryListener);
                        }
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int code) {
                    try {
                        SharedPreferencesUtil.putString(getContext(), "NOTIFY_DATA", s);
                        VipInfo vipInfo = JSON.parseObject(s, VipInfo.class);
                        list.clear();
                        for (int i = 0; i < vipInfo.getOther().size(); i++) {
                            for (int j = 0; j < vipInfo.getOther().get(i).getData().size(); j++) {
                                VideoInfo videoInfo = vipInfo.getOther().get(i).getData().get(j);
                                list.add(videoInfo);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (isShowLoading) {
                            if (statusViewLayout != null) {
                                statusViewLayout.showContent();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isShowLoading) {
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
            if (isShowLoading) {
                if (statusViewLayout != null) {
                    statusViewLayout.showNetError(retryListener);
                }
            } else {
                ptrLayout.refreshComplete();
            }
        }
    }

    //重试监听
    View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAnchorData(true);
        }
    };

    private void addFooterView() {
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_vip_footer, null);
        TextView footerText = (TextView) footerView.findViewById(R.id.footer_text);
        footerText.setVisibility(View.GONE);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("加载中...");
                }
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.cancel();
                                }
                            }
                        });
                    }
                }, 3000);

            }
        });
        mAdapter.addFooter(footerView);
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
        OkHttpUtils.getInstance().cancelTag(TAG);
        super.onDestroyView();
    }
}
