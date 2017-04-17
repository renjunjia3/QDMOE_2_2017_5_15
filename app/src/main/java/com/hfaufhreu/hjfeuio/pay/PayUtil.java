package com.hfaufhreu.hjfeuio.pay;

import android.app.ProgressDialog;
import android.content.Context;

import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.config.PayConfig;
import com.hfaufhreu.hjfeuio.util.API;
import fm.jiecao.jcvideoplayer_lib.SharedPreferencesUtil;
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
 * 支付
 * Created by scene on 2017/3/20.
 */
public class PayUtil {
    private ProgressDialog dialog;
    private static int VIP_MONEY = 3800;
    private static int VIP_MONEY_2 = 3800;
    private static int SPEED_MONEY = 1800;
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
     * @param type    支付钱的多少和名称1：VIp，2：加速服务
     * @param videoId 视频id
     */
    public void payByWeChat(final Context context, int type, int videoId) {
        getOrderNo(context, type, true, videoId);
    }

    public void payByWeChat(final Context context, int type, int videoId, boolean isYouhui) {
        VIP_MONEY = VIP_MONEY_2;
        getOrderNo(context, type, true, videoId);
    }

    /**
     * 支付宝去支付
     *
     * @param context 上下文
     * @param videoId 视频id
     */
    public void payByAliPay(final Context context, int type, int videoId) {
        getOrderNo(context, type, false, videoId);
    }

    public void payByAliPay(final Context context, int type, int videoId, boolean isYouhui) {
        VIP_MONEY = VIP_MONEY_2;
        getOrderNo(context, type, false, videoId);
    }

    /**
     * 从服务器获取订单号
     *
     * @param context  上下文
     * @param type     1：vip，2：加速服务
     * @param isWechat 支付类型1：微信，2：支付宝
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
        params.put("price", type == 1 ? VIP_MONEY + "" : SPEED_MONEY + "");
        params.put("title", type == 1 ? "开通VIP" : "开通视频加速服务");
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
                                if (type == 1) {
                                    SharedPreferencesUtil.putInt(context, App.ISVIP_KEY, 1);
                                    App.ISVIP = 1;
                                } else {
                                    SharedPreferencesUtil.putInt(context, App.ISSPEED_KEY, 1);
                                    App.ISSPEED = 1;
                                }

                            } else {
                                ToastUtils.getInstance(context).showToast(s);
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
