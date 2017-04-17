package com.hfaufhreu.hjfeuio.ui.fragment.index;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.adapter.SearchAdapter;
import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.base.BaseFragment;
import com.hfaufhreu.hjfeuio.base.SearchInfo;
import com.hfaufhreu.hjfeuio.pay.PayUtil;
import com.hfaufhreu.hjfeuio.ui.dialog.FunctionPayDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/24.
 */

public class SearchResultFragment extends BaseFragment {
    public static final String PARAMS_SEARCH_TAG_POSITION = "params_search_tag_position";
    @BindView(R.id.listview)
    ListView listview;

    private FunctionPayDialog dialog;
    private FunctionPayDialog.Builder builder;

    //内容
    private SearchAdapter adapter;
    private List<SearchInfo> lists;

    //标签下标
    private int position = 0;


    public static SearchResultFragment newInstance(int position) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putInt(PARAMS_SEARCH_TAG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt(PARAMS_SEARCH_TAG_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog();
        return view;
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        if (App.ISVIP == 0) {
            addFooter();
        }
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

        adapter = new SearchAdapter(getContext(), lists);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.show();
            }
        });
    }

    private void initDialog() {
        builder = new FunctionPayDialog.Builder(_mActivity);
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

    private void addFooter() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_footer_view, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        listview.addFooterView(v);
    }

    @OnClick(R.id.search)
    public void onClickSearch() {
        dialog.show();
    }

    @OnClick(R.id.cancel)
    public void onClickCancel() {
        _mActivity.onBackPressed();
    }

}
