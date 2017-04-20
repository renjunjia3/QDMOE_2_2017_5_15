package com.hfaufhreu.hjfeuio.ui.fragment.magnet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.adapter.SearchAdapter;
import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.base.BaseMainFragment;
import com.hfaufhreu.hjfeuio.base.SearchInfo;
import com.hfaufhreu.hjfeuio.event.StartBrotherEvent;
import com.hfaufhreu.hjfeuio.util.DialogUtil;
import com.hfaufhreu.hjfeuio.util.ToastUtils;
import com.liangfeizc.flowlayout.FlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Case By: 磁力链首页
 * package:
 * Author：scene on 2017/4/18 11:42
 */
public class MagnetFragment extends BaseMainFragment {
    @BindView(R.id.listview)
    ListView listview;

    private String[] tags;
    //标签
    private View tagView;
    private TextView tag;
    //内容
    private SearchAdapter adapter;
    private List<SearchInfo> lists;

    public static MagnetFragment newInstance() {
        MagnetFragment fragment = new MagnetFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDialog();
        tags = getResources().getStringArray(R.array.search_tag);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        addHead();
        if (App.isVip == 0) {
            addFooter();
        }
        lists = JSON.parseArray(getString(R.string.str_json_seach), SearchInfo.class);
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

    private void addHead() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_header_view, null);
        listview.addHeaderView(v);
        addTagView(v);
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

    /**
     * Case By:加载tag
     * Author: scene on 2017/4/14 16:49
     */
    private void addTagView(View v) {
        FlowLayout flowLayout = (FlowLayout) v.findViewById(R.id.flow_layout);
        for (int i = 0; i < tags.length; i++) {
            tagView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_tag_item, null);
            tag = (TextView) tagView.findViewById(R.id.tag);
            tag.setText(tags[i]);
            final int finalI = i;
            tagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new StartBrotherEvent(MagnetResultFragment.newInstance(finalI)));
                }
            });
            flowLayout.addView(tagView);
        }
    }

    @OnClick({R.id.search, R.id.btn_search})
    public void onClickSearch() {
        if (App.isVip == 0) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为会员功能，请成为会员后使用", App.isVip, true);
        } else if (App.isVip == 1) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为钻石会员功能，请升级钻石会员后使用", App.isVip, true);
        } else {
            ToastUtils.getInstance(getContext()).showToast("该功能完善中，敬请期待");
        }
    }


}
