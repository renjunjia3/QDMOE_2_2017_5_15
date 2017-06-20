package com.mzhguqvn.mzhguq.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.OrderAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
import com.mzhguqvn.mzhguq.bean.OrderInfo;
import com.mzhguqvn.mzhguq.bean.OrderListResultInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
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
 * Case By:我的订单
 * package:com.fldhqd.nspmalf.ui.fragment.mine
 * Author：scene on 2017/5/9 11:03
 */

public class OrderFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;

    private List<OrderInfo> list;
    private OrderAdapter adapter;

    private RequestCall getDataRequestCall;

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbarNav(toolbar);
        toolbarTitle.setText("订单");
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
        getData(true);
        MainActivity.upLoadPageInfo(PageConfig.ORDER_LIST_POSITOTN_ID, 0, 0);
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
        adapter = new OrderAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                start(OrderDetailFragment.newInstance(list.get(position)));
            }
        });
    }

    /**
     * Case By:获取数据
     * Author: scene on 2017/5/10 17:55
     *
     * @param isShowLoading 是否显示加载中
     */
    private void getData(final boolean isShowLoading) {
        if (isShowLoading) {
            statusViewLayout.showLoading();
        }
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            HashMap<String, String> params = API.createParams();
            params.put("user_id", String.valueOf(App.user_id));
            getDataRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.GET_ORDERS).params(params).build();
            getDataRequestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    try{
                        if (isShowLoading) {
                            statusViewLayout.showFailed(retryListener);
                        } else {
                            ptrLayout.refreshComplete();
                        }
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        OrderListResultInfo orderListResultInfo = JSON.parseObject(s, OrderListResultInfo.class);
                        list.clear();
                        list.addAll(orderListResultInfo.getData());
                        adapter.notifyDataSetChanged();
                        if (isShowLoading) {
                            statusViewLayout.showContent();
                        } else {
                            ptrLayout.refreshComplete();
                        }
                        if (list.size() == 0) {
                            statusViewLayout.showNone(retryListener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        try{
                            if (isShowLoading) {
                                statusViewLayout.showFailed(retryListener);
                            } else {
                                ptrLayout.refreshComplete();
                            }
                        }catch (Exception e1){
                            e1.printStackTrace();
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

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getData(true);
        }
    };

    @Override
    public void onDestroyView() {
        if (getDataRequestCall != null) {
            getDataRequestCall.cancel();
        }
        super.onDestroyView();
    }

}
