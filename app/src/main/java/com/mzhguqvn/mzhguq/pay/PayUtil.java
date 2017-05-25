package com.mzhguqvn.mzhguq.pay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.AliPayActivity;
import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.VideoDetailActivity;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.bean.CreateGoodsOrderInfo;
import com.mzhguqvn.mzhguq.bean.PayTokenResultInfo;
import com.mzhguqvn.mzhguq.ui.dialog.WxQRCodePayDialog;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DialogUtil;
import com.mzhguqvn.mzhguq.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

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


    //开通黄金会员 3800
    private static final int VIP_MONEY_TYPE_1 = 1;
    //优惠开通黄金会员 2800
    private static final int VIP_MONEY_TYPE_2 = 4;
    //直接开通钻石会员 6800
    private static final int VIP_MONEY_TYPE_3 = 1;
    //升级钻石会员 3000
    private static final int VIP_MONEY_TYPE_4 = 1;
    //开通CDN海外会员 2800
    private static final int VIP_MONEY_TYPE_5 = 1;
    //开通黄金永久会员 6800
    private static final int VIP_MONEY_TYPE_6 = 2;
    //优惠开通黄金永久会员 5800
    private static final int VIP_MONEY_TYPE_7 = 3;

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
     * @param type              开通的服务类型
     * @param videoId           视频id
     * @param isVideoDetailPage 当前是否在视频详情页
     */
    public void payByWeChat(Context context, int type, int videoId, boolean isVideoDetailPage, int pay_position_id) {
        getOrderNo(context, type, true, videoId, isVideoDetailPage, pay_position_id);
    }

    /**
     * 支付宝去支付
     *
     * @param context           上下文
     * @param type              要开通的服务类型
     * @param videoId           视频id
     * @param isVideoDetailPage 当前是否在视频详情页
     */
    public void payByAliPay(Context context, int type, int videoId, boolean isVideoDetailPage, int pay_position_id) {
        getOrderNo(context, type, false, videoId, isVideoDetailPage, pay_position_id);
    }

    /**
     * Case By:
     * Author: scene on 2017/5/10 10:33
     *
     * @param context 上下文
     * @param info    实体
     * @param payType 支付类型
     */
    public void buyGoods2Pay(Context context, CreateGoodsOrderInfo info, int payType, boolean isGoodsBuyPage) {
        getOrder4BuyGoods(context, info, payType, isGoodsBuyPage);
    }


    /**
     * 从服务器获取订单号
     *
     * @param context           上下文
     * @param type              1：vip，2：加速服务
     * @param isWechat          支付类型true：微信，false：支付宝
     * @param video_id          视频id
     * @param isVideoDetailPage 当前是否在视频详情页
     */
    private void getOrderNo(final Context context, final int type, final boolean isWechat, final int video_id, final boolean isVideoDetailPage, int pay_position_id) {
        App.isGoodsPay = false;
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        dialog = ProgressDialog.show(context, "", "订单提交中...");
        Map<String, String> params = API.createParams();
        switch (type) {
            case 1:
                params.put("money", String.valueOf(VIP_MONEY_TYPE_1));
                params.put("title", "开通会员");
                break;
            case 2:
                params.put("money", String.valueOf(VIP_MONEY_TYPE_2));
                params.put("title", "优惠开通会员");
                break;
            case 3:
                params.put("money", String.valueOf(VIP_MONEY_TYPE_3));
                params.put("title", "直接开通钻石会员");
                break;
            case 4:
                params.put("money", String.valueOf(VIP_MONEY_TYPE_4));
                params.put("title", "升级钻石会员");
                break;
            case 5:
                params.put("money", String.valueOf(VIP_MONEY_TYPE_5));
                params.put("title", "开通CDN加速服务");
                break;
            case 6:
                params.put("money", String.valueOf(VIP_MONEY_TYPE_6));
                params.put("title", "开通永久会员");
                break;
            case 7:
                params.put("money", String.valueOf(VIP_MONEY_TYPE_7));
                params.put("title", "优惠开通永久会员");
                break;
        }
        params.put("video_id", String.valueOf(video_id));
        params.put("position_id", String.valueOf(pay_position_id));
        params.put("pay_type", isWechat ? "1" : "2");
        params.put("type", String.valueOf((type == 6 || type == 7) ? 1 : type));

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
                try {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    final PayTokenResultInfo info = JSON.parseObject(s, PayTokenResultInfo.class);
                    if (info.getPay_type() == 1) {
                        //微信扫码
                        WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, info.getCode_img_url());
                        WxQRCodePayDialog wxQRCodePayDialog = builder.create();
                        wxQRCodePayDialog.show();
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                        DialogUtil.getInstance().showCustomSubmitDialog(context, "支付二维码已经保存到您的相册，请前往微信扫一扫付费");
                    } else if (info.getPay_type() == 2) {
                        //支付宝wap
                        Intent intent = new Intent(context, AliPayActivity.class);
                        intent.putExtra(AliPayActivity.ALIPAY_URL, info.getPay_url());
                        context.startActivity(intent);
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                    } else {
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
     * Case By:购买商品下单去支付
     * Author: scene on 2017/5/10 10:36
     *
     * @param context        上下文
     * @param info           订单信息
     * @param payType        支付类型1：微信，2：支付宝
     * @param isGoodsBuyPage 是否在单独的下单页去支付的
     */
    private void getOrder4BuyGoods(final Context context, CreateGoodsOrderInfo info, final int payType, final boolean isGoodsBuyPage) {
        App.isGoodsPay = true;
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        dialog = ProgressDialog.show(context, "", "订单提交中...");
        HashMap<String, String> params = API.createParams();
        params.put("goods_id", String.valueOf(info.getGoods_id()));
        params.put("user_id", String.valueOf(info.getUser_id()));
        params.put("number", String.valueOf(info.getNumber()));
        params.put("remark", String.valueOf(info.getRemark()));
        params.put("pay_type", String.valueOf(payType));
        params.put("mobile", info.getMobile());
        params.put("name", info.getName());
        params.put("address", info.getAddress());
        params.put("province", info.getProvince());
        params.put("city", info.getCity());
        params.put("area", info.getArea());
        params.put("voucher_id", String.valueOf(info.getVoucher_id()));
        OkHttpUtils.post().url(API.URL_PRE + API.GOODS_CREATE_ORDER).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtils.getInstance(context).showToast("购买失败，请重试");
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    final PayTokenResultInfo info = JSON.parseObject(s, PayTokenResultInfo.class);
                    App.order_id = info.getOrder_id();
                    //调用客户端
                    if (info.getPay_type() == 1) {
                        //微信扫码
                        WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, info.getCode_img_url());
                        WxQRCodePayDialog wxQRCodePayDialog = builder.create();
                        wxQRCodePayDialog.show();
                        App.isNeedCheckOrder = true;
                        App.goodsOrderId = info.getOrder_id_int();
                        App.isGoodsBuyPage = isGoodsBuyPage;
                        DialogUtil.getInstance().showCustomSubmitDialog(context, "支付二维码已经保存到您的相册，请前往微信扫一扫付费");
                    } else if (info.getPay_type() == 2) {
                        //支付宝wap
                        Intent intent = new Intent(context, AliPayActivity.class);
                        intent.putExtra(AliPayActivity.ALIPAY_URL, info.getPay_url());
                        context.startActivity(intent);
                        App.isNeedCheckOrder = true;
                        App.isGoodsBuyPage = isGoodsBuyPage;
                        App.goodsOrderId = info.getOrder_id_int();
                    } else {
                        ToastUtils.getInstance(context).showToast("购买失败，请重试");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.getInstance(context).showToast("购买失败，请重试");
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

    /**
     * Case By:检查订单状态
     * Author: scene on 2017/5/8 10:34
     *
     * @param context        上下文
     * @param isGoodsBuyPage 是否是单独的商品页来支付的
     */
    private void checkGoodsOrder(Context context, boolean isGoodsBuyPage) {
        Intent intent = new Intent(MainActivity.ACTION_NAME_MAINACTIVITY_CHECK_ORDER);
        intent.putExtra("IS_GOODS", true);
        intent.putExtra("IS_GOODS_BUY_PAGE", isGoodsBuyPage);
        context.sendBroadcast(intent);
    }

}
