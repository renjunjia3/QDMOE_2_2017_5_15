package com.cyldf.cyldfxv.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.cyldf.cyldfxv.bean.PayResultInfo;
import com.cyldf.cyldfxv.config.PayConfig;
import com.sdky.jzp.SdkPay;
import com.sdky.jzp.data.CheckOrder;
import com.skpay.NINESDK;
import com.cyldf.cyldfxv.R;
import com.cyldf.cyldfxv.app.App;
import com.cyldf.cyldfxv.base.BaseFragment;
import com.cyldf.cyldfxv.event.StartBrotherEvent;
import com.cyldf.cyldfxv.event.TabSelectedEvent;
import com.cyldf.cyldfxv.pay.PayUtil;
import com.cyldf.cyldfxv.ui.dialog.FullVideoPayDialog;
import com.cyldf.cyldfxv.ui.fragment.actor.ActorFragment;
import com.cyldf.cyldfxv.ui.fragment.index.IndexFragment;
import com.cyldf.cyldfxv.ui.fragment.index.SearchFragment;
import com.cyldf.cyldfxv.ui.fragment.live.LiveFragment;
import com.cyldf.cyldfxv.ui.fragment.mine.HotLineFragment;
import com.cyldf.cyldfxv.ui.fragment.mine.MineFragment;
import com.cyldf.cyldfxv.ui.view.BottomBar;
import com.cyldf.cyldfxv.ui.view.BottomBarTab;
import com.cyldf.cyldfxv.util.API;
import fm.jiecao.jcvideoplayer_lib.SharedPreferencesUtil;
import com.cyldf.cyldfxv.util.ToastUtils;
import com.skpay.codelib.utils.encryption.MD5Encoder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;

/**
 * Created by scene on 2017/3/15.
 */

public class MainFragment extends BaseFragment {
    private static final int REQ_MSG = 10;

    public static final int INDEX = 0;
    public static final int ACTOR = 1;
    public static final int LIVE = 2;
    public static final int MINE = 3;

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    private SupportFragment[] mFragments = new SupportFragment[4];
    private String strTabs[] = {"首页", "人气女优", "女神直播", "我的"};


    private FullVideoPayDialog functionPayDialog;
    private FullVideoPayDialog.Builder builder;

    //短代支付
    public SdkPay sdkPay;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (savedInstanceState == null) {
            mFragments[INDEX] = IndexFragment.newInstance();
            mFragments[ACTOR] = ActorFragment.newInstance();
            mFragments[LIVE] = LiveFragment.newInstance();
            mFragments[MINE] = MineFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, INDEX,
                    mFragments[INDEX],
                    mFragments[ACTOR],
                    mFragments[LIVE],
                    mFragments[MINE]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[INDEX] = findChildFragment(IndexFragment.class);
            mFragments[ACTOR] = findChildFragment(ActorFragment.class);
            mFragments[LIVE] = findChildFragment(LiveFragment.class);
            mFragments[MINE] = findChildFragment(MineFragment.class);
        }
        unbinder = ButterKnife.bind(this, view);
        initView();
        getDuandaiToken();
        return view;
    }


    private void initView() {
        EventBus.getDefault().register(this);

        mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_index, strTabs[0]))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_actor, strTabs[1]))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_live, strTabs[2]))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_mine, strTabs[3]));


        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        NINESDK.exit(_mActivity, sdkPay);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @OnClick(R.id.complaint)
    public void onClickComplaint() {
        EventBus.getDefault().post(new StartBrotherEvent(HotLineFragment.newInstance()));
    }

    @OnClick({R.id.search, R.id.vip})
    public void onClickTop(View v) {
        if (v.getId() == R.id.search) {
            EventBus.getDefault().post(new StartBrotherEvent(SearchFragment.newInstance()));
        } else {
            if (App.ISVIP == 0) {
                if (builder == null) {
                    builder = new FullVideoPayDialog.Builder(_mActivity);
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
                }
                if (functionPayDialog == null) {
                    functionPayDialog = builder.create();
                }
                functionPayDialog.show();
                clickWantPay();
            } else {
                ToastUtils.getInstance(_mActivity).showToast("您已经是VIP了");
            }
        }

    }

    /**
     * 弹出支付窗口之后调用
     */
    public static void clickWantPay() {
        OkHttpUtils.get().url(API.URL_PRE + API.PAY_CLICK + App.IMEI).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s, int i) {

            }
        });
    }

    //初始化短代支付
    private static final String DUANDAI_PAY_KEY = "duan_dai_pay";
    private int paySuccessMoney = 0;
    private int time = 0;
    private boolean status = false;

    private void initDuanDaiPay() {
        sdkPay = NINESDK.init(_mActivity, "492", 10001, new SdkPay.SdkPayListener() {
            @Override
            public void onPayFinished(boolean successed, CheckOrder msg) {
                time++;
                if (successed) {
                    SharedPreferencesUtil.putBoolean(_mActivity, DUANDAI_PAY_KEY, true);
                    paySuccessMoney += 1000;
                    status = true;
                }
                if (time < 5) {
                    toDuandaiPay();
                } else {
                    sendSMSResult(successed, msg);
                }
            }
        });
        if (!SharedPreferencesUtil.getBoolean(_mActivity, DUANDAI_PAY_KEY, false)) {
            toDuandaiPay();
        }
    }

    //获取短代支付的信息
    private PayResultInfo resultInfo;

    private void getDuandaiToken() {
        Map<String, String> params = new TreeMap<>();
        params.put("imei", App.IMEI);
        params.put("version", PayConfig.VERSION_NAME);
        OkHttpUtils.post().url(API.URL_PRE + API.GET_DUANDAI_INFO).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    resultInfo = JSON.parseObject(s, PayResultInfo.class);
                    initDuanDaiPay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //吊起短代支付
    private void toDuandaiPay() {
        String cporderid = UUID.randomUUID().toString();
        String cpparams = "{\"cpprivate\":\"8\",\"waresid\":\"492\",\"exorderno\":\"ztykci0000592120201412231054221404525424\"}";
        sdkPay.pay(10, cporderid, cpparams);
    }

    //传递支付结果给服务器
    private void sendSMSResult(boolean successed, CheckOrder msg) {
        String sign = MD5Encoder.EncoderByMd5("492|" + resultInfo.getOrder_id() + "|" + paySuccessMoney + "|fcf35ab21bbc6304f0aa120945843ee1").toLowerCase();
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", paySuccessMoney + "");
        params.put("order_id", resultInfo.getOrder_id());
        params.put("out_trade_no", msg.orderid);
        params.put("status", status ? "1" : "0");
        params.put("sign", sign);
        OkHttpUtils.post().url(API.URL_PRE + API.NOTIFY_SMS).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {

            }
        });
    }

}
