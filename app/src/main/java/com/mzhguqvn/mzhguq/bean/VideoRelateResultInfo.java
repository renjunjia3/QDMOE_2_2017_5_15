package com.mzhguqvn.mzhguq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/24 12:46
 */

public class VideoRelateResultInfo implements Serializable {
    private boolean status;
    private List<VideoInfo> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<VideoInfo> getData() {
        return data;
    }

    public void setData(List<VideoInfo> data) {
        this.data = data;
    }
}
