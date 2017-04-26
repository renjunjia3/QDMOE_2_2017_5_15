package com.szbqzsmxz.szbqz.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/14.
 */

public class UserInfo implements Serializable {
    private int user_id;
    private int is_vip;
    private int is_heijin;

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

    public int getIs_heijin() {
        return is_heijin;
    }

    public void setIs_heijin(int is_heijin) {
        this.is_heijin = is_heijin;
    }
}
