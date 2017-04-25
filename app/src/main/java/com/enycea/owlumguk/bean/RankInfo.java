package com.enycea.owlumguk.bean;

import java.io.Serializable;

/**
 * Case By:排行榜
 * package:com.hfaufhreu.hjfeuio.bean
 * Author：scene on 2017/4/19 20:43
 */

public class RankInfo implements Serializable {

    /**
     * id : 13
     * actor_name : 翔田千里
     * thumb : http://down.18kam.net/img/2017/03/877e223d6319d665c84f2d64042243e5.jpeg
     * votes : 3671
     * hits : 10856
     * created_at : 2017-03-29 09:17:28
     * updated_at : 2017-04-19 21:15:39
     * age : 33
     * description : 翔田千里
     * tag : 奶牛
     * videos : 《お色気P●A会長と悪ガキ生徒会》 《逆デリヘル-濃厚な接吻と猛烈に絡み合う肉体》 《美脚パンストマニアックス 極上の悩殺ストッキング 4時間 Vol.2》
     * percent : 23
     */

    private int id;
    private String actor_name;
    private String thumb;
    private String votes;
    private String hits;
    private String created_at;
    private String updated_at;
    private String age;
    private String description;
    private String tag;
    private String videos;
    private int percent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
