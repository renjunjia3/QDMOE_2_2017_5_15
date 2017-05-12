package com.mzhguqvn.mzhguq.ui.fragment.film;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.FlimAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.FlimInfo;
import com.mzhguqvn.mzhguq.event.StartBrotherEvent;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DialogUtil;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:片库
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class FilmFragment extends BaseMainFragment {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private RequestCall requestCall;

    private List<FlimInfo> list;
    private FlimAdapter adapter;

    private ProgressDialog progressDialog;

    public static FilmFragment newInstance() {
        Bundle args = new Bundle();
        FilmFragment fragment = new FilmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
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
        params.put("position_id", "9");
        params.put("user_id", App.USER_ID + "");
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
        list = new ArrayList<>();
        adapter = new FlimAdapter(getContext(), list);
        listview.setAdapter(adapter);

        adapter.setOnClickFlimItemListener(new FlimAdapter.OnClickFlimItemListener() {
            @Override
            public void onClickFlimItem(int position) {
                EventBus.getDefault().post(new StartBrotherEvent(FlimDetailFragment.newInstance(list.get(position).getId(), list.get(position).getTitle())));
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
                        DialogUtil.getInstance().showGoldVipDialog(getContext(), 0, false, 9);
                    } else if (App.isVip == 1) {
                        DialogUtil.getInstance().showDiamondVipDialog(getContext(), 0, false, 9);
                    } else {
                        DialogUtil.getInstance().showBlackGlodVipDialog(getContext(), 0, false, 9);
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
        listview.addFooterView(footerView);
    }

    private void getData(final boolean isShowLoad) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoad) {
                statusViewLayout.showLoading();
            }
            requestCall = OkHttpUtils.get().url(API.URL_PRE + API.FILM_INDEX).build();
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
                        list.addAll(JSON.parseArray(s, FlimInfo.class));
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

    @Override
    public void onDestroyView() {
        if (requestCall != null) {
            requestCall.cancel();
        }
        listview.setAdapter(null);
        super.onDestroyView();
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getData(true);
        }
    };
}
