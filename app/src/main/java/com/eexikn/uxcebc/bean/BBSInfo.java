package com.eexikn.uxcebc.bean;

import java.io.Serializable;

/**
 * Case By:论坛首页
 * package:com.hfaufhreu.hjfeuio.bean
 * Author：scene on 2017/4/18 13:11
 */

public class BBSInfo implements Serializable {


    /**
     * id : 12
     * title : 绝密资料
     * thumb : http://down.18kam.net/bbs/jmzl.jpg
     * description : 绝密图片 绝密视频
     * weight : 0
     * topic_number : 104
     * latest : 只分享2小时 速看 看完删帖
     * created_at : -0001-11-30 00:00:00
     * updated_at : -0001-11-30 00:00:00
     */

    private int id;
    private String title;
    private String thumb;
    private String description;
    private String weight;
    private String topic_number;
    private String latest;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTopic_number() {
        return topic_number;
    }

    public void setTopic_number(String topic_number) {
        this.topic_number = topic_number;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
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
