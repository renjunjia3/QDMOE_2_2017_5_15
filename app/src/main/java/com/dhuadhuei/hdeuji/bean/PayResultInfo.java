package com.dhuadhuei.hdeuji.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/30.
 */

public class PayResultInfo implements Serializable {
    private boolean status;
    private String order_id;
    private String url;
    private int pay_type;//1：微信 2：支付宝
    private int type;//1：开通VIp，2：开通加速
    private int order_int;

    public int getOrder_int() {
        return order_int;
    }

    public void setOrder_int(int order_int) {
        this.order_int = order_int;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PayResultInfo{" +
                "status=" + status +
                ", order_id='" + order_id + '\'' +
                ", url='" + url + '\'' +
                ", pay_type=" + pay_type +
                ", type=" + type +
                ", order_int=" + order_int +
                '}';
    }
}
