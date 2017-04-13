package com.heuewo.hiaodoipo.bean;

import java.io.Serializable;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.VideoInfo;

/**
 * Created by 首页 热门推荐 on 2017/3/15.
 */

public class IndexInfo implements Serializable {
    //1:banner 2:试看 3：vip最新上线 4：vip每周推荐 5：VIP热播
    int type;
    List<VideoInfo> data;

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
}
