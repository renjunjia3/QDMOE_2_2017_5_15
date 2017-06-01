package com.mzhguqvn.mzhguq.ui.fragment.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.event.StartBrotherEvent;
import com.mzhguqvn.mzhguq.ui.dialog.CustomSubmitDialog;
import com.mzhguqvn.mzhguq.util.DialogUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by scene on 2017/3/13.
 * 我的
 */

public class MineFragment extends BaseMainFragment {
    @BindView(R.id.vip_id)
    TextView vipId;
    @BindView(R.id.open_vip)
    ImageView openVip;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.password)
    TextView password;

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
                vipId.setText("黄金会员ID" + App.user_id);
                break;
        }
        if (App.role == 0) {
            openVip.setImageResource(R.drawable.ic_mine_open_vip);
        } else {
            openVip.setImageResource(R.drawable.ic_mine_update_vip);
        }
        if (App.role == 0) {
            openVip.setVisibility(View.VISIBLE);
        } else {
            openVip.setVisibility(View.GONE);
        }
        account.setText("ac00" + (App.user_id + 235));
        password.setText("qdacp1pd5");

    }

    /**
     * 开通Vip
     */
    @OnClick(R.id.open_vip)
    public void onClickOpenVip() {
        if (App.role == 0) {
            DialogUtil.getInstance().showGoldVipDialog(getContext(), 0, false, PageConfig.MINE_POSITOTN_ID);
        }
    }

    /**
     * 观看记录,我的收藏，离线视频
     */
    @OnClick({R.id.shoucang, R.id.download, R.id.lishi})
    public void onClick(View view) {
        if (App.role == 0) {
            DialogUtil.getInstance().showGoldVipDialog(getContext(), 0, false, PageConfig.MINE_POSITOTN_ID);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
