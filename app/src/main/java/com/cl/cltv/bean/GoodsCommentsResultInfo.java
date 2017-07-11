package com.cl.cltv.bean;

import java.util.ArrayList;

/**
 * Case By:商品评论返回的数据
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/23 11:38
 */

public class GoodsCommentsResultInfo {
    private boolean status;
    private ArrayList<GoodsCommentInfo> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<GoodsCommentInfo> getData() {
        return data;
    }

    public void setData(ArrayList<GoodsCommentInfo> data) {
        this.data = data;
    }
}
