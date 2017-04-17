package com.hfaufhreu.hjfeuio.ui.fragment.actor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.VideoDetailActivity;
import com.hfaufhreu.hjfeuio.adapter.VideoListAdapter;
import com.hfaufhreu.hjfeuio.base.BaseBackFragment;
import com.hfaufhreu.hjfeuio.base.BaseRecyclerAdapter;
import com.hfaufhreu.hjfeuio.itemdecoration.IndexOtherTypeItemDecoration;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrClassicFrameLayout;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrDefaultHandler;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrFrameLayout;
import com.hfaufhreu.hjfeuio.pull_loadmore.recyclerview.RecyclerAdapterWithHF;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.NetWorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.VideoInfo;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Created by 女优视频集 on 2017/3/15.
 */

public class ActorVideoListFragment extends BaseBackFragment {
    private static final String ARG_ACTOR_ID = "arg_actor_id";
    private static final String ARG_ACTOR_NAME = "arg_actor_name";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    private int actorId;
    private String actorName;

    private List<VideoInfo> mList;
    private VideoListAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            actorId = args.getInt(ARG_ACTOR_ID);
            actorName = args.getString(ARG_ACTOR_NAME);
        }
    }

    public static ActorVideoListFragment newInstance(int actorId, String actorName) {
        ActorVideoListFragment fragment = new ActorVideoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ACTOR_ID, actorId);
        args.putString(ARG_ACTOR_NAME, actorName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor_video_list, container, false);
        ButterKnife.bind(this, view);
        toolbarTitle.setText(actorName);
        initToolbarNav(toolbar);
        return view;
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initRecyclerView();
        getActorList(true);
    }

    private void initRecyclerView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        mList = new ArrayList<>();
        adapter = new VideoListAdapter(ActorVideoListFragment.this, mList);
        recyclerView.addItemDecoration(new IndexOtherTypeItemDecoration((int) getResources().getDimension(R.dimen.other_type_space)));
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mAdapter);
        ptrLayout.setLoadMoreEnable(false);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getActorList(false);
            }
        });
        adapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, mList.get(position));
                _mActivity.startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }

    private void getActorList(boolean isShowLoading) {
        if (isShowLoading) {
            statusViewLayout.showLoading();
        }

        if (!NetWorkUtils.isNetworkConnected(_mActivity)) {
            statusViewLayout.showNetError(onClickRetryListener);
        } else {
            OkHttpUtils.get().url(API.URL_PRE + API.ACTOR_LIST + actorId).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (mList.size() <= 0) {
                        statusViewLayout.showFailed(onClickRetryListener);
                    } else {
                        if (!statusViewLayout.isContent()) {
                            statusViewLayout.showContent();
                        }
                    }
                    ptrLayout.refreshComplete();
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        List<VideoInfo> list = JSON.parseArray(s, VideoInfo.class);
                        mList.clear();
                        mList.addAll(list);
                        if (mList.size() <= 0) {
                            statusViewLayout.showNone(onClickRetryListener);
                        } else {
                            statusViewLayout.showContent();
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mList.size() <= 0) {
                            statusViewLayout.showFailed(onClickRetryListener);
                        } else {
                            if (!statusViewLayout.isContent()) {
                                statusViewLayout.showContent();
                            }
                        }
                    } finally {
                        ptrLayout.refreshComplete();
                    }
                }
            });

        }
    }

    /**
     * 重试
     */
    View.OnClickListener onClickRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActorList(true);
        }
    };

}
