package com.mzhguqvn.mzhguq.bean;

import java.util.List;

/**
 * Case By:
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/23 16:55
 */

public class GalleryResultInfo {

    /**
     * data : [{"id":6,"title":"学生妹","thumb":"http://tfile.hemeiti.net/img/2017/05/3326f579cc586cfa285d9125324e6dd3.jpeg","hits":559,"created_at":"2017-05-23 14:10:59","updated_at":"2017-05-23 14:10:59"},{"id":5,"title":"熟女","thumb":"http://tfile.hemeiti.net/img/2017/05/c8b3945b3e3bad572bcfcbac19b75691.png","hits":1381,"created_at":"2017-05-23 14:10:42","updated_at":"2017-05-23 14:10:42"},{"id":4,"title":"爆乳新人","thumb":"http://tfile.hemeiti.net/img/2017/05/b676bb60023823529d36819b44d2752d.png","hits":606,"created_at":"2017-05-23 14:10:22","updated_at":"2017-05-23 14:10:22"},{"id":3,"title":"美女哦","thumb":"http://tfile.hemeiti.net/img/2017/05/9a76147a12820d7497b6863eb701f924.png","hits":651,"created_at":"2017-05-23 14:10:00","updated_at":"2017-05-23 14:10:00"},{"id":2,"title":"大屁股","thumb":"http://tfile.hemeiti.net/img/2017/05/b2c63fe3e6f017df35fdc2fd8b991732.png","hits":665,"created_at":"2017-05-23 14:09:48","updated_at":"2017-05-23 14:09:48"}]
     * status : true
     * status1 : true
     */

    private boolean status;
    private boolean status1;
    private List<GalleryInfo> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus1() {
        return status1;
    }

    public void setStatus1(boolean status1) {
        this.status1 = status1;
    }

    public List<GalleryInfo> getData() {
        return data;
    }

    public void setData(List<GalleryInfo> data) {
        this.data = data;
    }

    public static class GalleryInfo {
        /**
         * id : 6
         * title : 学生妹
         * thumb : http://tfile.hemeiti.net/img/2017/05/3326f579cc586cfa285d9125324e6dd3.jpeg
         * hits : 559
         * created_at : 2017-05-23 14:10:59
         * updated_at : 2017-05-23 14:10:59
         */

        private int id;
        private String title;
        private String thumb;
        private int hits;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
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
    }
}
