package com.cl.cltv.bean;

import java.io.Serializable;

/**
 * Case By:收货信息
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/10 16:19
 */

public class ReceiverInfo implements Serializable {
    private String receiverName;
    private String receiverPhone;
    private String receiverProvince;
    private String receiverCity;
    private String receiverArea;
    private String receiverAddress;
    private int positionProvince;
    private int positionCity;
    private int positionArea;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverArea() {
        return receiverArea;
    }

    public void setReceiverArea(String receiverArea) {
        this.receiverArea = receiverArea;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public int getPositionProvince() {
        return positionProvince;
    }

    public void setPositionProvince(int positionProvince) {
        this.positionProvince = positionProvince;
    }

    public int getPositionCity() {
        return positionCity;
    }

    public void setPositionCity(int positionCity) {
        this.positionCity = positionCity;
    }

    public int getPositionArea() {
        return positionArea;
    }

    public void setPositionArea(int positionArea) {
        this.positionArea = positionArea;
    }
}
