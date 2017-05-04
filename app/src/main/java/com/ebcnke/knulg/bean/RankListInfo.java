package com.ebcnke.knulg.bean;

import java.util.List;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.bean
 * Author：scene on 2017/4/19 22:33
 */

public class RankListInfo {
    private RankInfo actor;
    private List<VideoInfo> videos;

    public RankInfo getActor() {
        return actor;
    }

    public void setActor(RankInfo actor) {
        this.actor = actor;
    }

    public List<VideoInfo> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoInfo> videos) {
        this.videos = videos;
    }
}
