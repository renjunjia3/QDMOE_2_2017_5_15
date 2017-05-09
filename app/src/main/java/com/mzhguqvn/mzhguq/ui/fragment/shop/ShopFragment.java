package com.mzhguqvn.mzhguq.ui.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.GoodsInfo;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:商品页
 * package:com.fldhqd.nspmalf.ui.fragment.shop
 * Author：scene on 2017/5/9 10:11
 */

public class ShopFragment extends BaseMainFragment {
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.kuaidi_price)
    TextView kuaidiPrice;
    @BindView(R.id.sale_number)
    TextView saleNumber;
    @BindView(R.id.shop_address)
    TextView shopAddress;
    @BindView(R.id.buy_send_vip)
    TextView buySendVip;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.image4)
    ImageView image4;
    @BindView(R.id.image5)
    ImageView image5;
    @BindView(R.id.image6)
    ImageView image6;
    @BindView(R.id.image7)
    ImageView image7;
    @BindView(R.id.image8)
    ImageView image8;
    @BindView(R.id.image9)
    ImageView image9;
    @BindView(R.id.image10)
    ImageView image10;
    @BindView(R.id.image11)
    ImageView image11;
    @BindView(R.id.image12)
    ImageView image12;
    @BindView(R.id.image13)
    ImageView image13;
    @BindView(R.id.image14)
    ImageView image14;
    @BindView(R.id.image15)
    ImageView image15;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.goods_image)
    ImageView goodsImage;
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.goods_price)
    TextView goodsPrice;

    private RequestCall dataRequestCall;
    private RequestCall commentRequestCall;

    private GoodsInfo goodsInfo;

    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        getData(true);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Case By:获取数据
     * Author: scene on 2017/5/9 14:48
     */
    private void getData(final boolean isShowLoading) {
        if (isShowLoading) {
            statusViewLayout.showLoading();
        }
        if (NetWorkUtils.isNetworkConnected(_mActivity)) {
            dataRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.GOODS_DETAIL).build();
            dataRequestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    if (isShowLoading) {
                        statusViewLayout.showNetError(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        goodsInfo = JSON.parseObject(s, GoodsInfo.class);
                        initData(isShowLoading);
                        if (isShowLoading) {
                            statusViewLayout.showContent();
                        } else {
                            ptrLayout.refreshComplete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            if (isShowLoading) {
                statusViewLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
        }
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getData(true);
        }
    };

    /**
     * Case By:显示数据
     * Author: scene on 2017/5/9 15:03
     */
    private void initData(boolean isShowLoading) {
        name.setText(goodsInfo.getName());
        price.setText("￥" + goodsInfo.getPrice());
        saleNumber.setText("" + goodsInfo.getSales());
        shopAddress.setText(goodsInfo.getAddress());
        kuaidiPrice.setText(goodsInfo.getDelivery_money() == 0 ? "免费" : goodsInfo.getDelivery_money() + "");
        goodsName.setText(goodsInfo.getName());
        goodsPrice.setText("￥"+goodsInfo.getPrice());
        Glide.with(getContext()).load(goodsInfo.getThumb()).into(goodsImage);
        if (goodsInfo.getImages() == null || goodsInfo.getImages().size() < 15) {
            if (isShowLoading) {
                statusViewLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
            return;
        }
        List<String> images = goodsInfo.getImages();
        Glide.with(getContext()).load(images.get(0)).into(image1);
        Glide.with(getContext()).load(images.get(1)).into(image2);
        Glide.with(getContext()).load(images.get(2)).into(image3);
        Glide.with(getContext()).load(images.get(3)).into(image4);
        Glide.with(getContext()).load(images.get(4)).into(image5);
        Glide.with(getContext()).load(images.get(5)).into(image6);
        Glide.with(getContext()).load(images.get(6)).into(image7);
        Glide.with(getContext()).load(images.get(7)).into(image8);
        Glide.with(getContext()).load(images.get(8)).into(image9);
        Glide.with(getContext()).load(images.get(9)).into(image10);
        Glide.with(getContext()).load(images.get(10)).into(image11);
        Glide.with(getContext()).load(images.get(11)).into(image12);
        Glide.with(getContext()).load(images.get(12)).into(image13);
        Glide.with(getContext()).load(images.get(13)).into(image14);
        Glide.with(getContext()).load(images.get(14)).into(image15);
    }
}
