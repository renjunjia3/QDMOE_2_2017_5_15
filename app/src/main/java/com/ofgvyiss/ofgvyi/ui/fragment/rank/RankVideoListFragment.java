package com.ofgvyiss.ofgvyi.ui.fragment.rank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ofgvyiss.ofgvyi.R;
import com.ofgvyiss.ofgvyi.VideoDetailActivity;
import com.ofgvyiss.ofgvyi.adapter.RankVideoListAdapter;
import com.ofgvyiss.ofgvyi.app.App;
import com.ofgvyiss.ofgvyi.base.BaseBackFragment;
import com.ofgvyiss.ofgvyi.bean.RankInfo;
import com.ofgvyiss.ofgvyi.bean.RankListInfo;
import com.ofgvyiss.ofgvyi.bean.VideoInfo;
import com.ofgvyiss.ofgvyi.pull_loadmore.PtrClassicFrameLayout;
import com.ofgvyiss.ofgvyi.pull_loadmore.PtrDefaultHandler;
import com.ofgvyiss.ofgvyi.pull_loadmore.PtrFrameLayout;
import com.ofgvyiss.ofgvyi.pull_loadmore.loadmore.GridViewWithHeaderAndFooter;
import com.ofgvyiss.ofgvyi.util.API;
import com.ofgvyiss.ofgvyi.util.DialogUtil;
import com.ofgvyiss.ofgvyi.util.NetWorkUtils;
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
 * Case By:
 * package:com.hfaufhreu.hjfeuio.ui.fragment.rank
 * Author：scene on 2017/4/19 22:20
 */

public class RankVideoListFragment extends BaseBackFragment {
    private static final String ARG_ID = "arg_id";
    private static final String ARG_POSITION = "arg_position";
    private static final String ARG_NAME = "arg_name";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.gridView)
    GridViewWithHeaderAndFooter gridView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private int id = 0;
    private int position = 0;
    private String name = "";

    private RequestCall requestCall;


    private RankListInfo rankListInfo;
    private List<VideoInfo> list;
    private RankVideoListAdapter adapter;


    private ImageView image;
    private TextView number;
    private TextView actorName;
    private TextView age;
    private TextView description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            id = args.getInt(ARG_ID);
            position = args.getInt(ARG_POSITION);
            name = args.getString(ARG_NAME);
        }
    }

    public static RankVideoListFragment newInstance(int id, int position, String name) {
        RankVideoListFragment fragment = new RankVideoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_video_list, container, false);
        ButterKnife.bind(this, view);
        toolbarTitle.setText(name);
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
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
        params.put("position_id", "6");
        params.put("user_id", App.USER_ID + "");
        params.put("actor_id", id + "");
        OkHttpUtils.post().url(API.URL_PRE + API.UPLOAD_CURRENT_PAGE).params(params).build().execute(null);
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
        addHeaderView();
        adapter = new RankVideoListAdapter(getContext(), list);
        gridView.setAdapter(adapter);
    }


    private void addHeaderView() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_rank_video_list_header, null);
        image = (ImageView) headerView.findViewById(R.id.image);
        number = (TextView) headerView.findViewById(R.id.number);
        actorName = (TextView) headerView.findViewById(R.id.actor_name);
        age = (TextView) headerView.findViewById(R.id.age);
        description = (TextView) headerView.findViewById(R.id.description);
        gridView.addHeaderView(headerView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (App.isVip > 0) {
                    toVideoDetail(list.get(position));
                } else {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该片为会员视频，请开通会员后观看", App.isVip, false, true, list.get(position).getVideo_id());
                }
            }
        });
    }

    /**
     * Case By:绑定headerview数据
     * Author: scene on 2017/4/20 9:42
     */
    private void bindHeaderView(RankInfo rankInfo) {
        number.setText("NO." + position);
        actorName.setText(rankInfo.getActor_name());
        age.setText("年龄：" + rankInfo.getAge());
        description.setText(rankInfo.getDescription());
        Glide.with(getContext()).load(rankInfo.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(image);
    }

    private void getData(final boolean isShowLoad) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoad) {
                statusViewLayout.showLoading();
            }
            requestCall = OkHttpUtils.get().url(API.URL_PRE + API.ACTOR_LIST + id).build();
            requestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (isShowLoad) {
                        statusViewLayout.showFailed(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        rankListInfo = JSON.parseObject(s, RankListInfo.class);
                        list.clear();
                        list.addAll(rankListInfo.getVideos());
                        adapter.notifyDataSetChanged();
                        bindHeaderView(rankListInfo.getActor());
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


    /**
     * Case By:跳转到视频详情页
     * Author: scene on 2017/4/19 9:33
     *
     * @param videoInfo 视频信息
     */
    private void toVideoDetail(VideoInfo videoInfo) {
        Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
        intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, videoInfo);
        intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_FROM_TRY_SEE, false);
        _mActivity.startActivityForResult(intent, 9999);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9999 && resultCode == RESULT_OK) {
            _mActivity.onBackPressed();
        }
    }
}
