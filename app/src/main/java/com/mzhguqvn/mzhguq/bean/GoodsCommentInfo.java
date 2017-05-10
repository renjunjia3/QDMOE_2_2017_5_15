package com.mzhguqvn.mzhguq.bean;

import com.mzhguqvn.mzhguq.util.DateUtils;

import java.io.Serializable;

/**
 * Case By:
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/10 11:57
 */

public class GoodsCommentInfo implements Serializable {


    /**
     * id : 1
     * name : 玉皇大帝
     * avatar : http://tfile.hemeiti.net/avatar/head7.jpg
     * content : 药很好，很快就见效了，一晚上来了8次，每次一小时，非常硬
     * hits : 336
     * good : 26
     * created_at : 2017-05-09 17:09:34
     * updated_at : 2017-05-09 17:09:32
     */

    private int id;
    private String name;
    private String avatar;
    private String content;
    private int hits;
    private int good;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
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
