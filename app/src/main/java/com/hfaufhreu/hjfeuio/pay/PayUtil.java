package com.hfaufhreu.hjfeuio.pay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.config.PayConfig;
import com.hfaufhreu.hjfeuio.event.ChangeTabEvent;
import com.hfaufhreu.hjfeuio.event.CloseVideoDetailEvent;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.SharedPreferencesUtil;
import com.hfaufhreu.hjfeuio.util.ToastUtils;
import com.lessen.paysdk.pay.PayCallBack;
import com.lessen.paysdk.pay.PayTool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Case By: 支付工具类
 * package:
 * Author：scene on 2017/4/18 9:30
 */
public class PayUtil {
    private ProgressDialog dialog;

    //VIP的类型对应下面的描述
    public static final int VIP_TYPE_1 = 1;
    public static final int VIP_TYPE_2 = 2;
    public static final int VIP_TYPE_3 = 3;
    public static final int VIP_TYPE_4 = 4;
    public static final int VIP_TYPE_5 = 5;
    public static final int VIP_TYPE_6 = 6;
    public static final int VIP_TYPE_7 = 7;
    public static final int VIP_TYPE_8 = 8;
    public static final int VIP_TYPE_9 = 9;

    //开通黄金会员 3800
    private static final int VIP_MONEY_TYPE_1 = 1;
    //优惠开通黄金会员 2800
    private static final int VIP_MONEY_TYPE_2 = 2;
    //直接开通钻石会员 6800
    private static final int VIP_MONEY_TYPE_3 = 3;
    //升级钻石会员 3000
    private static final int VIP_MONEY_TYPE_4 = 4;
    //开通VPN海外会员 2800
    private static final int VIP_MONEY_TYPE_5 = 5;
    //开通海外片库 1900
    private static final int VIP_MONEY_TYPE_6 = 6;
    //开通黑金会员 4800
    private static final int VIP_MONEY_TYPE_7 = 7;
    //开通海外加速通道 1500
    private static final int VIP_MONEY_TYPE_8 = 8;
    //开通海外急速双线通道 1000
    private static final int VIP_MONEY_TYPE_9 = 9;

    private static PayUtil instance = null;

    public static PayUtil getInstance() {
        if (instance == null) {
            synchronized (PayUtil.class) {
                if (instance == null) {
                    instance = new PayUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 微信去支付
     *
     * @param context           上下文
     * @param dialog            对话框
     * @param type              开通的服务类型
     * @param videoId           视频id
     * @param isVideoDetailPage 当前是否在视频详情页
     */
    public void payByWeChat(Context context, Dialog dialog, int type, int videoId, boolean isVideoDetailPage) {
        getOrderNo(context, dialog, type, true, videoId, isVideoDetailPage);
    }

    /**
     * 支付宝去支付
     *
     * @param context           上下文
     * @param dialog            对话框
     * @param type              要开通的服务类型
     * @param videoId           视频id
     * @param isVideoDetailPage 当前是否在视频详情页
     */
    public void payByAliPay(Context context, Dialog dialog, int type, int videoId, boolean isVideoDetailPage) {
        getOrderNo(context, dialog, type, false, videoId, isVideoDetailPage);
    }

    /**
     * 从服务器获取订单号
     *
     * @param context           上下文
     * @param vipDialog         对话框
     * @param type              1：vip，2：加速服务
     * @param isWechat          支付类型true：微信，false：支付宝
     * @param video_id          视频id
     * @param isVideoDetailPage 当前是否在视频详情页
     */
    private void getOrderNo(final Context context, final Dialog vipDialog, final int type, final boolean isWechat, int video_id, final boolean isVideoDetailPage) {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        dialog = ProgressDialog.show(context, "", "订单提交中...");
        Map<String, String> params = new TreeMap<>();
        params.put("imei", App.IMEI);
        switch (type) {
            case 1:
                params.put("price", VIP_MONEY_TYPE_1 + "");
                params.put("title", "开通黄金会员");
                break;
            case 2:
                params.put("price", VIP_MONEY_TYPE_2 + "");
                params.put("title", "优惠开通黄金会员");
                break;
            case 3:
                params.put("price", VIP_MONEY_TYPE_3 + "");
                params.put("title", "直接开通钻石会员");
                break;
            case 4:
                params.put("price", VIP_MONEY_TYPE_4 + "");
                params.put("title", "升级钻石会员");
                break;
            case 5:
                params.put("price", VIP_MONEY_TYPE_5 + "");
                params.put("title", "开通VPN海外会员");
                break;
            case 6:
                params.put("price", VIP_MONEY_TYPE_6 + "");
                params.put("title", "开通海外片库");
                break;
            case 7:
                params.put("price", VIP_MONEY_TYPE_7 + "");
                params.put("title", "开通黑金会员");
                break;
            case 8:
                params.put("price", VIP_MONEY_TYPE_8 + "");
                params.put("title", "开通海外加速通道");
                break;
            case 9:
                params.put("price", VIP_MONEY_TYPE_9 + "");
                params.put("title", "开通海外急速双线通道");
                break;
        }
        params.put("video_id", video_id + "");
        params.put("type", type + "");
        params.put("version", PayConfig.VERSION_NAME);
        params.put("pay_type", isWechat ? "1" : "2");
        OkHttpUtils.post().url(API.URL_PRE + API.GET_ORDER_INFO_TYPE_2).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
            }

            @Override
            public void onResponse(String s, int i) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String payinfo = jsonObject.getString("payinfo");
                    PayTool.payWork(context, isWechat ? PayTool.PayType.PAY_WX : PayTool.PayType.PAY_ALIPAY, payinfo, new PayCallBack() {
                        @Override
                        public void onResult(int i, String s) {
//                            if (i == 0) {
                                int isvipType = 0;
                                switch (type) {
                                    case 1:
                                    case 2:
                                        isvipType = 1;
                                        ToastUtils.getInstance(context).showToast("恭喜您成为黄金会员");
                                        break;
                                    case 3:
                                    case 4:
                                        isvipType = 2;
                                        ToastUtils.getInstance(context).showToast("恭喜您成为钻石会员");
                                        break;
                                    case 5:
                                        isvipType = 3;
                                        ToastUtils.getInstance(context).showToast("恭喜您成功注册VPN海外会员");
                                        break;
                                    case 6:
                                        isvipType = 4;
                                        ToastUtils.getInstance(context).showToast("恭喜你进入海外片库，我们将携手为您服务");
                                        break;
                                    case 7:
                                        isvipType = 5;
                                        ToastUtils.getInstance(context).showToast("恭喜您成为最牛逼的黑金会员");
                                        break;
                                    case 8:
                                        isvipType = 6;
                                        ToastUtils.getInstance(context).showToast("恭喜您开通海外高速通道");
                                        break;
                                    case 9:
                                        isvipType = 7;
                                        ToastUtils.getInstance(context).showToast("恭喜您开通海外双线通道");
                                        break;
                                    default:
                                        break;
                                }

                                SharedPreferencesUtil.putInt(context, App.ISVIP_KEY, isvipType);
                                App.isVip = isvipType;
                                if (vipDialog != null) {
                                    vipDialog.cancel();
                                }
                                if (isVideoDetailPage) {
                                    EventBus.getDefault().post(new CloseVideoDetailEvent());
                                } else {
                                    EventBus.getDefault().post(new ChangeTabEvent(isvipType));
                                }
//                            } else {
//                                ToastUtils.getInstance(context).showToast("支付失败");
//                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
                }
            }
        });
    }

}
