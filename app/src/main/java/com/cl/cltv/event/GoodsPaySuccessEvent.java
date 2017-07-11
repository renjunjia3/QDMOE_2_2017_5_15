package com.cl.cltv.event;

/**
 * Case By:
 * package:com.mzhguqvn.mzhguq.event
 * Author：scene on 2017/5/10 15:53
 */

public class GoodsPaySuccessEvent {
    public boolean isGoodsBuyPage;

    public GoodsPaySuccessEvent(boolean isGoodsBuyPage) {
        this.isGoodsBuyPage = isGoodsBuyPage;
    }
}
