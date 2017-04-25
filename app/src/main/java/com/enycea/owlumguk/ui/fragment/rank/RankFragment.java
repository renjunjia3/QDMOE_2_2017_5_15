package com.enycea.owlumguk.ui.fragment.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.enycea.owlumguk.R;
import com.enycea.owlumguk.adapter.RankAdapter;
import com.enycea.owlumguk.adapter.RankHeaderAdapter;
import com.enycea.owlumguk.base.BaseMainFragment;
import com.enycea.owlumguk.bean.RankInfo;
import com.enycea.owlumguk.event.StartBrotherEvent;
import com.enycea.owlumguk.pull_loadmore.PtrClassicFrameLayout;
import com.enycea.owlumguk.pull_loadmore.PtrDefaultHandler;
import com.enycea.owlumguk.pull_loadmore.PtrFrameLayout;
import com.enycea.owlumguk.ui.view.CustomeGridView;
import com.enycea.owlumguk.util.API;
import com.enycea.owlumguk.util.NetWorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:排行榜
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class RankFragment extends BaseMainFragment {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private RequestCall requestCall;


    private List<RankInfo> headerList;
    private List<RankInfo> list;
    private RankAdapter adapter;

    //headerview
    private View headerView;
    private CustomeGridView gridView;
    private RankHeaderAdapter headerAdapter;

    public static RankFragment newInstance() {
        Bundle args = new Bundle();
        RankFragment fragment = new RankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
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
        adapter = new RankAdapter(getContext(), list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    EventBus.getDefault().post(new StartBrotherEvent(RankVideoListFragment.newInstance(list.get(position - 1).getId(), position + 3, list.get(position - 1).getActor_name())));
                }
            }
        });
        addheader();
    }


    private void addheader() {
        headerList = new ArrayList<>();
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_rank_header, null);
        gridView = (CustomeGridView) headerView.findViewById(R.id.headGridView);
        headerAdapter = new RankHeaderAdapter(getContext(), headerList);
        gridView.setAdapter(headerAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int number = 0;
                if (position == 0) {
                    number = 2;
                } else if (position == 1) {
                    number = 1;
                } else {
                    number = 3;
                }
                EventBus.getDefault().post(new StartBrotherEvent(RankVideoListFragment.newInstance(headerList.get(position).getId(), number, headerList.get(position).getActor_name())));
            }
        });
        listview.addHeaderView(headerView);
    }


    private void getData(final boolean isShowLoad) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoad) {
                statusViewLayout.showLoading();
            }
            requestCall = OkHttpUtils.get().url(API.URL_PRE + API.ACTOR).build();
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
                public void onResponse(String s, int code) {
                    try {
                        List<RankInfo> tempList = JSON.parseArray(s, RankInfo.class);
                        headerList.clear();
                        list.clear();
                        for (int i = 0; i < tempList.size(); i++) {
                            if (i < 3) {
                                if (i == 1) {
                                    headerList.add(0, tempList.get(i));
                                } else {
                                    headerList.add(tempList.get(i));
                                }
                            } else {
                                list.add(tempList.get(i));
                            }
                        }
                        headerAdapter.notifyDataSetChanged();
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
        super.onDestroyView();
    }


    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getData(true);
        }
    };
}
