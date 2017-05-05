package com.fldhqd.nspmalf.bean;

import java.io.Serializable;

/**
 * Case By:
 * package:com.cyldf.cyldfxv.base
 * Author：scene on 2017/4/14 17:10
 */

public class SearchInfo implements Serializable {
    private String title;
    private String size;
    private String files;
    private boolean isShowPlay;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public boolean isShowPlay() {
        return isShowPlay;
    }

    public void setShowPlay(boolean showPlay) {
        isShowPlay = showPlay;
    }
}
