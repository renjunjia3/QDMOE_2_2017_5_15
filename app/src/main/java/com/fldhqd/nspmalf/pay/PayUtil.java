package com.fldhqd.nspmalf.pay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.fldhqd.nspmalf.AliPayActivity;
import com.fldhqd.nspmalf.MainActivity;
import com.fldhqd.nspmalf.VideoDetailActivity;
import com.fldhqd.nspmalf.app.App;
import com.fldhqd.nspmalf.bean.PayTokenResultInfo;
import com.fldhqd.nspmalf.config.PayConfig;
import com.fldhqd.nspmalf.ui.dialog.WxQRCodePayDialog;
import com.fldhqd.nspmalf.util.API;
import com.fldhqd.nspmalf.util.DialogUtil;
import com.fldhqd.nspmalf.util.ToastUtils;
import com.lessen.paysdk.pay.PayCallBack;
import com.lessen.paysdk.pay.PayTool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
    private static final int VIP_MONEY_TYPE_2 = 1;
    //直接开通钻石会员 6800
    private static final int VIP_MONEY_TYPE_3 = 1;
    //升级钻石会员 3000
    private static final int VIP_MONEY_TYPE_4 = 1;
    //开通VPN海外会员 2800
    private static final int VIP_MONEY_TYPE_5 = 1;
    //开通海外片库 1900
    private static final int VIP_MONEY_TYPE_6 = 1;
    //开通黑金会员 4800
    private static final int VIP_MONEY_TYPE_7 = 1;
    //开通海外加速通道 1500
    private static final int VIP_MONEY_TYPE_8 = 1;
    //开通海外急速双线通道 1000
    private static final int VIP_MONEY_TYPE_9 = 1;

//    //开通黄金会员 3800
//    private static final int VIP_MONEY_TYPE_1 = 10;
//    //优惠开通黄金会员 2800
//    private static final int VIP_MONEY_TYPE_2 = 10;
//    //直接开通钻石会员 6800
//    private static final int VIP_MONEY_TYPE_3 = 10;
//    //升级钻石会员 3000
//    private static final int VIP_MONEY_TYPE_4 = 10;
//    //开通VPN海外会员 2800
//    private static final int VIP_MONEY_TYPE_5 = 10;
//    //开通海外片库 1900
//    private static final int VIP_MONEY_TYPE_6 = 10;
//    //开通黑金会员 4800
//    private static final int VIP_MONEY_TYPE_7 = 10;
//    //开通海外加速通道 1500
//    private static final int VIP_MONEY_TYPE_8 = 10;
//    //开通海外急速双线通道 1000
//    private static final int VIP_MONEY_TYPE_9 = 10;

//    //开通黄金会员 3800
//    private static final int VIP_MONEY_TYPE_1 = 3800;
//    //优惠开通黄金会员 2800
//    private static final int VIP_MONEY_TYPE_2 = 2800;
//    //直接开通钻石会员 6800
//    private static final int VIP_MONEY_TYPE_3 = 6800;
//    //升级钻石会员 3000
//    private static final int VIP_MONEY_TYPE_4 = 3000;
//    //开通VPN海外会员 2800
//    private static final int VIP_MONEY_TYPE_5 = 2800;
//    //开通海外片库 1900
//    private static final int VIP_MONEY_TYPE_6 = 1900;
//    //开通黑金会员 4800
//    private static final int VIP_MONEY_TYPE_7 = 4800;
//    //开通海外加速通道 1500
//    private static final int VIP_MONEY_TYPE_8 = 1500;
//    //开通海外急速双线通道 1000
//    private static final int VIP_MONEY_TYPE_9 = 1000;

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
    private void getOrderNo(final Context context, final Dialog vipDialog, final int type, final boolean isWechat, final int video_id, final boolean isVideoDetailPage) {
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
                params.put("title", "wqeqweqweeqw");
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
                    final PayTokenResultInfo info = JSON.parseObject(s, PayTokenResultInfo.class);
                    //调用客户端
                    if (info.getType() == 1 || info.getType() == 2) {
                        final ProgressDialog progressDialog12 = new ProgressDialog(context);
                        progressDialog12.setMessage(info.getType() == 1 ? "正在为你跳转到微信" : "正在为你跳转到支付宝");
                        progressDialog12.show();
                        PayTool.payWork(context, isWechat ? PayTool.PayType.PAY_WX : PayTool.PayType.PAY_ALIPAY, info.getPayinfo(), new PayCallBack() {
                            @Override
                            public void onResult(int i, String s) {
                                if (progressDialog12 != null && progressDialog12.isShowing()) {
                                    progressDialog12.cancel();
                                }
                                //不管支付成功或者失败都检查一下支付结果
                                App.orderIdInt = info.getOrder_id_int();
                                checkOrder(context, isVideoDetailPage);
                            }
                        });
                    } else if (info.getType() == 3) {
                        //微信扫码
                        WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, info.getCode_img());
                        WxQRCodePayDialog wxQRCodePayDialog = builder.create();
                        wxQRCodePayDialog.show();
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                        DialogUtil.getInstance().showCustomSubmitDialog(context, "支付二维码已经保存到您的相册，请前往微信扫一扫付费");
                    } else if (info.getType() == 4) {
                        //支付宝wap
                        Intent intent = new Intent(context, AliPayActivity.class);
                        intent.putExtra(AliPayActivity.ALIPAY_URL, info.getPay_url());
                        context.startActivity(intent);
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                    }else{
                        ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
                }
            }
        });
    }

    /**
     * Case By:检查订单状态
     * Author: scene on 2017/5/8 10:34
     *
     * @param context           上下文
     * @param isVideoDetailPage 是否是视频详情页
     */
    private void checkOrder(Context context, boolean isVideoDetailPage) {
        if (isVideoDetailPage) {
            Intent intent = new Intent(VideoDetailActivity.ACTION_NAME_VIDEODETAILACTIVITY_CHECK_ORDER);
            context.sendBroadcast(intent);
        } else {
            Intent intent = new Intent(MainActivity.ACTION_NAME_MAINACTIVITY_CHECK_ORDER);
            context.sendBroadcast(intent);
        }
    }

}
