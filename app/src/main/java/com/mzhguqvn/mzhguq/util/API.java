package com.mzhguqvn.mzhguq.util;

import com.mzhguqvn.mzhguq.app.App;

import java.util.HashMap;

/**
 * Case By: API接口
 * package:com.mzhguqvn.mzhguq.util
 * Author：scene on 2017/5/19 11:12
 */
public class API {

    //正式环境
    public static final String URL_PRE = "http://api.alpv.pw/video/";
//    //测试环境
//    public static final String URL_PRE = "http://tapi.alpv.pw/video/";

    //默认的key
    private static final String SIGN_KEY = "045448f765b0c0592563123a2652fb63";

    //登录注册 每天只调用一次
    public static final String LOGIN_REGISTER = "user";
    //点击支付按钮pay/click/{imei}
    public static final String PAY_CLICK = "pay/click/";
    //视频详情页相关推荐
    public static final String VIDEO_RELATED = "video/related";
    //检查支付状态http://api.18kam.net/video/pay/is_success?order_id=12&imei=860635035819277
    public static final String CHECK_ORDER = "pay/is_success";
    //获取订单信息方式2
    public static final String GET_ORDER_INFO_TYPE_2 = "pay/get_token";
    //获取视频详情评论资源
    public static final String VIDEO_COMMENT = "video/comment";

    //获取会员首页的数据[1 => '体验', 2 => '黄金', 3 => '钻石', 4 => '主播']
    public static final String VIP_INDEX = "position";

    //每隔30s调用一次
    public static final String UPLOAD_INFP = "user/stay";
    //更新
    public static final String UPDATE = "version/android";
    //上传log日子
    public static final String LOG = "log";
    //弹幕
    public static final String DANMU = "video/danmu";
    /*
    统计
 1 => '体验', 2 => '黄金', 3 => '钻石', 4 => '主播', 5 => '图库', 6 => '商城购买弹窗', 7 => '商城产品页',
 8 => '商城购买清单', 9 => '商城产品购买成功', 10 => '我的', 11 => '订单列表', 12 => '订单详情',
  13 => '会员协议', 14 => '联系客服退款', 15 => '开通黄金会员支付弹窗', 16 => '开通钻石会员支付弹窗',
  17 => '开通CDN加速支付弹窗', 18 => '特惠大礼包(支付)', 19 => '商城评论列表', 20 => '视频详情页', 21 => '我的代金券'
     */
    public static final String UPLOAD_CURRENT_PAGE = "position/click";

    //发表评论
    public static final String SEND_COMMEND = "user/comment";
    //商品详情
    public static final String GOODS_DETAIL = "goods/get_goods";
    //商品评论
    public static final String GOODS_COMMENT = "goods/comment";
    //商品下单
    public static final String GOODS_CREATE_ORDER = "goods/create_order";
    //购买商品支付是否成功:goods/is_pay_success/{user_id}/{order_id}
    public static final String CHECK_GOODS_ORDER = "goods/is_pay_success";
    //获取订单列表goods/get_orders/{user_id}
    public static final String GET_ORDERS = "goods/get_orders";
    //获取物流信息
    public static final String GET_LOGISTICS = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";
    //获取图库数据
    public static final String GALLERY = "gallery";
    //频道
    public static final String CHANNEL = "gallery/cate";
    //频道内页
    public static final String CHANNEL_DETAIL = "gallery";
    //获取代金券列表
    public static final String VOUCHER = "user/voucher";
    //上传更新日志
    public static final String UPDATE_FAIL = "version/android_fail";
    //获取开通会员的提示
    public static final String TOP_NOTICE = "user/pay_success";
    //排行榜
    public static final String RANK = "ranking";
    //排行榜内容
    public static final String RANK_DETAIL = "ranking/lists";

    /**
     * Case By:创建参数基础信息
     * Author: scene on 2017/5/19 13:19
     */
    public static HashMap<String, String> createParams() {
        HashMap<String, String> params = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        params.put("agent_id", App.CHANNEL_ID + "");
        params.put("timestamp", timestamp + "");
        params.put("signature", getSign(App.CHANNEL_ID + "", timestamp + ""));
        params.put("app_type", "1");
        params.put("imei", App.IMEI);
        params.put("version", App.versionCode + "");
        return params;
    }

    /**
     * Case By:获取sign
     * Author: scene on 2017/5/19 13:19
     */
    private static String getSign(String agent_id, String timestamp) {
        return MD5Util.string2Md5(MD5Util.string2Md5(agent_id + timestamp + 1 + App.IMEI + App.versionCode, "UTF-8") + SIGN_KEY, "UTF-8");
    }

}
