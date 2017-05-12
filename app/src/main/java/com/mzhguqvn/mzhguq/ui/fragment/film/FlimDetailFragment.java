package com.mzhguqvn.mzhguq.ui.fragment.film;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.VideoDetailActivity;
import com.mzhguqvn.mzhguq.adapter.FlimDetailAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
import com.mzhguqvn.mzhguq.bean.VideoInfo;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.loadmore.GridViewWithHeaderAndFooter;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DialogUtil;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
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
    @BindView(R.id.gridView)
    GridViewWithHeaderAndFooter gridView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private int flimId = 0;
    private String flimName = "";

    private List<VideoInfo> list;
    private FlimDetailAdapter adapter;

    private RequestCall requestCall;

    private ProgressDialog progressDialog;

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
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
        getData(true);
        uploadCurrentPage();
    }

    /**
     * Case By:上报当前页面
     * Author: scene on 2017/4/27 17:05
     */
    private void uploadCurrentPage() {
        Map<String, String> params = new HashMap<>();
        params.put("position_id", "10");
        params.put("user_id", App.USER_ID + "");
        params.put("cate_id", flimId + "");
        OkHttpUtils.post().url(API.URL_PRE + API.UPLOAD_CURRENT_PAGE).params(params).build().execute(null);
    }

    private void initView() {
        addFooterView();
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        ptrLayout.disableWhenHorizontalMove(false);
        list = new ArrayList<>();
        adapter = new FlimDetailAdapter(getContext(), list);
        gridView.setAdapter(adapter);
        adapter.setOnClickItemVideoListener(new FlimDetailAdapter.OnClickItemVideoListener() {
            @Override
            public void onClickItemVideo(int positon) {
                if (App.isVip > 0) {
                    toVideoDetail(list.get(positon));
                } else {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该片为会员视频，请开通会员后观看", App.isVip, false, true, list.get(positon).getVideo_id(), false,10);
                }
            }
        });
    }

    private void addFooterView() {
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_vip_footer, null);
        TextView footerText = (TextView) footerView.findViewById(R.id.footer_text);
        if (App.isVip == 0) {
            footerText.setText("请开通会员开放更多影片资源");
        } else if (App.isVip == 1) {
            footerText.setText("请升级钻石会员开放更多影片资源");
        } else if (App.isVip < 5 && App.isHeijin == 0) {
            footerText.setText("请升级黑金会员开放更多影片资源");
        } else {
            footerText.setVisibility(View.GONE);
        }
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.isVip < 5 && App.isHeijin == 0) {
                    if (App.isVip == 0) {
                        DialogUtil.getInstance().showGoldVipDialog(getContext(), 0, false,10);
                    } else if (App.isVip == 1) {
                        DialogUtil.getInstance().showDiamondVipDialog(getContext(), 0, false,10);
                    } else {
                        DialogUtil.getInstance().showBlackGlodVipDialog(getContext(), 0, false,10);
                    }
                } else {
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
            }
        });
        gridView.addFooterView(footerView);
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
        gridView.setAdapter(null);
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hideSoftInput();
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
