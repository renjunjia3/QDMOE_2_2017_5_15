package com.mzhguqvn.mzhguq.bean;

import java.util.List;

/**
 * Case By:视频评论返回值
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/24 12:43
 */

public class VideoCommentResultInfo {
    private boolean status;
    private List<CommentInfo> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<CommentInfo> getData() {
        return data;
    }

    public void setData(List<CommentInfo> data) {
        this.data = data;
    }
}
