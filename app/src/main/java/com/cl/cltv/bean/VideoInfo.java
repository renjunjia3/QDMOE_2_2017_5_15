package com.cl.cltv.bean;

import java.io.Serializable;
import java.util.Random;

/**
 * 视频信息
 * Created by scene on 2017/3/15.
 */

public class VideoInfo implements Serializable {
    private boolean isTilteType;

    private String thumb;
    private String tag;
    private String tag_color;
    private String title;
    private String url;
    private int video_id;
    private int id;
    private String description;
    private int hits;
    private int score;
    private int update_number;
    private String thumb_heng;
    private int duration;//服务器返回的时间是真实时间*100的值
    private int real_duration;

    private Random random = new Random();

    public boolean isTilteType() {
        return isTilteType;
    }

    public void setTilteType(boolean tilteType) {
        isTilteType = tilteType;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag_color() {
        return tag_color;
    }

    public void setTag_color(String tag_color) {
        this.tag_color = tag_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVideo_id() {
        return video_id == 0 ? getId() : video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUpdate_number() {
        return update_number;
    }

    public void setUpdate_number(int update_number) {
        this.update_number = update_number;
    }

    public int getId() {
        return id == 0 ? getVideo_id() : id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumb_heng() {
        return thumb_heng;
    }

    public void setThumb_heng(String thumb_heng) {
        this.thumb_heng = thumb_heng;
    }

    public int getDuration() {
        /*
        因为服务器的时间是真实秒数乘以100，本地使用的时候是把这个时间虚拟了50倍的
        所以转换到需要的时间的方式是：服务器返回的时间除以100乘以1000再乘以50倍
        换算之后不足1小时的再加半个小时
        */

        if (duration * 500 < 3600 * 1000) {
            return duration * 500 + random.nextInt(1000) * 1000 + 20 * 60 * 1000;
        } else {
            return duration * 500 + random.nextInt(1000) * 1000;
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getReal_duration() {
        return duration * 10;
    }

    public void setReal_duration(int real_duration) {
        this.real_duration = real_duration;
    }
}
