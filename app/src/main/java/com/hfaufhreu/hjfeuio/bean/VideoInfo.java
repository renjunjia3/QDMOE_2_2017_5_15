package com.hfaufhreu.hjfeuio.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 视频信息
 * Created by scene on 2017/3/15.
 */

public class VideoInfo implements Serializable {
    String thumb;
    String tag;
    String tag_color;
    String title;
    String url;
    int video_id;
    String description;
    int hits;

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
        return video_id;
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
                '}';
    }
}
