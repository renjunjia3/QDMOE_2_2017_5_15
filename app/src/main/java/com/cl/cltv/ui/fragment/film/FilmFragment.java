package com.cl.cltv.ui.fragment.film;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.cl.cltv.MainActivity;
import com.cl.cltv.R;
import com.cl.cltv.adapter.FilmAdapter;
import com.cl.cltv.app.App;
import com.cl.cltv.base.BaseMainFragment;
import com.cl.cltv.bean.RankResultInfo;
import com.cl.cltv.config.PageConfig;
import com.cl.cltv.event.StartBrotherEvent;
import com.cl.cltv.itemdecoration.FilmItemDecoration;
import com.cl.cltv.pull_loadmore.PtrClassicFrameLayout;
import com.cl.cltv.pull_loadmore.PtrDefaultHandler;
import com.cl.cltv.pull_loadmore.PtrFrameLayout;
import com.cl.cltv.util.API;
import com.cl.cltv.util.DialogUtil;
import com.cl.cltv.util.NetWorkUtils;
import com.cl.cltv.util.ScreenUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:片库
 * package:com.mzhguqvn.mzhguq.ui.fragment.film
 * Author：scene on 2017/6/12 09:58
 */

public class FilmFragment extends BaseMainFragment implements FilmAdapter.OnClickFilmItemListener {
    private static final String TAG = "FilmFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private FilmAdapter adapter;
    private List<RankResultInfo.DataBean> list;

    public static FilmFragment newInstance() {
        return new FilmFragment();
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
        MainActivity.upLoadPageInfo(PageConfig.FILM_POSITION_ID, 0, 0);
        initView();
        getdata(true);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getdata(false);
            }
        });

        list = new ArrayList<>();
        adapter = new FilmAdapter(getContext(), list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        int space = ScreenUtils.instance(getContext()).dp2px(3);
        recyclerView.addItemDecoration(new FilmItemDecoration(space));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickFilmItemListener(this);
    }

    private void getdata(final boolean isShowLoading) {
        HashMap<String, String> params = API.createParams();
        OkHttpUtils.get().url(API.URL_PRE + API.FILM).params(params).tag(TAG).build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                if (!NetWorkUtils.isNetworkConnected(getContext())) {
                    showStatus(isShowLoading, 3);
                    OkHttpUtils.getInstance().cancelTag(TAG);
                } else {
                    if (isShowLoading) {
                        statusViewLayout.showLoading();
                    }
                }
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    Log.e(TAG, s);
                    RankResultInfo info = JSON.parseObject(s, RankResultInfo.class);
                    if (info.isStatus()) {
                        if (info.getData() != null && info.getData().size() > 0) {
                            showStatus(isShowLoading, 1);
                            list.clear();
                            list.addAll(info.getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            showStatus(isShowLoading, 4);
                        }
                    } else {
                        showStatus(isShowLoading, 2);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showStatus(isShowLoading, 2);
                }
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                showStatus(isShowLoading, 2);
            }
        });
    }

    View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getdata(true);
        }
    };

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(TAG);
        super.onDestroyView();
    }

    private void showStatus(boolean isShowLoading, int status) {
        try {
            if (isShowLoading) {
                switch (status) {
                    case 1:
                        //正常
                        statusViewLayout.showContent();
                        break;
                    case 2:
                        //失败
                        statusViewLayout.showFailed(retryListener);
                        break;
                    case 3:
                        //无网
                        statusViewLayout.showNetError(retryListener);
                        break;
                    case 4:
                        //无内容
                        statusViewLayout.showNone(retryListener);
                        break;
                }
            } else {
                ptrLayout.refreshComplete();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onClickFilmItem(int position) {
        if (App.role <= 2) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, getString(R.string.other_channer_open_vip_notice), App.role, true, PageConfig.FILM_POSITION_ID, true);
        } else {
            EventBus.getDefault().post(new StartBrotherEvent(FilmDetailFragment.newInstance(list.get(position).getId(), list.get(position).getTitle())));
        }
    }

}
