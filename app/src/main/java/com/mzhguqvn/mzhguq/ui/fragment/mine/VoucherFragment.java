package com.mzhguqvn.mzhguq.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.VoucherAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
import com.mzhguqvn.mzhguq.bean.VoucherInfo;
import com.mzhguqvn.mzhguq.bean.VoucherListResultInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.event.ChoosedVoucherBackEvent;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.util.API;
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
 * Case By:我的代金券
 * package:com.mzhguqvn.mzhguq.ui.fragment.mine
 * Author：scene on 2017/5/25 09:31
 */

public class VoucherFragment extends BaseBackFragment {
    private static final String TAG = "VoucherFragment";
    private static final String ARG_ENTER_PAGE = "arg_enter_page";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;

    private List<VoucherInfo> list;
    private VoucherAdapter adapter;

    private int enterPage = 0;

    public static VoucherFragment newInstance(int enterPage) {
        VoucherFragment fragment = new VoucherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ENTER_PAGE, enterPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            enterPage = args.getInt(ARG_ENTER_PAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbarNav(toolbar);
        toolbarTitle.setText("代金券");
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
        getData(true);
        MainActivity.upLoadPageInfo(PageConfig.MY_VOUCHER_POSITION_ID, 0, 0);
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
        adapter = new VoucherAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.fragment_voucher_header, null));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (enterPage !=0) {
                    EventBus.getDefault().post(new ChoosedVoucherBackEvent(list.get(position - 1), enterPage));
                    pop();
                }
            }
        });
    }

    /**
     * Case By:获取数据
     * Author: scene on 2017/5/10 17:55
     *
     * @param isShowLoading 是否显示加载中
     */
    private void getData(final boolean isShowLoading) {
        if (isShowLoading) {
            statusViewLayout.showLoading();
        }
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            HashMap<String, String> params = API.createParams();
            params.put("user_id", String.valueOf(App.user_id));
            OkHttpUtils.get().url(API.URL_PRE + API.VOUCHER).params(params).tag(TAG).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (isShowLoading) {
                        statusViewLayout.showFailed(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        VoucherListResultInfo resultInfo = JSON.parseObject(s, VoucherListResultInfo.class);
                        list.clear();
                        list.addAll(resultInfo.getData());
                        adapter.notifyDataSetChanged();
                        if (isShowLoading) {
                            statusViewLayout.showContent();
                        } else {
                            ptrLayout.refreshComplete();
                        }
                        if (list.size() == 0) {
                            statusViewLayout.showNone(retryListener);
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


        } else {
            if (isShowLoading) {
                statusViewLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
        }
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getData(true);
        }
    };

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(TAG);
        super.onDestroyView();
    }
}
