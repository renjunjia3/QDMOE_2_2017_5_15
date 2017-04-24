package com.cxbzcye.eolnz.ui.fragment.magnet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.cxbzcye.eolnz.R;
import com.cxbzcye.eolnz.adapter.SearchAdapter;
import com.cxbzcye.eolnz.app.App;
import com.cxbzcye.eolnz.base.BaseFragment;
import com.cxbzcye.eolnz.base.SearchInfo;
import com.cxbzcye.eolnz.util.API;
import com.cxbzcye.eolnz.util.DialogUtil;
import com.cxbzcye.eolnz.util.NetWorkUtils;
import com.cxbzcye.eolnz.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By: 磁力链结果页
 * package:
 * Author：scene on 2017/4/18 11:43
 */
public class MagnetResultFragment extends BaseFragment {
    public static final String PARAMS_SEARCH_TAG_POSITION = "params_search_tag_position";
    public static final String PARAMS_SEARCH_TAG_KEYWORD = "params_search_tag_keyword";
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.search)
    EditText search;

    //内容
    private SearchAdapter adapter;
    private List<SearchInfo> lists;

    //标签下标
    private int position = -1;
    private String keyWord = "";


    private RequestCall requestCall;

    public static MagnetResultFragment newInstance(int position, String keyWord) {
        MagnetResultFragment fragment = new MagnetResultFragment();
        Bundle args = new Bundle();
        args.putInt(PARAMS_SEARCH_TAG_POSITION, position);
        args.putString(PARAMS_SEARCH_TAG_KEYWORD, keyWord);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt(PARAMS_SEARCH_TAG_POSITION);
            keyWord = args.getString(PARAMS_SEARCH_TAG_KEYWORD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        if (App.isVip == 0) {
            addFooter();
        }
        searchLayout.setBackgroundColor(getResources().getColor(R.color.black));
        if (position != -1) {
            switch (position) {
                case 0:
                    lists = JSON.parseArray(getString(R.string.search_tag_1), SearchInfo.class);
                    break;
                case 1:
                    lists = JSON.parseArray(getString(R.string.search_tag_2), SearchInfo.class);
                    break;
                case 2:
                    lists = JSON.parseArray(getString(R.string.search_tag_3), SearchInfo.class);
                    break;
                case 3:
                    lists = JSON.parseArray(getString(R.string.search_tag_4), SearchInfo.class);
                    break;
                case 4:
                    lists = JSON.parseArray(getString(R.string.search_tag_5), SearchInfo.class);
                    break;
                case 5:
                    lists = JSON.parseArray(getString(R.string.search_tag_6), SearchInfo.class);
                    break;
                case 6:
                    lists = JSON.parseArray(getString(R.string.search_tag_7), SearchInfo.class);
                    break;
                case 7:
                    lists = JSON.parseArray(getString(R.string.search_tag_8), SearchInfo.class);
                    break;
            }
            statusViewLayout.showContent();
        } else {
            lists = new ArrayList<>();
        }
        adapter = new SearchAdapter(getContext(), lists);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (App.isVip == 0) {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为会员功能，请成为会员后使用", App.isVip, true);
                } else if (App.isVip == 1) {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为钻石会员功能，请升级钻石会员后使用", App.isVip, true);
                } else {
                    ToastUtils.getInstance(getContext()).showToast("该功能完善中，敬请期待");
                }
            }
        });
        if (position == -1) {
            getData();
        }
    }

    private void addFooter() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_footer_view, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.isVip == 0) {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为会员功能，请成为会员后使用", App.isVip, true);
                } else if (App.isVip == 1) {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为钻石会员功能，请升级钻石会员后使用", App.isVip, true);
                } else {
                    ToastUtils.getInstance(getContext()).showToast("该功能完善中，敬请期待");
                }
            }
        });
        listview.addFooterView(v);
    }

    @OnClick(R.id.btn_search)
    public void onClickSearch() {
        if (search.getText().toString().trim().isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请输入你要搜索的关键词");
        } else {
            hideSoftInput();
            keyWord = search.getText().toString().trim();
            getData();
        }
    }

    @Override
    public void onDestroyView() {
        if (requestCall != null) {
            requestCall.cancel();
        }
        super.onDestroyView();
    }


    private void getData() {
        if (!NetWorkUtils.isNetworkConnected(getContext())) {
            statusViewLayout.showNetError(retryListener);
            return;
        }
        statusViewLayout.showLoading();
        requestCall = OkHttpUtils.get().url(API.URL_PRE + API.MAGNET + keyWord).build();
        requestCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
                if (statusViewLayout != null) {
                    statusViewLayout.showFailed(retryListener);
                }
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    lists.clear();
                    lists.addAll(JSON.parseArray(s, SearchInfo.class));
                    adapter.notifyDataSetChanged();
                    statusViewLayout.showContent();
                } catch (Exception e) {
                    e.printStackTrace();
                    statusViewLayout.showFailed(retryListener);
                }
            }
        });
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getData();
        }
    };

    @Override
    public void onPause() {
        hideSoftInput();
        super.onPause();
    }
}
