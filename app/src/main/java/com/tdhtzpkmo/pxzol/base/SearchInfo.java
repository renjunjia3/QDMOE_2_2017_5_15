package com.tdhtzpkmo.pxzol.base;

import java.io.Serializable;

/**
 * Case By:
 * package:com.cyldf.cyldfxv.base
 * Author：scene on 2017/4/14 17:10
 */

public class SearchInfo implements Serializable {
    String title;
    String size;
    String file;

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
