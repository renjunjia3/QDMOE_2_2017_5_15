package com.mzhguqvn.mzhguq.config;

import com.mzhguqvn.mzhguq.app.App;


/**
 * Case By: 支付配置
 * package:com.mzhguqvn.mzhguq.config
 * Author：scene on 2017/6/14 19:07
 */
public class PayConfig {
    //当前应用版本号
    public static final String VERSION_NAME = App.versionCode + "-2017/6/14-Relase-" + App.CHANNEL_ID;
    //微信支付
    public static final int PAY_BY_WECHAT = 1;
    //支付宝支付
    public static final int PAY_BY_ALIPAY = 2;
    //默认支付方式
    public static int DEFAULT_PAY_WAY = PAY_BY_ALIPAY;
}
