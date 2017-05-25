package com.mzhguqvn.mzhguq.bean;

import com.mzhguqvn.mzhguq.util.DecimalUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Case By:
 * package:com.fldhqd.nspmalf.bean
 * Author：scene on 2017/5/9 14:47
 */

public class GoodsInfo implements Serializable {

    /**
     * id : 1
     * name : 印度CIPLA万艾可
     * description : 印度CIPLA万艾可
     * thumb : http://tfile.hemeiti.net/goods/1/thumb.jpg
     * price : 19990
     * images : ["http://tfile.hemeiti.net/goods/1/p1.webp","http://tfile.hemeiti.net/goods/1/p2.webp","http://tfile.hemeiti.net/goods/1/p3.webp","http://tfile.hemeiti.net/goods/1/p4.webp","http://tfile.hemeiti.net/goods/1/p5.webp","http://tfile.hemeiti.net/goods/1/p6.webp","http://tfile.hemeiti.net/goods/1/p7.webp","http://tfile.hemeiti.net/goods/1/p8.webp","http://tfile.hemeiti.net/goods/1/p9.webp","http://tfile.hemeiti.net/goods/1/p10.webp","http://tfile.hemeiti.net/goods/1/p11.webp","http://tfile.hemeiti.net/goods/1/p12.webp","http://tfile.hemeiti.net/goods/1/p13.webp","http://tfile.hemeiti.net/goods/1/p14.webp","http://tfile.hemeiti.net/goods/1/p15.webp"]
     * hits : 9654658
     * create_date : 20170509
     * created_at : 2017-05-09 14:10:38
     * updated_at : 2017-05-09 14:10:54
     */

    private int id;
    private String name;
    private String description;
    private String thumb;
    private double price;//单位是分
    private int hits;
    private int create_date;
    private String created_at;
    private String updated_at;
    private List<String> images;
    private int sales;
    private String address;
    private double delivery_money;
    private List<VoucherInfo> voucher;

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public double getPrice() {
        return price / 100d;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getCreate_date() {
        return create_date;
    }

    public void setCreate_date(int create_date) {
        this.create_date = create_date;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public double getDelivery_money() {
        return delivery_money;
    }

    public void setDelivery_money(double delivery_money) {
        this.delivery_money = delivery_money;
    }

    public List<VoucherInfo> getVoucher() {
        return voucher;
    }

    public void setVoucher(List<VoucherInfo> voucher) {
        this.voucher = voucher;
    }
}
