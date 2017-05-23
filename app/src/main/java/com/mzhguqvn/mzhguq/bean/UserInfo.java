package com.mzhguqvn.mzhguq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/14.
 */

public class UserInfo implements Serializable {


    /**
     * user_id : 2
     * role : 0
     * cdn : 1
     */

    private int user_id;
    //0：游客，1：黄金，2 ：钻石
    private int role;
    //0：未开通cdn ,1：已开通cdn
    private int cdn;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getCdn() {
        return cdn;
    }

    public void setCdn(int cdn) {
        this.cdn = cdn;
    }
}
