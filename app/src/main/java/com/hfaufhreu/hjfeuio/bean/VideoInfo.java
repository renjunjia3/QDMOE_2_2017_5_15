package com.hfaufhreu.hjfeuio.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 视频信息
 * Created by scene on 2017/3/15.
 */

public class VideoInfo implements Serializable {
    int id;
    String title;
    String thumb;
    List<String> images;//截图
    int cate_id;//分类ID
    int actor_id;//女优id
    String url;//视频地址
    String attr;//属性
    int hot;//1:热门
    int vip;//1:会员才能看
    int try_view;//1:可以试看
    int hits;//人气
    int slider;//是否是幻灯片
    String created_at;//创建时间
    String updated_at;//修改时间
    String slider_thumb;//幻灯片缩略图
    String description;//简介

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getActor_id() {
        return actor_id;
    }

    public void setActor_id(int actor_id) {
        this.actor_id = actor_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getTry_view() {
        return try_view;
    }

    public void setTry_view(int try_view) {
        this.try_view = try_view;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getSlider() {
        return slider;
    }

    public void setSlider(int slider) {
        this.slider = slider;
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

    public String getSlider_thumb() {
        return slider_thumb;
    }

    public void setSlider_thumb(String slider_thumb) {
        this.slider_thumb = slider_thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
