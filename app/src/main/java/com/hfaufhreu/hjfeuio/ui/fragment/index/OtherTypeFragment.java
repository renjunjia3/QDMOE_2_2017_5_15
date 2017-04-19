package com.hfaufhreu.hjfeuio.ui.fragment.index;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.VideoDetailActivity;
import com.hfaufhreu.hjfeuio.adapter.VideoListAdapter;
import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.base.BaseFragment;
import com.hfaufhreu.hjfeuio.base.BaseRecyclerAdapter;
import com.hfaufhreu.hjfeuio.bean.VideoInfo;
import com.hfaufhreu.hjfeuio.itemdecoration.IndexOtherTypeItemDecoration;
import com.hfaufhreu.hjfeuio.pay.PayUtil;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrClassicFrameLayout;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrDefaultHandler;
import com.hfaufhreu.hjfeuio.pull_loadmore.PtrFrameLayout;
import com.hfaufhreu.hjfeuio.pull_loadmore.recyclerview.RecyclerAdapterWithHF;
import com.hfaufhreu.hjfeuio.ui.dialog.FullVideoPayDialog;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.NetWorkUtils;
import com.hfaufhreu.hjfeuio.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * 最新 推荐之外的类别界面
 * Created by scene on 2017/3/13.
 */

public class OtherTypeFragment extends BaseFragment {
    private static final String ARG_TYPE = "arg_pos";
    public static final int TYPE_2 = 1;
    public static final int TYPE_3 = 2;
    public static final int TYPE_4 = 3;
    public static final int TYPE_5 = 4;
    public static final int TYPE_6 = 5;

    private int mType = TYPE_2;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private List<VideoInfo> mList;
    private VideoListAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;

    private FullVideoPayDialog dialog;
    private FullVideoPayDialog.Builder builder;

    public static OtherTypeFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        OtherTypeFragment fragment = new OtherTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(ARG_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index_other_type, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initRecyclerView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        mList = new ArrayList<>();
        adapter = new VideoListAdapter(OtherTypeFragment.this, mList);
        recyclerView.addItemDecoration(new IndexOtherTypeItemDecoration((int) getResources().getDimension(R.dimen.other_type_space)));
        mAdapter = new RecyclerAdapterWithHF(adapter);
        View footerView = LayoutInflater.from(_mActivity).inflate(R.layout.recycler_footer, null);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.isVip == 1) {
                    ToastUtils.getInstance(_mActivity).showToast("该功能完善中，敬请期待");
                } else {
                    dialog.show();
                }
            }
        });
        mAdapter.addFooter(footerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getVideoList(false);
            }
        });
        adapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new Intent(_mActivity, VideoDetailActivity.class);
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, mList.get(position));
                intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_FROM_TRY_SEE, false);
                _mActivity.startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
        //EventBus.getDefault().unregister(this);
    }


    private void getVideoList(boolean isShowLoading) {
        if (isShowLoading) {
            statusViewLayout.showLoading();
        }

        if (!NetWorkUtils.isNetworkConnected(_mActivity)) {
            statusViewLayout.showNetError(onClickRetryListener);
        } else {
            OkHttpUtils.get().url(API.URL_PRE + API.CATE_VIDEOS + mType).build().execute(new StringCallback() {
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
                            if (!statusViewLayout.isContent()) {
                                statusViewLayout.showContent();
                            }
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

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initDialog();
        initRecyclerView();
        getVideoList(true);
    }

    private void initDialog() {
        builder = new FullVideoPayDialog.Builder(_mActivity);
        builder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByWeChat(_mActivity, 1, 0);
            }
        });

        builder.setAliPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByAliPay(_mActivity, 1, 0);
            }
        });
        dialog = builder.create();
    }

    /**
     * 重试
     */
    View.OnClickListener onClickRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getVideoList(true);
        }
    };

}
