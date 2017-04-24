package com.tdhtzpkmo.pxzol.ui.fragment.magnet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.tdhtzpkmo.pxzol.R;
import com.tdhtzpkmo.pxzol.adapter.SearchAdapter;
import com.tdhtzpkmo.pxzol.app.App;
import com.tdhtzpkmo.pxzol.base.BaseFragment;
import com.tdhtzpkmo.pxzol.base.SearchInfo;
import com.tdhtzpkmo.pxzol.util.DialogUtil;
import com.tdhtzpkmo.pxzol.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Case By: 磁力链结果页
 * package:
 * Author：scene on 2017/4/18 11:43
 */
public class MagnetResultFragment extends BaseFragment {
    public static final String PARAMS_SEARCH_TAG_POSITION = "params_search_tag_position";
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;

    //内容
    private SearchAdapter adapter;
    private List<SearchInfo> lists;

    //标签下标
    private int position = 0;


    public static MagnetResultFragment newInstance(int position) {
        MagnetResultFragment fragment = new MagnetResultFragment();
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
        if (App.isVip == 0) {
            addFooter();
        }
        searchLayout.setBackgroundColor(getResources().getColor(R.color.black));
        btnSearch.setText("取消");
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
                if (App.isVip == 0) {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为会员功能，请成为会员后使用", App.isVip, true);
                } else if (App.isVip == 1) {
                    DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为钻石会员功能，请升级钻石会员后使用", App.isVip, true);
                } else {
                    ToastUtils.getInstance(getContext()).showToast("该功能完善中，敬请期待");
                }
            }
        });
    }

    private void initDialog() {

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

    @OnClick(R.id.search)
    public void onClickSearch() {
        if (App.isVip == 0) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为会员功能，请成为会员后使用", App.isVip, true);
        } else if (App.isVip == 1) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为钻石会员功能，请升级钻石会员后使用", App.isVip, true);
        } else {
            ToastUtils.getInstance(getContext()).showToast("该功能完善中，敬请期待");
        }
    }

    @OnClick(R.id.btn_search)
    public void onClickCancel() {
        _mActivity.onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
