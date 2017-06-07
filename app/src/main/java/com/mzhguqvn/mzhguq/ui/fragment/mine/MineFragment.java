package com.mzhguqvn.mzhguq.ui.fragment.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.VideoDetailActivity;
import com.mzhguqvn.mzhguq.adapter.IndexItemAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.VideoInfo;
import com.mzhguqvn.mzhguq.bean.VideoRelateResultInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.event.StartBrotherEvent;
import com.mzhguqvn.mzhguq.ui.dialog.CustomSubmitDialog;
import com.mzhguqvn.mzhguq.ui.view.CustomeGridView;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DialogUtil;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by scene on 2017/3/13.
 * 我的
 */

public class MineFragment extends BaseMainFragment {
    private static final String HOT_RECOMMEND_TAG = "hot_recommend_tag";
    @BindView(R.id.vip_id)
    TextView vipId;
    @BindView(R.id.open_vip)
    ImageView openVip;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.password)
    TextView password;
    @BindView(R.id.gridView)
    CustomeGridView gridView;

    private IndexItemAdapter adapter;
    private List<VideoInfo> list;

    public static MineFragment newInstance() {

        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        MainActivity.upLoadPageInfo(PageConfig.MINE_POSITOTN_ID, 0, 0);
    }

    private void initView() {
        switch (App.role) {
            case 0:
                vipId.setText("游客ID" + App.user_id);
                break;
            case 1:
            case 2:
                vipId.setText("黄金会员ID" + App.user_id);
                break;
            case 3:
            case 4:
                vipId.setText("钻石会员ID" + App.user_id);
                break;
        }
        if (App.role == 0) {
            openVip.setImageResource(R.drawable.ic_mine_open_vip);
        } else {
            openVip.setImageResource(R.drawable.ic_mine_update_vip);
        }
        if (App.role <= 2) {
            openVip.setVisibility(View.VISIBLE);
        } else {
            openVip.setVisibility(View.GONE);
        }
        account.setText("ac00" + (App.user_id + 235));
        password.setText("qdacp1pd5");

        list = new ArrayList<>();
        adapter = new IndexItemAdapter(getContext(), list);
        gridView.setAdapter(adapter);
        getHotRecommendVideo();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, list.get(position));
                intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_FROM, PageConfig.GLOD_POSITOTN_ID);
                startActivity(intent);
            }
        });
    }

    /**
     * 开通Vip
     */
    @OnClick(R.id.open_vip)
    public void onClickOpenVip() {
        switch (App.role) {
            case 0:
                DialogUtil.getInstance().showGoldVipDialog(getContext(), 0, PageConfig.MINE_POSITOTN_ID);
                break;
            case 1:
            case 2:
                DialogUtil.getInstance().showDiamondVipDialog(getContext(), 0, PageConfig.MINE_POSITOTN_ID);
                break;
        }
    }

    /**
     * 观看记录,我的收藏，离线视频
     */
    @OnClick({R.id.shoucang, R.id.download, R.id.lishi})
    public void onClick(View view) {
        if (App.role == 0) {
            DialogUtil.getInstance().showGoldVipDialog(getContext(), 0, PageConfig.MINE_POSITOTN_ID);
        }
    }

    @OnClick({R.id.my_discount, R.id.system_message})
    public void onClick2EmptyPage(View view) {
        switch (view.getId()) {
            case R.id.my_discount:
                EventBus.getDefault().post(new StartBrotherEvent(EmptyContentFragment.newInstance(EmptyContentFragment.TYPE_MY_DISCOUNT)));
                break;
            case R.id.system_message:
                EventBus.getDefault().post(new StartBrotherEvent(EmptyContentFragment.newInstance(EmptyContentFragment.TYPE_SYSTEM_MESSAGE)));
                break;
        }
    }


    /**
     * Case By:点击我的订单
     * Author: scene on 2017/5/9 11:03
     */
    @OnClick(R.id.order)
    public void onClickOrder() {
        EventBus.getDefault().post(new StartBrotherEvent(OrderFragment.newInstance()));
    }

    /**
     * 用户协议
     */
    @OnClick({R.id.xieyi})
    public void onClickAgreementAndDisclaime(View view) {
        switch (view.getId()) {
            case R.id.xieyi:
                EventBus.getDefault().post(new StartBrotherEvent(AgreementFragment.newInstance(AgreementFragment.TYPE_AGREEMENT)));
                break;
        }
    }

    @OnClick(R.id.voucher)
    public void onClickVoucher() {
        EventBus.getDefault().post(new StartBrotherEvent(VoucherFragment.newInstance(0)));
    }

    /**
     * 检查更新,清除缓存
     */
    @OnClick({R.id.update, R.id.huancun})
    public void onClickCheckUpdate(View view) {
        if (view.getId() == R.id.update) {
            CustomSubmitDialog.Builder builder = new CustomSubmitDialog.Builder(_mActivity);
            builder.setMessage("当前已经是最新版本");
            builder.setButtonText("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            CustomSubmitDialog.Builder builder = new CustomSubmitDialog.Builder(_mActivity);
            builder.setMessage("缓存清理成功");
            builder.setButtonText("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

    }

    /**
     * 投诉热线
     */
    @OnClick(R.id.tousu)
    public void onClickHotLine() {
        EventBus.getDefault().post(new StartBrotherEvent(HotLineFragment.newInstance()));
    }

    /**
     * 获取热门推荐
     */
    private void getHotRecommendVideo() {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            HashMap<String, String> params = API.createParams();
            params.put("video_id", String.valueOf(1));
            params.put("layout_id", String.valueOf(PageConfig.GLOD_POSITOTN_ID));
            OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_RELATED).params(params).tag(HOT_RECOMMEND_TAG).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    e.printStackTrace();
                    try {
                        gridView.setVisibility(View.GONE);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        VideoRelateResultInfo relateResultInfo = JSON.parseObject(s, VideoRelateResultInfo.class);
                        if (relateResultInfo.getData().size() > 3) {
                            list.clear();
                            for (int j = 0; j < 3; j++) {
                                list.add(relateResultInfo.getData().get(j));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        gridView.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        gridView.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            gridView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(HOT_RECOMMEND_TAG);
        super.onDestroyView();
    }
}
