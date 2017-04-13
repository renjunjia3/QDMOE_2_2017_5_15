package com.heuewo.hiaodoipo.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/30.
 */

public class CheckOrderInfo implements Serializable {
    boolean status;
    int pay_type;
    int type;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
}
