package com.mzhguqvn.mzhguq.pay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.AliPayActivity;
import com.mzhguqvn.mzhguq.WechatPayActivity;
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
    // [1 => '开通黄金会员', 2 => '优惠开通黄金会员', 3 => '优惠开通包年黄金会员', 4 => '开通包年黄金会员',
    // 5 => '直接开通钻石会员', 6 => '升级钻石会员', 7 => '直接开通包年钻石会员', 8 => '升级包年钻石会员',9=>'包年黄金会员升级包年钻石会员']
    public static final int VIP_TYPE_OPEN_GLOD_MONTH = 1;
    public static final int VIP_TYPE_OPEN_GLOD_DISCOUNT_MONTH = 2;
    public static final int VIP_TYPE_OPEN_GLOD_DISCOUNT_YEAR = 3;
    public static final int VIP_TYPE_OPEN_GLOD_YEAR = 4;
    public static final int VIP_TYPE_OPEN_DIAMOND_MONTH = 5;
    public static final int VIP_TYPE_UPDATE_DIAMOND_MONTH = 6;
    public static final int VIP_TYPE_OPEN_DIAMOND_YEAR = 7;
    public static final int VIP_TYPE_UPDATE_DIAMOND_YEAR_FROM_MONTH = 8;
    public static final int VIP_TYPE_UPDATE_DIAMOND_YEAR_FROM_YEAR = 9;


//    //开通包月黄金会员 3800
//    private static final int VIP_MONEY_OPEN_GLOD_MONTH = 1;
//    //优惠开通包月黄金会员 2800
//    private static final int VIP_MONEY_OPEN_GLOD_DISCOUNT_MONTH = 2;
//    //优惠开通包年黄金会员 5800
//    private static final int VIP_MONEY_OPEN_GLOD_DISCOUNT_YEAR = 3;
//    //开通包年黄金会员 6800
//    private static final int VIP_MONEY_OPEN_GLOD_YEAR = 4;
//    //直接开通包月钻石会员 6800
//    private static final int VIP_MONEY_OPEN_DIAMOND_MONTH = 5;
//    //升级包月钻石会员 3000
//    private static final int VIP_MONEY_UPDATE_DIAMOND_MONTH = 6;
//    //直接开通包年钻石会员 9800
//    private static final int VIP_MONEY_OPEN_DIAMOND_YEAR = 7;
//    //黄金包月升级包年钻石会员 6000
//    private static final int VIP_MONEY_UPDATE_DIAMOND_YEAR_FROM_MONTH = 8;
//    //黄金包年升级包年钻石 3000
//    private static final int VIP_MONEY_UPDATE_DIAMOND_YEAR_FROM_YEAR = 9;

    //开通包月黄金会员 3800
    private static final int VIP_MONEY_OPEN_GLOD_MONTH = 3800;
    //优惠开通包月黄金会员 2800
    private static final int VIP_MONEY_OPEN_GLOD_DISCOUNT_MONTH = 2800;
    //优惠开通包年黄金会员 5800
    private static final int VIP_MONEY_OPEN_GLOD_DISCOUNT_YEAR = 5800;
    //开通包年黄金会员 6800
    private static final int VIP_MONEY_OPEN_GLOD_YEAR = 6800;
    //直接开通包月钻石会员 6800
    private static final int VIP_MONEY_OPEN_DIAMOND_MONTH = 6800;
    //升级包月钻石会员 3000
    private static final int VIP_MONEY_UPDATE_DIAMOND_MONTH = 3000;
    //直接开通包年钻石会员 9800
    private static final int VIP_MONEY_OPEN_DIAMOND_YEAR = 9800;
    //黄金包月升级包年钻石会员 6000
    private static final int VIP_MONEY_UPDATE_DIAMOND_YEAR_FROM_MONTH = 6000;
    //黄金包年升级包年钻石 3000
    private static final int VIP_MONEY_UPDATE_DIAMOND_YEAR_FROM_YEAR = 3000;

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
     * @param viptype 开通的服务类型
     * @param videoId 视频id
     */
    public void payByWeChat(Context context, int viptype, int videoId, int pay_position_id) {
        getOrderNo(context, viptype, true, videoId, pay_position_id);
    }

    /**
     * 支付宝去支付
     *
     * @param context 上下文
     * @param viptype 要开通的服务类型
     * @param videoId 视频id
     */
    public void payByAliPay(Context context, int viptype, int videoId, int pay_position_id) {
        getOrderNo(context, viptype, false, videoId, pay_position_id);
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
     * @param context  上下文
     * @param vipType  要开通的服务类型
     * @param isWechat 支付类型true：微信，false：支付宝
     * @param video_id 视频id
     */
    private void getOrderNo(final Context context, final int vipType, final boolean isWechat, final int video_id, int pay_position_id) {
        App.isGoodsPay = false;
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        dialog = ProgressDialog.show(context, "", "订单提交中...");
        Map<String, String> params = API.createParams();
        switch (vipType) {
            case VIP_TYPE_OPEN_GLOD_MONTH:
                params.put("money", String.valueOf(VIP_MONEY_OPEN_GLOD_MONTH));
                params.put("title", "开通黄金包月会员");
                break;
            case VIP_TYPE_OPEN_GLOD_DISCOUNT_MONTH:
                params.put("money", String.valueOf(VIP_MONEY_OPEN_GLOD_DISCOUNT_MONTH));
                params.put("title", "开通黄金包月会员（优惠）");
                break;
            case VIP_TYPE_OPEN_GLOD_DISCOUNT_YEAR:
                params.put("money", String.valueOf(VIP_MONEY_OPEN_GLOD_DISCOUNT_YEAR));
                params.put("title", "开通黄金包年会员（优惠）");
                break;
            case VIP_TYPE_OPEN_GLOD_YEAR:
                params.put("money", String.valueOf(VIP_MONEY_OPEN_GLOD_YEAR));
                params.put("title", "开通黄金包年会员");
                break;
            case VIP_TYPE_OPEN_DIAMOND_MONTH:
                params.put("money", String.valueOf(VIP_MONEY_OPEN_DIAMOND_MONTH));
                params.put("title", "开通钻石包月会员");
                break;
            case VIP_TYPE_UPDATE_DIAMOND_MONTH:
                params.put("money", String.valueOf(VIP_MONEY_UPDATE_DIAMOND_MONTH));
                params.put("title", "升级钻石包月会员");
                break;
            case VIP_TYPE_OPEN_DIAMOND_YEAR:
                params.put("money", String.valueOf(VIP_MONEY_OPEN_DIAMOND_YEAR));
                params.put("title", "开通钻石包年会员");
                break;
            case VIP_TYPE_UPDATE_DIAMOND_YEAR_FROM_MONTH:
                params.put("money", String.valueOf(VIP_MONEY_UPDATE_DIAMOND_YEAR_FROM_MONTH));
                params.put("title", "黄金包月会员升级钻石包年会员");
                break;
            case VIP_TYPE_UPDATE_DIAMOND_YEAR_FROM_YEAR:
                params.put("money", String.valueOf(VIP_MONEY_UPDATE_DIAMOND_YEAR_FROM_YEAR));
                params.put("title", "黄金包年会员升级钻石包年会员");
                break;
        }

        params.put("video_id", String.valueOf(video_id));
        params.put("position_id", String.valueOf(pay_position_id));
        params.put("pay_type", isWechat ? "1" : "2");
        params.put("type", String.valueOf(vipType));

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
                        DialogUtil.getInstance().showWxQRCodePayDialog(context, info.getCode_img_url());
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                        DialogUtil.getInstance().showCustomSubmitDialog(context, "支付二维码已经保存到您的相册，请前往微信扫一扫付费");
                    } else if (info.getPay_type() == 2) {
                        //支付宝wap
                        Intent intent = new Intent(context, AliPayActivity.class);
                        intent.putExtra(AliPayActivity.ALIPAY_URL, info.getCode_url());
                        context.startActivity(intent);
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                    } else if (info.getPay_type() == 3) {
                        //微信wap
                        if (TextUtils.isEmpty(info.getWx_url())) {
                            if (!TextUtils.isEmpty(info.getPay_info())) {
                                Intent intent = new Intent(context, WechatPayActivity.class);
                                intent.putExtra(WechatPayActivity.WECHAT_PAY_URL, info.getPay_info());
                                context.startActivity(intent);
                                App.isNeedCheckOrder = true;
                                App.orderIdInt = info.getOrder_id_int();
                            } else {
                                ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(info.getWx_url()));
                            context.startActivity(intent);
                            App.isNeedCheckOrder = true;
                            App.orderIdInt = info.getOrder_id_int();
                        }
                    } else if (info.getPay_type() == 4) {
                        //微信公众号二维码
                        DialogUtil.getInstance().showWxQRCodePayDialog(context, info.getCode_img_url());
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                        DialogUtil.getInstance().showCustomSubmitDialog(context, "支付二维码已经保存到您的相册，请前往微信扫一扫付费");
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
                        intent.putExtra(AliPayActivity.ALIPAY_URL, info.getCode_url());
                        context.startActivity(intent);
                        App.isNeedCheckOrder = true;
                        App.isGoodsBuyPage = isGoodsBuyPage;
                        App.goodsOrderId = info.getOrder_id_int();
                    } else if (info.getPay_type() == 3) {
                        //微信wap
                        if (TextUtils.isEmpty(info.getWx_url())) {
                            if (!TextUtils.isEmpty(info.getPay_info())) {
                                Intent intent = new Intent(context, WechatPayActivity.class);
                                intent.putExtra(WechatPayActivity.WECHAT_PAY_URL, info.getPay_info());
                                context.startActivity(intent);
                                App.isNeedCheckOrder = true;
                                App.goodsOrderId = info.getOrder_id_int();
                                App.isGoodsBuyPage = isGoodsBuyPage;
                            } else {
                                ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(info.getWx_url()));
                            context.startActivity(intent);
                            App.isNeedCheckOrder = true;
                            App.goodsOrderId = info.getOrder_id_int();
                            App.isGoodsBuyPage = isGoodsBuyPage;
                        }
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

}
