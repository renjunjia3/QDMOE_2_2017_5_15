package com.mzhguqvn.mzhguq.bean;

import java.io.Serializable;

/**
 * Case By:代金券信息
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/25 13:16
 */

public class VoucherInfo implements Serializable {
    private int id;
    private int type;
    private int money;
    private int user_id;
    private boolean status;
    private int pay_log_id;
    private int goods_order_id;
    private String use_time;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPay_log_id() {
        return pay_log_id;
    }

    public void setPay_log_id(int pay_log_id) {
        this.pay_log_id = pay_log_id;
    }

    public int getGoods_order_id() {
        return goods_order_id;
    }

    public void setGoods_order_id(int goods_order_id) {
        this.goods_order_id = goods_order_id;
    }

    public String getUse_time() {
        return use_time;
    }

    public void setUse_time(String use_time) {
        this.use_time = use_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
