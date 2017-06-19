package com.mzhguqvn.mzhguq.bean;

import java.io.Serializable;

/**
 * Case By:
 * package:com.plnrqrzy.tjtutfa.bean
 * Author：scene on 2017/4/25 14:11
 */

public class CheckOrderInfo implements Serializable {
    private boolean status;
    private int cdn;
    private int role;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCdn() {
        return cdn;
    }

    public void setCdn(int cdn) {
        this.cdn = cdn;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
