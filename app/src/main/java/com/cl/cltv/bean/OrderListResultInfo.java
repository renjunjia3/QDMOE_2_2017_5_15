package com.cl.cltv.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:订单列表
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/23 13:10
 */

public class OrderListResultInfo implements Serializable {
    private boolean status;
    private List<OrderInfo> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<OrderInfo> getData() {
        return data;
    }

    public void setData(List<OrderInfo> data) {
        this.data = data;
    }
}
