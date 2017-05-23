package com.mzhguqvn.mzhguq.ui.fragment.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.GalleryAdapter;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.GalleryResultInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.itemdecoration.CustomItemDecotation;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.ui.dialog.BigImageDialog;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.mzhguqvn.mzhguq.util.ScreenUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:图库
 * package:com.mzhguqvn.mzhguq.ui.fragment.gallery
 * Author：scene on 2017/5/19 10:13
 */

public class GalleryFragment extends BaseMainFragment implements GalleryAdapter.OnGalleryClickListener{
    private static final String TAG = "GalleryFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private List<GalleryResultInfo.GalleryInfo> list;
    private GalleryAdapter adapter;

    private BigImageDialog bigImageDialog;

    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.GALLERY_POSITOTN_ID, 0, 0);
        initView();
        initDialig();
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
        adapter = new GalleryAdapter(getContext(), list);
        ScreenUtils screenUtils = ScreenUtils.instance(getContext());
        int space = (int) screenUtils.dip2px(3);
        CustomItemDecotation itemDecotation = new CustomItemDecotation(space, space, 3, true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.addItemDecoration(itemDecotation);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnGalleryClickListener(this);
    }

    /**
     * 获取数据
     */
    private void getData(final boolean isShowLoading) {
        if (!NetWorkUtils.isNetworkConnected(getContext())) {
            if (isShowLoading) {
                statusViewLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
            return;
        }

        HashMap<String, String> params = API.createParams();
        OkHttpUtils.get().url(API.URL_PRE + API.GALLERY).params(params).tag(TAG).build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                if (isShowLoading) {
                    statusViewLayout.showLoading();
                }
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
                if (isShowLoading) {
                    statusViewLayout.showNetError(retryListener);
                } else {
                    ptrLayout.refreshComplete();
                }
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    GalleryResultInfo info = JSON.parseObject(s, GalleryResultInfo.class);
                    list.clear();
                    list.addAll(info.getData());
                    adapter.notifyDataSetChanged();
                    if (isShowLoading) {
                        statusViewLayout.showContent();
                    } else {
                        ptrLayout.refreshComplete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (isShowLoading) {
                        statusViewLayout.showFailed(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

            }
        });

    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getData(true);
        }
    };


    private void initDialig() {
        bigImageDialog = new BigImageDialog(getContext(), R.style.Dialog);
    }

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(TAG);
        super.onDestroyView();
    }

    @Override
    public void onGalleryClick(int position) {
        bigImageDialog.setUrl(list.get(position).getThumb());
        bigImageDialog.show();
    }
}
