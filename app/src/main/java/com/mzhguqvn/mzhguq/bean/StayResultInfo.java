package com.mzhguqvn.mzhguq.bean;

/**
 * Case By:上传用户信息返回 获取默认的支付方式
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/6/16 17:06
 */

public class StayResultInfo {

    /**
     * default_pay_way : 1
     * status : true
     */

    private int default_pay_way;
    private boolean status;

    public int getDefault_pay_way() {
        return default_pay_way;
    }

    public void setDefault_pay_way(int default_pay_way) {
        this.default_pay_way = default_pay_way;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
