package com.mzhguqvn.mzhguq.ui.fragment.bbs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.BBSAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.BBSInfo;
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
 * Case By:福利社
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class BBSFragment extends BaseMainFragment implements BBSAdapter.BBSItemOnClickListener {

    @BindView(R.id.gridView)
    GridViewWithHeaderAndFooter gridView;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;

    private RequestCall call;

    private List<BBSInfo> lists;

    private BBSAdapter adapter;

    private ProgressDialog progressDialog;

    public static BBSFragment newInstance() {
        Bundle args = new Bundle();
        BBSFragment fragment = new BBSFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        getBBSData(true);
        uploadCurrentPage();
    }

    /**
     * Case By:上报当前页面
     * Author: scene on 2017/4/27 17:05
     */
    private void uploadCurrentPage() {
        Map<String, String> params = new HashMap<>();
        params.put("position_id", "11");
        params.put("user_id", App.USER_ID + "");
        OkHttpUtils.post().url(API.URL_PRE + API.UPLOAD_CURRENT_PAGE).params(params).build().execute(null);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getBBSData(false);
            }
        });

        addFooterView();

        lists = new ArrayList<>();
        adapter = new BBSAdapter(getContext(), lists);
        adapter.setBbsItemOnClickListener(this);
        gridView.setAdapter(adapter);
    }

    private void addFooterView() {
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_vip_footer, null);
        TextView footerText = (TextView) footerView.findViewById(R.id.footer_text);
        if (App.isVip == 0) {
            footerText.setText("请开通会员开放更多内容");
        } else if (App.isVip == 1) {
            footerText.setText("请升级钻石会员开放更多内容");
        } else if (App.isVip < 5 && App.isHeijin == 0) {
            footerText.setText("请升级黑金会员开放更多内容");
        } else {
            footerText.setVisibility(View.GONE);
        }
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.isVip < 5 && App.isHeijin == 0) {
                    if (App.isVip == 0) {
                        DialogUtil.getInstance().showGoldVipDialog(getContext(), 0, false, 11);
                    } else if (App.isVip == 1) {
                        DialogUtil.getInstance().showDiamondVipDialog(getContext(), 0, false, 11);
                    } else {
                        DialogUtil.getInstance().showBlackGlodVipDialog(getContext(), 0, false, 11);
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

    /**
     * Case By:
     * Author: scene on 2017/4/18 13:05
     * 获取bbs数据
     */
    private void getBBSData(final boolean isShowLoading) {
        if (NetWorkUtils.isNetworkConnected(_mActivity)) {
            if (isShowLoading) {
                statusViewLayout.showLoading();
            }

            call = OkHttpUtils.get().url(API.URL_PRE + API.BBS_LIST).build();
            call.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (isShowLoading) {
                        statusViewLayout.showFailed(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        lists.clear();
                        lists.addAll(JSON.parseArray(s, BBSInfo.class));
                        adapter.notifyDataSetChanged();
                        statusViewLayout.showContent();
                        ptrLayout.refreshComplete();
                    } catch (Exception e) {
                        if (isShowLoading) {
                            statusViewLayout.showFailed(retryListener);
                        } else {
                            ptrLayout.refreshComplete();
                        }
                    }
                }
            });
        } else {
            if (isShowLoading) {
                statusViewLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
        }


    }


    /**
     * Case By:列表的点击事件
     * Author: scene on 2017/4/18 13:52
     *
     * @param position 下标
     */
    @Override
    public void onBBsItemOnClick(int position) {
        if (App.isVip == 0) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该栏目只对会员开放，请先升级会员", App.isVip, true, 11);
        } else if (App.isVip == 1) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "您的会员权限不足，请先升级钻石会员", App.isVip, true, 11);
        }
    }

    @Override
    public void onDestroyView() {
        if (call != null) {
            call.cancel();
        }
        gridView.setAdapter(null);
        super.onDestroyView();
    }

    View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getBBSData(true);
        }
    };

}
