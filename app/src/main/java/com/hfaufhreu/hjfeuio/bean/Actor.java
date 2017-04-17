package com.hfaufhreu.hjfeuio.bean;

import java.io.Serializable;

/**
 * 女优首页
 * Created by scene on 2017/3/14.
 */

public class Actor implements Serializable {
    String actor_name;
    String thumb;
    int id;
    String hits;//人气

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }
}
