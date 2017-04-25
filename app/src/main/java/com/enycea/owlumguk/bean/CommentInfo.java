package com.enycea.owlumguk.bean;

import java.io.Serializable;

/**
 * Created by scene on 2017/3/16.
 */

public class CommentInfo implements Serializable {
    String avatar;
    String time;
    String text;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CommentInfo{" +
                "avatar='" + avatar + '\'' +
                ", time='" + time + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
