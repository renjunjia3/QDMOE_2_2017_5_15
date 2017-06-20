package com.mzhguqvn.mzhguq.ui.fragment.channel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.ChannelAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.ChannelResultInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.event.StartBrotherEvent;
import com.mzhguqvn.mzhguq.itemdecoration.ActorItemDecoration;
import com.mzhguqvn.mzhguq.listener.OnTaskFinishedListener;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.task.ChannelTask;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DialogUtil;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.mzhguqvn.mzhguq.util.ScreenUtils;
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
 * Case By:频道
 * package:com.mzhguqvn.mzhguq.ui.fragment.channel
 * Author：scene on 2017/6/1 12:06
 */

public class ChannelFragment extends BaseMainFragment implements ChannelAdapter.OnClickChannelItemListener {
    private static final String TAG = "ChannelFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private ChannelAdapter adapter;
    private List<ChannelResultInfo.DataBean> list;

    public static ChannelFragment newInstance() {
        return new ChannelFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.CHANNEL_POSITION_ID, 0, 0);
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
        adapter = new ChannelAdapter(getContext(), list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int space = ScreenUtils.instance(getContext()).dp2px(5);
        recyclerView.addItemDecoration(new ActorItemDecoration(space));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickChannelItemListener(this);
    }

    private void getdata(final boolean isShowLoading) {
        HashMap<String, String> params = API.createParams();
        OkHttpUtils.get().url(API.URL_PRE + API.CHANNEL).params(params).tag(TAG).build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                if (!NetWorkUtils.isNetworkConnected(getContext())) {
                    showStatus(3);
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
                    new ChannelTask(getContext(), new OnTaskFinishedListener<ChannelResultInfo>() {
                        @Override
                        public void onTaskFinished(ChannelResultInfo data) {
                            if (data.isStatus()) {
                                if (data.getData() != null && data.getData().size() > 0) {
                                    showStatus(1);
                                    list.clear();
                                    list.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    showStatus(4);
                                }
                            } else {
                                showStatus(2);
                            }
                        }
                    }, s).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } catch (Exception e) {
                    e.printStackTrace();
                    showStatus(2);
                }
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                showStatus(2);
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

    private void showStatus(int status) {
        try {
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
            ptrLayout.refreshComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClickChannelItem(int position) {
        if (App.role <= 2) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, getString(R.string.other_channer_open_vip_notice), App.role, true, PageConfig.CHANNEL_POSITION_ID, true);
        } else {
            EventBus.getDefault().post(new StartBrotherEvent(ChannelDetailFragment.newInstance(list.get(position).getId(), list.get(position).getTitle())));
        }
    }
}
