package com.mzhguqvn.mzhguq.bean;

import java.util.List;

/**
 * Case By:排行榜返回
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/6/9 12:07
 */

public class RankResultInfo {

    /**
     * data : [{"id":1,"title":"水莱丽","thumb":"http://static.alpy.pw/video/ranking/cate/1.jpg","score":3659,"description":"《UMSO-073》","tag":"重口loli","percentage":1}]
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
         * title : 水莱丽
         * thumb : http://static.alpy.pw/video/ranking/cate/1.jpg
         * score : 3659
         * description : 《UMSO-073》
         * tag : 重口loli
         * percentage : 1
         */

        private int id;
        private String title;
        private String thumb;
        private int score;
        private String description;
        private String tag;
        private int percentage;

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

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }
    }
}
