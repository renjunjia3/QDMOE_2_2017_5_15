package com.mzhguqvn.mzhguq.bean;

import java.io.Serializable;

/**
 * Case By:
 * package:com.mzhguqvn.mzhguq.bean
 * Author：scene on 2017/5/10 17:29
 */

public class OrderInfo implements Serializable {

    /**
     * order_id : g170510023915000001
     * good_name : 印度CIPLA万艾可
     * goods_thumb : http://tfile.hemeiti.net/goods/1/thumb.jpg
     * price : 2
     * money : 3
     * delivery_money : 1
     * address_name : 现在咋整
     * address : 重庆重庆南岸区南坪协信星光
     * mobile : 13389628382
     * delivery_no :
     */

    private String order_id;
    private String good_name;
    private String goods_thumb;
    private double price;
    private double money;
    private double delivery_money;
    private String address_name;
    private String address;
    private String mobile;
    private String delivery_no;
    private int number;
    //[1 => '未付款', 2 => '已付款', 3 => '已发货', 4 => '已完成', 5 => '支付失败']
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public double getPrice() {
        return price / 100d;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMoney() {
        return money / 100d;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getDelivery_money() {
        return delivery_money / 100d;
    }

    public void setDelivery_money(double delivery_money) {
        this.delivery_money = delivery_money;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
