package com.cl.cltv.bean;

import java.io.Serializable;

/**
 * Case By:顶部的toast通知
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/6/7 13:50
 */

public class TopNoticeInfo implements Serializable {
    private boolean status;
    private String message;
    private long done_time;
    private int user_id;
    private int type;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDone_time() {
        return done_time;
    }

    public void setDone_time(long done_time) {
        this.done_time = done_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
