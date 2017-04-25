package com.enycea.owlumguk.ui.fragment.magnet;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liangfeizc.flowlayout.FlowLayout;
import com.enycea.owlumguk.R;
import com.enycea.owlumguk.adapter.SearchAdapter;
import com.enycea.owlumguk.app.App;
import com.enycea.owlumguk.base.BaseMainFragment;
import com.enycea.owlumguk.bean.SearchInfo;
import com.enycea.owlumguk.event.StartBrotherEvent;
import com.enycea.owlumguk.ui.dialog.DownLoadDialog;
import com.enycea.owlumguk.util.DialogUtil;
import com.enycea.owlumguk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By: 磁力链首页
 * package:
 * Author：scene on 2017/4/18 11:42
 */
public class MagnetFragment extends BaseMainFragment implements SearchAdapter.OnClickDownloadListener {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private String[] tags;
    //标签
    private View tagView;
    private TextView tag;
    //内容
    private SearchAdapter adapter;
    private List<SearchInfo> lists;

    //下载
    private DownLoadDialog downLoadDialog;
    private DownLoadDialog.Builder builder;
    private MaterialProgressBar progressBar;

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
        statusViewLayout.showContent();
        addHead();
        if (App.isVip == 0) {
            addFooter();
        }
        lists = JSON.parseArray(getString(R.string.str_json_seach), SearchInfo.class);
        adapter = new SearchAdapter(getContext(), lists);
        listview.setAdapter(adapter);
        adapter.setOnClickDownloadListener(this);

    }

    private void initDialog() {
        builder = new DownLoadDialog.Builder(getContext());
        downLoadDialog = builder.create();
        progressBar = builder.getProgressBar();
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
                    EventBus.getDefault().post(new StartBrotherEvent(MagnetResultFragment.newInstance(finalI, "")));
                }
            });
            flowLayout.addView(tagView);
        }
    }

    @Override
    public void onPause() {
        hideSoftInput();
        super.onPause();
    }

    @OnClick({R.id.btn_search})
    public void onClickSearch() {
        hideSoftInput();
        if (search.getText().toString().trim().isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请输入你要搜索的关键词");
        } else {
            EventBus.getDefault().post(new StartBrotherEvent(MagnetResultFragment.newInstance(-1, search.getText().toString().trim())));
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * Case By:点击下载按钮
     * Author: scene on 2017/4/25 10:13
     */
    @Override
    public void onClickDownLoad(final int position) {
        if (progressBar.getProgress() != 0) {
            progressBar.setProgress(0);
        }
        if (downLoadDialog != null) {
            downLoadDialog.show();
        }
        new CountDownTimer(3000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (progressBar != null) {
                    progressBar.setProgress(progressBar.getProgress() + 20 < 100 ? progressBar.getProgress() + 20 : 100);
                }
            }

            @Override
            public void onFinish() {
                if (progressBar != null) {
                    progressBar.setProgress(100);
                }
                if (downLoadDialog != null && downLoadDialog.isShowing()) {
                    downLoadDialog.cancel();
                }
                lists.get(position).setShowPlay(true);
                adapter.notifyDataSetChanged();
            }
        }.start();
    }

    /**
     * Case By:点击播放按钮
     * Author: scene on 2017/4/25 10:13
     */
    @Override
    public void onClickPlay() {
        if (App.isVip == 0) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为会员功能，请成为会员后使用", App.isVip, true);
        } else if (App.isVip == 1) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该功能为钻石会员功能，请升级钻石会员后使用", App.isVip, true);
        } else {
            ToastUtils.getInstance(getContext()).showToast("该功能完善中，敬请期待");
        }
    }
}
