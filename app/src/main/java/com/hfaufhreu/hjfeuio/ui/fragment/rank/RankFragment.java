package com.hfaufhreu.hjfeuio.ui.fragment.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.adapter.RankAdapter;
import com.hfaufhreu.hjfeuio.base.BaseMainFragment;
import com.hfaufhreu.hjfeuio.bean.RankInfo;
import com.hfaufhreu.hjfeuio.event.StartBrotherEvent;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrClassicFrameLayout;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrDefaultHandler;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrFrameLayout;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.NetWorkUtils;
import com.hfaufhreu.hjfeuio.util.ScreenUtils;
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


    private View headerView;
    private ImageView image1, image2, image3;
    private TextView name1, name2, name3;
    private TextView number1, number2, number3;

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
        headerList = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new RankAdapter(getContext(), list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    EventBus.getDefault().post(new StartBrotherEvent(RankVideoListFragment.newInstance(list.get(position - 1).getId(), position + 4, list.get(position - 1).getActor_name())));
                }
            }
        });
        addheader();
    }


    private void addheader() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_rank_header, null);
        ScreenUtils screenUtils = ScreenUtils.instance(getContext());
        image1 = (ImageView) headerView.findViewById(R.id.image_1);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int height1 = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(70)) * 8f / 3f / 5f);
        layoutParams1.height = height1;
        image1.setLayoutParams(layoutParams1);

        image2 = (ImageView) headerView.findViewById(R.id.image_2);
        image2.setLayoutParams(layoutParams1);

        image3 = (ImageView) headerView.findViewById(R.id.image_3);
        image3.setLayoutParams(layoutParams1);

        name1 = (TextView) headerView.findViewById(R.id.name_1);
        name2 = (TextView) headerView.findViewById(R.id.name_2);
        name3 = (TextView) headerView.findViewById(R.id.name_3);
        number1 = (TextView) headerView.findViewById(R.id.number_1);
        number2 = (TextView) headerView.findViewById(R.id.number_2);
        number3 = (TextView) headerView.findViewById(R.id.number_3);

        listview.addHeaderView(headerView);

    }

    /**
     * Case By:绑定headerView
     * Author: scene on 2017/4/20 10:20
     */
    private void bindHeaderView(final List<RankInfo> rankInfoList) {
        switch (rankInfoList.size()) {
            case 3:
                name3.setText(rankInfoList.get(2).getActor_name());
                number3.setText(rankInfoList.get(2).getVotes() + "票");
                Glide.with(getContext()).load(rankInfoList.get(2).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(image3);
                headerView.findViewById(R.id.video_layout3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new StartBrotherEvent(RankVideoListFragment.newInstance(rankInfoList.get(2).getId(), 3, rankInfoList.get(2).getActor_name())));
                    }
                });
            case 2:
                name2.setText(rankInfoList.get(1).getActor_name());
                number2.setText(rankInfoList.get(1).getVotes() + "票");
                Glide.with(getContext()).load(rankInfoList.get(1).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(image2);
                headerView.findViewById(R.id.video_layout2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new StartBrotherEvent(RankVideoListFragment.newInstance(rankInfoList.get(1).getId(), 2, rankInfoList.get(1).getActor_name())));
                    }
                });
            case 1:
                name1.setText(rankInfoList.get(0).getActor_name());
                number1.setText(rankInfoList.get(0).getVotes() + "票");
                Glide.with(getContext()).load(rankInfoList.get(0).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(image1);
                headerView.findViewById(R.id.video_layout1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new StartBrotherEvent(RankVideoListFragment.newInstance(rankInfoList.get(0).getId(), 1, rankInfoList.get(0).getActor_name())));
                    }
                });
                break;
        }


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
                                headerList.add(tempList.get(i));
                            } else {
                                list.add(tempList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        bindHeaderView(headerList);
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
