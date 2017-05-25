package com.mzhguqvn.mzhguq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:我的代金券列表
 * package:com.mzhguqvn.mzhguq.adapter
 * Author：scene on 2017/5/25 14:57
 */

public class VoucherListResultInfo implements Serializable {
    private boolean status;
    private List<VoucherInfo> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<VoucherInfo> getData() {
        return data;
    }

    public void setData(List<VoucherInfo> data) {
        this.data = data;
    }
}
