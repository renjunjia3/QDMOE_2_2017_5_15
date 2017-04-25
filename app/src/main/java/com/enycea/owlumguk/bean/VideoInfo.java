package com.enycea.owlumguk.bean;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return "VideoInfo{" +
                "thumb='" + thumb + '\'' +
                ", tag='" + tag + '\'' +
                ", tag_color='" + tag_color + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", video_id=" + video_id +
                ", description='" + description + '\'' +
                ", hits=" + hits +
                ", score=" + score +
                ", update_number=" + update_number +
                '}';
    }
}
