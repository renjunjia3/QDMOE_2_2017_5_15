package com.hfaufhreu.hjfeuio.pay;

import android.app.ProgressDialog;
import android.content.Context;

import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.config.PayConfig;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.SharedPreferencesUtil;
import com.hfaufhreu.hjfeuio.util.ToastUtils;
import com.lessen.paysdk.pay.PayCallBack;
import com.lessen.paysdk.pay.PayTool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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

    //开通黄金会员
    private static final int VIP_MONEY_TYPE_1 = 3800;
    //优惠开通黄金会员
    private static final int VIP_MONEY_TYPE_2 = 2800;
    //直接开通钻石会员
    private static final int VIP_MONEY_TYPE_3 = 6800;
    //升级钻石会员
    private static final int VIP_MONEY_TYPE_4 = 3000;
    //开通VPN海外会员
    private static final int VIP_MONEY_TYPE_5 = 2800;
    //开通海外片库
    private static final int VIP_MONEY_TYPE_6 = 1900;
    //开通黑金会员
    private static final int VIP_MONEY_TYPE_7 = 4800;
    //开通海外加速通道
    private static final int VIP_MONEY_TYPE_8 = 1500;
    //开通海外急速双线通道
    private static final int VIP_MONEY_TYPE_9 = 1000;

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
     * @param context 上下文
     * @param type    开通的服务类型
     * @param videoId 视频id
     */
    public void payByWeChat(final Context context, int type, int videoId) {
        getOrderNo(context, type, true, videoId);
    }

    /**
     * 支付宝去支付
     *
     * @param context 上下文
     * @param type    要开通的服务类型
     * @param videoId 视频id
     */
    public void payByAliPay(final Context context, int type, int videoId) {
        getOrderNo(context, type, false, videoId);
    }

    /**
     * 从服务器获取订单号
     *
     * @param context  上下文
     * @param type     1：vip，2：加速服务
     * @param isWechat 支付类型true：微信，false：支付宝
     * @param video_id 视频id
     */
    private void getOrderNo(final Context context, final int type, final boolean isWechat, int video_id) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        } else {
            dialog = ProgressDialog.show(context, "", "订单提交中...");
        }
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
                            if (i == 0) {
                                ToastUtils.getInstance(context).showToast("支付成功");
                                SharedPreferencesUtil.putInt(context, App.ISVIP_KEY, type);
                                App.isVip = type;
                            } else {
                                ToastUtils.getInstance(context).showToast("支付失败");
                            }
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
