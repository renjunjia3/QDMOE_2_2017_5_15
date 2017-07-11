package com.cl.cltv.bean;

import java.io.Serializable;

/**
 * Case By:
 * package:com.enycea.owlumguk.bean
 * Author：scene on 2017/4/25 16:51
 */

public class UpdateInfo implements Serializable {

    /**
     * id : 1
     * version_name : 1.06
     * version_code : 2
     * size : 4
     * created_at : 2017-04-25 14:30:16
     * updated_at : 2017-04-25 14:30:16
     * apk_url : http://file.18kam.net/apk/video/NNY_1001.apk
     */

    private int id;
    private String version_name;
    private int version_code;
    private String size;
    private String created_at;
    private String updated_at;
    private String apk_url;
    private String apk_url1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getApk_url1() {
        return apk_url1;
    }

    public void setApk_url1(String apk_url1) {
        this.apk_url1 = apk_url1;
    }
}
