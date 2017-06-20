package com.mzhguqvn.mzhguq.ui.fragment.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.joooonho.SelectableRoundedImageView;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.RankAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.RankResultInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.event.StartBrotherEvent;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DialogUtil;
import com.mzhguqvn.mzhguq.util.GlideUtils;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:排行榜
 * package:com.mzhguqvn.mzhguq.ui.fragment.rank
 * Author：scene on 2017/6/9 10:46
 */

public class RankFragment extends BaseMainFragment {
    private static final String TAG = "RankFragment";
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.listview)
    ListView listView;
    //headerView
    private LinearLayout headerLayout1, headerLayout2, headerLayout3;
    private SelectableRoundedImageView headerImage1, headerImage2, headerImage3;
    private TextView headerName1, headerName2, headerName3;
    private TextView headerScore1, headerScore2, headerScore3;

    private List<RankResultInfo.DataBean> list;
    private List<RankResultInfo.DataBean> headerList;
    private RankAdapter adapter;

    public static RankFragment newInstance() {
        return new RankFragment();
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
        MainActivity.upLoadPageInfo(PageConfig.RANK_POSITION_ID, 0, 0);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getdata(false);
            }
        });

        initHeaderView();
        headerList = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new RankAdapter(getContext(), list);
        listView.setAdapter(adapter);
        getdata(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    itemClick(list.get(position - 1));
                }
            }
        });
    }

    /**
     * 初始化headerview
     */
    private void initHeaderView() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_rank_header, null, false);
        headerLayout1 = (LinearLayout) headerView.findViewById(R.id.headerLayout_1);
        headerLayout2 = (LinearLayout) headerView.findViewById(R.id.headerLayout_2);
        headerLayout3 = (LinearLayout) headerView.findViewById(R.id.headerLayout_3);
        headerImage1 = (SelectableRoundedImageView) headerView.findViewById(R.id.headerImage1);
        headerImage2 = (SelectableRoundedImageView) headerView.findViewById(R.id.headerImage2);
        headerImage3 = (SelectableRoundedImageView) headerView.findViewById(R.id.headerImage3);
        headerName1 = (TextView) headerView.findViewById(R.id.headerName1);
        headerName2 = (TextView) headerView.findViewById(R.id.headerName2);
        headerName3 = (TextView) headerView.findViewById(R.id.headerName3);
        headerScore1 = (TextView) headerView.findViewById(R.id.headerScore1);
        headerScore2 = (TextView) headerView.findViewById(R.id.headerScore2);
        headerScore3 = (TextView) headerView.findViewById(R.id.headerScore3);
        listView.addHeaderView(headerView);
    }

    private void getdata(final boolean isShowLoading) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoading) {
                statusViewLayout.showLoading();
            }
            HashMap<String, String> params = API.createParams();
            OkHttpUtils.get().url(API.URL_PRE + API.RANK).params(params).tag(TAG).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    try{
                        e.printStackTrace();
                        showLoadStatus(3, isShowLoading);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }

                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        RankResultInfo info = JSON.parseObject(s, RankResultInfo.class);
                        if (info.isStatus()) {
                            list.clear();
                            headerList.clear();
                            for (int j = 0; j < info.getData().size(); j++) {
                                if (j < 3) {
                                    headerList.add(info.getData().get(j));
                                } else {
                                    list.add(info.getData().get(j));
                                }
                            }
                            bindHeaderViewData();
                            adapter.notifyDataSetChanged();
                            showLoadStatus(1, isShowLoading);
                        } else {
                            showLoadStatus(3, isShowLoading);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showLoadStatus(3, isShowLoading);
                    }
                }
            });
        } else {
            showLoadStatus(2, isShowLoading);
        }
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getdata(true);
        }
    };

    /**
     * 显示状态
     */
    private void showLoadStatus(int status, boolean isShowLoading) {
        try {
            if (isShowLoading) {
                switch (status) {
                    case 1:
                        statusViewLayout.showContent();
                        break;
                    case 2:
                        statusViewLayout.showNetError(retryListener);
                        break;
                    case 3:
                        statusViewLayout.showFailed(retryListener);
                        break;
                }
            } else {
                ptrLayout.refreshComplete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定headerview的数据
     */
    private void bindHeaderViewData() {
        try {
            GlideUtils.loadImage(getContext(), headerImage1, headerList.get(0).getThumb());
            headerName1.setText(headerList.get(0).getTitle());
            headerScore1.setText(headerList.get(0).getScore() + "票");
            headerLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(headerList.get(0));
                }
            });
            if (null == headerList.get(1)) {
                return;
            }
            GlideUtils.loadImage(getContext(), headerImage2, headerList.get(1).getThumb());
            headerName2.setText(headerList.get(1).getTitle());
            headerScore2.setText(headerList.get(1).getScore() + "票");
            headerLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(headerList.get(1));
                }
            });
            if (null == headerList.get(2)) {
                return;
            }
            GlideUtils.loadImage(getContext(), headerImage3, headerList.get(2).getThumb());
            headerName3.setText(headerList.get(2).getTitle());
            headerScore3.setText(headerList.get(2).getScore() + "票");
            headerLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(headerList.get(2));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RankFragment", "数据不足3个");
        }
    }


    /**
     * 处理item点击之后的动作
     */
    private void itemClick(RankResultInfo.DataBean dataBean) {
        if (App.role <= 2) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, getString(R.string.other_channer_open_vip_notice), App.role, true, PageConfig.RANK_POSITION_ID, true);
        } else {
            EventBus.getDefault().post(new StartBrotherEvent(RankDetailFragment.newInstance(dataBean.getId(), dataBean.getTitle())));
        }
    }


    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(TAG);
        super.onDestroyView();
    }
}
