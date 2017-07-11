package com.cl.cltv.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:频道
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/6/1 16:35
 */

public class ChannelResultInfo implements Serializable {

    /**
     * data : [{"id":1,"title":"熟女诱惑","thumb":"http://tfile.alqi.pw/video/gallery/cate/1.png","hits":659851,"period":325},{"id":2,"title":"极致诱惑","thumb":"http://tfile.alqi.pw/video/gallery/cate/2.png","hits":558984,"period":526},{"id":3,"title":"美腿诱惑","thumb":"http://tfile.alqi.pw/video/gallery/cate/3.png","hits":498654,"period":487},{"id":4,"title":"高清诱惑","thumb":"http://tfile.alqi.pw/video/gallery/cate/4.png","hits":639885,"period":365},{"id":5,"title":"黑丝诱惑","thumb":"http://tfile.alqi.pw/video/gallery/cate/5.png","hits":558977,"period":555},{"id":6,"title":"湿身诱惑","thumb":"http://tfile.alqi.pw/video/gallery/cate/6.png","hits":365685,"period":389},{"id":7,"title":"制服诱惑","thumb":"http://tfile.alqi.pw/video/gallery/cate/7.png","hits":745485,"period":665}]
     * status : true
     */

    private boolean status;
    private List<DataBean> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 熟女诱惑
         * thumb : http://tfile.alqi.pw/video/gallery/cate/1.png
         * hits : 659851
         * period : 325
         */

        private int id;
        private String title;
        private String thumb;
        private int hits;
        private int period;

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

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }
    }
}
