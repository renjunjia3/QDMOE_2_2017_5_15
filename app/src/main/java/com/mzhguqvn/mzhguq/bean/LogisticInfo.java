package com.mzhguqvn.mzhguq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:物流信息
 * package:com.mzhguqvn.mzhguq.base
 * Author：scene on 2017/5/11 12:57
 */

public class LogisticInfo implements Serializable {


    /**
     * EBusinessID : 1288065
     * ShipperCode : ANE
     * Success : true
     * LogisticCode : 210001633605
     * State : 2
     * Traces : [{"AcceptTime":"2016-04-01 13:44:12","AcceptStation":"【2016-04-01 13:44:12】【和平滨江道】派送中,派件人【和平滨江道】,查件电话【null】"},{"AcceptTime":"2016-04-01 15:14:31","AcceptStation":""},{"AcceptTime":"2016-04-03 09:42:48","AcceptStation":"【2016-04-03 09:42:48】【和平滨江道】派送中,派件人【和平滨江道】,查件电话【null】"}]
     */

    private String EBusinessID;
    private String ShipperCode;
    private boolean Success;
    private String LogisticCode;
    private String State;
    private List<TracesBean> Traces;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String ShipperCode) {
        this.ShipperCode = ShipperCode;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public List<TracesBean> getTraces() {
        return Traces;
    }

    public void setTraces(List<TracesBean> Traces) {
        this.Traces = Traces;
    }

    public static class TracesBean {
        /**
         * AcceptTime : 2016-04-01 13:44:12
         * AcceptStation : 【2016-04-01 13:44:12】【和平滨江道】派送中,派件人【和平滨江道】,查件电话【null】
         */

        private String AcceptTime;
        private String AcceptStation;

        public String getAcceptTime() {
            return AcceptTime;
        }

        public void setAcceptTime(String AcceptTime) {
            this.AcceptTime = AcceptTime;
        }

        public String getAcceptStation() {
            return AcceptStation;
        }

        public void setAcceptStation(String AcceptStation) {
            this.AcceptStation = AcceptStation;
        }
    }
}
