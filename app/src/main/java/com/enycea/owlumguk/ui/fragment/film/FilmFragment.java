package com.enycea.owlumguk.ui.fragment.film;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.enycea.owlumguk.R;
import com.enycea.owlumguk.adapter.FlimAdapter;
import com.enycea.owlumguk.base.BaseMainFragment;
import com.enycea.owlumguk.bean.FlimInfo;
import com.enycea.owlumguk.event.StartBrotherEvent;
import com.enycea.owlumguk.itemdecoration.CustomItemDecotation;
import com.enycea.owlumguk.pull_loadmore.PtrClassicFrameLayout;
import com.enycea.owlumguk.pull_loadmore.PtrDefaultHandler;
import com.enycea.owlumguk.pull_loadmore.PtrFrameLayout;
import com.enycea.owlumguk.util.API;
import com.enycea.owlumguk.util.NetWorkUtils;
import com.enycea.owlumguk.util.ScreenUtils;
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
 * Case By:片库
 * package:com.hfaufhreu.hjfeuio.ui.fragment.vip
 * Author：scene on 2017/4/17 10:17
 */

public class FilmFragment extends BaseMainFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private RequestCall requestCall;

    private List<FlimInfo> list;
    private FlimAdapter adapter;

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
        adapter = new FlimAdapter(getContext(), list);
        int space = (int) ScreenUtils.instance(getContext()).dip2px(10);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new CustomItemDecotation(space, space, 1, true));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickFlimItemListener(new FlimAdapter.OnClickFlimItemListener() {
            @Override
            public void onClickFlimItem(int position) {
                EventBus.getDefault().post(new StartBrotherEvent(FlimDetailFragment.newInstance(list.get(position).getId(), list.get(position).getTitle())));
            }
        });
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
        if(requestCall!=null){
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
