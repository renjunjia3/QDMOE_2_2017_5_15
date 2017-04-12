package com.cyldf.cyldfxv.bean;

import java.io.Serializable;

/**
 * Created by scene on 2017/3/16.
 */

public class CommentInfo implements Serializable {
    String content;
    String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
