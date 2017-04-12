package com.cyldf.cyldfxv.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/14.
 */

public class UserInfo implements Serializable {
    int user_id;
    int is_vip;
    int is_jiasu;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getIs_jiasu() {
        return is_jiasu;
    }

    public void setIs_jiasu(int is_jiasu) {
        this.is_jiasu = is_jiasu;
    }
}
