package com.ofgvyiss.ofgvyi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.bean
 * Author：scene on 2017/4/18 20:36
 */

public class TrySeeContentInfo implements Serializable {

    String title;
    int type;//[1 => 'banner', 2 => '大图', 3 => '竖图', 4 => '横图']
    List<VideoInfo> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<VideoInfo> getData() {
        return data;
    }

    public void setData(List<VideoInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TrySeeContentInfo{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", data=" + data +
                '}';
    }
}
