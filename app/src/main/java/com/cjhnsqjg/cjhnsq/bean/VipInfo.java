package com.cjhnsqjg.cjhnsq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.bean
 * Author：scene on 2017/4/18 20:30
 */

public class VipInfo implements Serializable {
    List<VideoInfo> banner;
    List<TrySeeContentInfo> other;

    public List<VideoInfo> getBanner() {
        return banner;
    }

    public void setBanner(List<VideoInfo> banner) {
        this.banner = banner;
    }

    public List<TrySeeContentInfo> getOther() {
        return other;
    }

    public void setOther(List<TrySeeContentInfo> other) {
        this.other = other;
    }
}
