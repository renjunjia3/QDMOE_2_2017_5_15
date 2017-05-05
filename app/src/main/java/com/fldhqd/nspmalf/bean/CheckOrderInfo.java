package com.fldhqd.nspmalf.bean;

import java.io.Serializable;

/**
 * Case By:
 * package:com.plnrqrzy.tjtutfa.bean
 * Author：scene on 2017/4/25 14:11
 */

public class CheckOrderInfo implements Serializable {
    private boolean status;
    private int is_heijin;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getIs_heijin() {
        return is_heijin;
    }

    public void setIs_heijin(int is_heijin) {
        this.is_heijin = is_heijin;
    }
}
