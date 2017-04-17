package com.hfaufhreu.hjfeuio.ui.fragment.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.base.BaseMainFragment;
import com.hfaufhreu.hjfeuio.event.StartBrotherEvent;
import com.hfaufhreu.hjfeuio.pay.PayUtil;
import com.hfaufhreu.hjfeuio.ui.dialog.CustomSubmitDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.FullVideoPayDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.FunctionPayDialog;
import com.hfaufhreu.hjfeuio.ui.fragment.MainFragment;
import com.hfaufhreu.hjfeuio.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by scene on 2017/3/13.
 * 我的
 */

public class MineFragment extends BaseMainFragment {
    @BindView(R.id.user_name)
    TextView userName;

    private FunctionPayDialog dialog;
    private FunctionPayDialog.Builder builder;

    private FullVideoPayDialog fullVideoPayDialog;
    private FullVideoPayDialog.Builder fullVideoPayBuilder;

    private Unbinder unbinder;

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
    }

    private void initView() {
        userName.setText("VIP" + App.USER_ID);
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


        fullVideoPayBuilder = new FullVideoPayDialog.Builder(_mActivity);
        fullVideoPayBuilder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fullVideoPayDialog.dismiss();
                PayUtil.getInstance().payByWeChat(_mActivity, 1, 0);
            }
        });

        fullVideoPayBuilder.setAliPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fullVideoPayDialog.dismiss();
                PayUtil.getInstance().payByAliPay(_mActivity, 1, 0);
            }
        });
        fullVideoPayDialog = fullVideoPayBuilder.create();

    }

    /**
     * 开通Vip
     */
    @OnClick(R.id.open_vip)
    public void onClickOpenVip() {
        if (App.ISVIP == 1) {
            ToastUtils.getInstance(_mActivity).showToast("您已经是VIP了");
        } else {
            fullVideoPayDialog.show();
            MainFragment.clickWantPay();
        }
    }

    /**
     * 观看记录,我的收藏，离线视频
     */
    @OnClick({R.id.view_record, R.id.fravoter, R.id.offline_video})
    public void onClick(View view) {
        if (App.ISVIP == 1) {
            ToastUtils.getInstance(_mActivity).showToast("该功能完善中，敬请期待");
        } else {
            dialog.show();
            MainFragment.clickWantPay();
        }
    }

    /**
     * 免责声明，用户协议
     */
    @OnClick({R.id.disclaime, R.id.agreement})
    public void onClickAgreementAndDisclaime(View view) {
        switch (view.getId()) {
            case R.id.agreement:
                EventBus.getDefault().post(new StartBrotherEvent(AgreementFragment.newInstance(AgreementFragment.TYPE_AGREEMENT)));
                break;
            case R.id.disclaime:
                EventBus.getDefault().post(new StartBrotherEvent(AgreementFragment.newInstance(AgreementFragment.TYPE_DISCLAIME)));
                break;
        }
    }

    /**
     * 检查更新
     */
    @OnClick(R.id.check_update)
    public void onClickCheckUpdate() {
        CustomSubmitDialog.Builder builder = new CustomSubmitDialog.Builder(_mActivity);
        builder.setTitle("检查更新");
        builder.setMessage("当前已经是最新版本");
        builder.setButtonText("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 投诉热线
     */
    @OnClick(R.id.hot_line)
    public void onClickHotLine() {
        EventBus.getDefault().post(new StartBrotherEvent(HotLineFragment.newInstance()));
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
