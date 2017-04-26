package com.ofgvyiss.ofgvyi.bean;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.bean
 * Author：scene on 2017/4/21 15:40
 */

public class PayTokenResultInfo {
    private int type;//1：微信，2支付宝，3微信扫码4支付宝WAP
    private String pay_url;//支付宝wap路径
    private String payinfo;//吊起客户端的数据
    private int order_id_int;//订单号
    private String code_img;//微信的二维码地址，

    public String getPay_url() {
        return pay_url;
    }

    public void setPay_url(String pay_url) {
        this.pay_url = pay_url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPayinfo() {
        return payinfo;
    }

    public void setPayinfo(String payinfo) {
        this.payinfo = payinfo;
    }

    public int getOrder_id_int() {
        return order_id_int;
    }

    public void setOrder_id_int(int order_id_int) {
        this.order_id_int = order_id_int;
    }

    public String getCode_img() {
        return code_img;
    }

    public void setCode_img(String code_img) {
        this.code_img = code_img;
    }
}
