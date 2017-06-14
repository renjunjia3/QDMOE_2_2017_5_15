package com.mzhguqvn.mzhguq.bean;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.bean
 * Author：scene on 2017/4/21 15:40
 */

public class PayTokenResultInfo {
    private int pay_type;//1：微信扫码，2支付宝扫码
    private String pay_url;//支付宝wap路径
    private String payinfo;//吊起客户端的数据
    private int order_id_int;//订单号
    private String order_id;
    private String code_img_url;//微信的二维码地址，
    private boolean status;
    private String code_url;
    private String pay_info;
    private String wx_url;

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_url() {
        return pay_url;
    }

    public void setPay_url(String pay_url) {
        this.pay_url = pay_url;
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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCode_img_url() {
        return code_img_url;
    }

    public void setCode_img_url(String code_img_url) {
        this.code_img_url = code_img_url;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
    }

    public String getWx_url() {
        return wx_url;
    }

    public void setWx_url(String wx_url) {
        this.wx_url = wx_url;
    }
}
