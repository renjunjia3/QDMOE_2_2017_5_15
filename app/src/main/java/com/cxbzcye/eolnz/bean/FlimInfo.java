package com.cxbzcye.eolnz.bean;

import java.io.Serializable;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.bean
 * Author：scene on 2017/4/19 17:13
 */

public class FlimInfo implements Serializable {

    /**
     * id : 1
     * title : 风骚护士
     * thumb : http://down.18kam.net/img/2017/04/543af7d87d63eddefe2fdf5b0a5d93cb.jpeg
     * weight : 5
     * update_number : 1776
     * created_at : 2017-03-27 17:04:24
     * updated_at : 2017-04-19 15:12:02
     */

    private int id;
    private String title;
    private String thumb;
    private String weight;
    private String update_number;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUpdate_number() {
        return update_number;
    }

    public void setUpdate_number(String update_number) {
        this.update_number = update_number;
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
