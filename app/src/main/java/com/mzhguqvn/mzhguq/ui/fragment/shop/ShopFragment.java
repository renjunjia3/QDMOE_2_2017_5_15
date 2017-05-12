package com.mzhguqvn.mzhguq.ui.fragment.shop;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.adapter.GoodsCommentAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseMainFragment;
import com.mzhguqvn.mzhguq.bean.CreateGoodsOrderInfo;
import com.mzhguqvn.mzhguq.bean.GoodsCommentInfo;
import com.mzhguqvn.mzhguq.bean.GoodsInfo;
import com.mzhguqvn.mzhguq.bean.ProvinceInfo;
import com.mzhguqvn.mzhguq.bean.ReceiverInfo;
import com.mzhguqvn.mzhguq.config.AddressConfig;
import com.mzhguqvn.mzhguq.config.PayConfig;
import com.mzhguqvn.mzhguq.event.GoodsPaySuccessEvent;
import com.mzhguqvn.mzhguq.event.StartBrotherEvent;
import com.mzhguqvn.mzhguq.pay.PayUtil;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrClassicFrameLayout;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrDefaultHandler;
import com.mzhguqvn.mzhguq.pull_loadmore.PtrFrameLayout;
import com.mzhguqvn.mzhguq.ui.view.CustomListView;
import com.mzhguqvn.mzhguq.ui.view.verticalrollingtextview.DataSetAdapter;
import com.mzhguqvn.mzhguq.ui.view.verticalrollingtextview.VerticalRollingTextView;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.GetAssestDataUtil;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.mzhguqvn.mzhguq.util.SharedPreferencesUtil;
import com.mzhguqvn.mzhguq.util.TextCheckUtils;
import com.mzhguqvn.mzhguq.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:商品页
 * package:com.fldhqd.nspmalf.ui.fragment.shop
 * Author：scene on 2017/5/9 10:11
 */

public class ShopFragment extends BaseMainFragment {

    private static final int MSG_LOAD_SUCCESS = 10000;

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
    @BindView(R.id.number_less)
    TextView numberLess;
    @BindView(R.id.numbers_1)
    TextView numbers1;
    @BindView(R.id.number_add)
    TextView numberAdd;
    @BindView(R.id.numbers_2)
    TextView numbers2;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.receiver_name)
    EditText receiverName;
    @BindView(R.id.receiver_phone)
    EditText receiverPhone;
    @BindView(R.id.province)
    TextView province;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.receiver_address)
    EditText receiverAddress;
    @BindView(R.id.pay_way_radiogroup)
    RadioGroup payWayRadiogroup;
    @BindView(R.id.notice_textView)
    VerticalRollingTextView noticeTextView;
    @BindView(R.id.submit_order)
    TextView submitOrder;
    @BindView(R.id.comment_size)
    TextView commentSize;
    @BindView(R.id.comment_listView)
    CustomListView commentListView;
    @BindView(R.id.see_all_comment)
    TextView seeAllComment;
    @BindView(R.id.buy_now_fly)
    ImageView buyNowFly;
    @BindView(R.id.send_glod_vip)
    ImageView sendGlodVip;
    @BindView(R.id.comment_layout)
    LinearLayout commentLayout;
    @BindView(R.id.old_price)
    TextView oldPrice;
    @BindView(R.id.goods_old_price)
    TextView goodsOldPrice;
    @BindView(R.id.countdown)
    CountdownView countdownView;

    private RequestCall dataRequestCall;
    private RequestCall commentRequestCall;

    private GoodsInfo goodsInfo;

    //选择省市区
    private List<ProvinceInfo> options1Items = new ArrayList<>();
    private List<ArrayList<String>> options2Items = new ArrayList<>();
    private List<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String strProvince = "";
    private String strCity = "";
    private String strArea = "";
    private String strAddress = "";
    private String strReceiverName = "";
    private String strReceiverPhone = "";
    private int positionProvince = 0;
    private int positionCity = 0;
    private int positionArea = 0;
    //数量和价格
    private int buyNumber = 1;
    //支付方式
    private int paywayType = 1;
    //滚动的文本
    private List<String> noticeList = new ArrayList<>();
    //评论
    private ArrayList<GoodsCommentInfo> commentList = new ArrayList<>();
    private GoodsCommentAdapter goodsCommentAdapter;
    //折扣
    public static final double DISCOUNT = 0.68d;

    //数据解析完成的通知
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_SUCCESS:
                    getData(true);
                    //显示默认的地址
                    receiverAddress.setText(strAddress);
                    province.setText(strProvince);
                    city.setText(strCity);
                    area.setText(strArea);
                    break;
            }
        }
    };

    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        initAddressData();
        uploadCurrentPage();
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        //支付方式
        paywayType = payWayRadiogroup.getCheckedRadioButtonId() == R.id.pay_way_wechat ? 1 : 2;
        payWayRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.pay_way_wechat) {
                    paywayType = 1;
                } else {
                    paywayType = 2;
                }
            }
        });

        //获取保存的收货地址
        positionProvince = SharedPreferencesUtil.getInt(getContext(), AddressConfig.ARG_PROVINCE_position, 0);
        positionCity = SharedPreferencesUtil.getInt(getContext(), AddressConfig.ARG_CITY_POSITION, 0);
        positionArea = SharedPreferencesUtil.getInt(getContext(), AddressConfig.ARG_AREA_POSITION, 0);
        strProvince = SharedPreferencesUtil.getString(getContext(), AddressConfig.ARG_PROVINCE_KEY, "");
        strCity = SharedPreferencesUtil.getString(getContext(), AddressConfig.ARG_CITY_KEY, "");
        strArea = SharedPreferencesUtil.getString(getContext(), AddressConfig.ARG_AREA_KEY, "");
        strAddress = SharedPreferencesUtil.getString(getContext(), AddressConfig.ARG_ADDRESS, "");
        strReceiverName = SharedPreferencesUtil.getString(getContext(), AddressConfig.ARG_RECEIVER_NAME, "");
        strReceiverPhone = SharedPreferencesUtil.getString(getContext(), AddressConfig.ARG_RECEIVER_PHONE, "");
        receiverName.setText(strReceiverName);
        receiverPhone.setText(strReceiverPhone);
        province.setText(strProvince);
        city.setText(strCity);
        area.setText(strArea);
        receiverAddress.setText(strAddress);
        countdownView.start(120 * 60 * 1000);
    }


    /**
     * Case By:获取数据
     * Author: scene on 2017/5/9 14:48
     */
    private void getData(final boolean isShowLoading) {
        if (isShowLoading && !statusViewLayout.isLoading()) {
            statusViewLayout.showLoading();
        }
        if (NetWorkUtils.isNetworkConnected(_mActivity)) {
            dataRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.GOODS_DETAIL + "/" + App.USER_ID).build();
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
                        getCommentData(isShowLoading);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isShowLoading) {
                            statusViewLayout.showNetError(retryListener);
                        } else {
                            ptrLayout.refreshComplete();
                        }
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

    /**
     * Case By:获取评论数据
     * Author: scene on 2017/5/10 11:52
     */
    private void getCommentData(final boolean isShowLoading) {
        commentRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.GOODS_COMMENT).build();
        commentRequestCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
                if (isShowLoading) {
                    statusViewLayout.showFailed(retryListener);
                } else {
                    ptrLayout.refreshComplete();
                }
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    commentList.clear();
                    commentList.addAll(JSON.parseArray(s, GoodsCommentInfo.class));
                    if (commentList == null || commentList.size() == 0) {
                        seeAllComment.setVisibility(View.GONE);
                        commentLayout.setVisibility(View.GONE);
                    } else {
                        commentSize.setText("宝贝评价（" + commentList.size() + "）");
                        seeAllComment.setVisibility(View.VISIBLE);
                        commentLayout.setVisibility(View.VISIBLE);
                        if (goodsCommentAdapter == null) {
                            goodsCommentAdapter = new GoodsCommentAdapter(getContext(), commentList);
                            commentListView.setAdapter(goodsCommentAdapter);
                        } else {
                            goodsCommentAdapter.notifyDataSetChanged();
                        }
                    }
                    if (isShowLoading) {
                        statusViewLayout.showContent();
                    } else {
                        ptrLayout.refreshComplete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (isShowLoading) {
                        statusViewLayout.showNetError(retryListener);
                    } else {
                        ptrLayout.refreshComplete();
                    }
                }
            }
        });
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
        saleNumber.setText("" + goodsInfo.getSales());
        shopAddress.setText(goodsInfo.getAddress());
        kuaidiPrice.setText(goodsInfo.getDelivery_money() == 0 ? "免费" : "￥" + goodsInfo.getDelivery_money());
        goodsName.setText(goodsInfo.getName());
        Glide.with(getContext()).load(goodsInfo.getThumb()).into(goodsImage);
        numbers1.setText("1");
        numbers2.setText("1");
        oldPrice.setText("原价：￥" + goodsInfo.getPrice());
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        goodsOldPrice.setText("原价：￥" + goodsInfo.getPrice());
        goodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (App.isVip > 0) {
            oldPrice.setVisibility(View.VISIBLE);
            goodsOldPrice.setVisibility(View.VISIBLE);

            price.setText("优惠价：￥" + new BigDecimal((goodsInfo.getPrice() * DISCOUNT)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            goodsPrice.setText("￥" + new BigDecimal((goodsInfo.getPrice() * DISCOUNT)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            totalPrice.setText("￥" + new BigDecimal((goodsInfo.getPrice() * DISCOUNT)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        } else {
            oldPrice.setVisibility(View.GONE);
            goodsOldPrice.setVisibility(View.GONE);
            price.setText("单价：￥" + goodsInfo.getPrice());
            goodsPrice.setText("￥" + goodsInfo.getPrice());
            totalPrice.setText("￥" + goodsInfo.getPrice());
        }

        //滚动文本
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            noticeList.add("用户：" + (random.nextInt(50000) + 12345) + "订购的" + goodsInfo.getName() + "已经发货");
        }
        noticeTextView.setDataSetAdapter(new DataSetAdapter<String>(noticeList) {
            @Override
            protected String text(String s) {
                return s;
            }
        });
        noticeTextView.run();

        if (goodsInfo.getImages() == null) {
            if (isShowLoading) {
                statusViewLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
            return;
        }
        List<String> images = goodsInfo.getImages();
        //图片数组
        ImageView imageViews[] = {image1, image2, image3, image4, image5, image6, image7, image8,
                image9, image10, image11, image12, image13, image14, image15};
        for (int i = 0; i < images.size(); i++) {
            Glide.with(getContext()).load(images.get(i)).into(imageViews[i]);
        }

    }


    /**
     * Case By:初始化省市区的数据
     * Author: scene on 2017/5/10 8:58
     */
    private void initAddressData() {
        statusViewLayout.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 写子线程中的操作,解析省市区数据
                String provinceJsonStr = new GetAssestDataUtil().getAssestJson(getContext(), "province.json");
                List<ProvinceInfo> provinceList = JSON.parseArray(provinceJsonStr, ProvinceInfo.class);
                options1Items = provinceList;
                int provinceListSize = provinceList.size();
                //遍历省份
                for (int i = 0; i < provinceListSize; i++) {
                    //该省的城市列表（第二级）
                    ArrayList<String> CityList = new ArrayList<>();
                    //该省的所有地区列表（第三极）
                    ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();
                    //遍历该省份的所有城市
                    for (int c = 0; c < provinceList.get(i).getCity().size(); c++) {
                        String CityName = provinceList.get(i).getCity().get(c).getName();
                        //添加城市
                        CityList.add(CityName);
                        //该城市的所有地区列表
                        ArrayList<String> City_AreaList = new ArrayList<>();

                        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                        if (provinceList.get(i).getCity().get(c).getArea() == null
                                || provinceList.get(i).getCity().get(c).getArea().size() == 0) {
                            City_AreaList.add("");
                        } else {
                            //该城市对应地区所有数据
                            for (int d = 0; d < provinceList.get(i).getCity().get(c).getArea().size(); d++) {
                                String AreaName = provinceList.get(i).getCity().get(c).getArea().get(d);
                                //添加该城市所有地区数据
                                City_AreaList.add(AreaName);
                            }
                        }
                        //添加该省所有地区数据
                        Province_AreaList.add(City_AreaList);
                    }

                    //添加城市数据
                    options2Items.add(CityList);
                    //添加地区数据
                    options3Items.add(Province_AreaList);
                }
                mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
            }
        }).start();
    }

    /**
     * Case By:弹出选择器
     * Author: scene on 2017/5/10 9:12
     */
    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                strProvince = options1Items.get(options1).getPickerViewText();
                strCity = options2Items.get(options1).get(options2);
                strArea = options3Items.get(options1).get(options2).get(options3);
                positionProvince = options1;
                positionCity = options2;
                positionArea = options3;
                province.setText(strProvince);
                city.setText(strCity);
                area.setText(strArea);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .setSelectOptions(positionProvince, positionCity, positionArea)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * Case By:省市区的选择事件
     * Author: scene on 2017/5/10 9:38
     */
    @OnClick({R.id.province, R.id.city, R.id.area})
    public void onChooseAddress() {
        showPickerView();
    }

    /**
     * Case By:点击减少数量的按钮
     * Author: scene on 2017/5/10 9:39
     */
    @OnClick(R.id.number_less)
    public void onClickNumLess() {
        //大于1才能减少
        if (buyNumber > 1) {
            buyNumber--;
            numbers1.setText(buyNumber + "");
            numbers2.setText(buyNumber + "");
            if (App.isVip > 0) {
                totalPrice.setText("￥" + new BigDecimal((buyNumber * goodsInfo.getPrice() * DISCOUNT)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            } else {
                totalPrice.setText("￥" + buyNumber * goodsInfo.getPrice());
            }
        }

    }

    /**
     * Case By:点击添加数量的按钮
     * Author: scene on 2017/5/10 9:40
     */
    @OnClick(R.id.number_add)
    public void onClickNumAdd() {
        buyNumber++;
        numbers1.setText(buyNumber + "");
        numbers2.setText(buyNumber + "");
        if (App.isVip > 0) {
            totalPrice.setText("￥" + new BigDecimal((buyNumber * goodsInfo.getPrice() * DISCOUNT)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        } else {
            totalPrice.setText("￥" + buyNumber * goodsInfo.getPrice());
        }
    }


    /**
     * Case By:提交订单
     * Author: scene on 2017/5/10 10:26
     */
    @OnClick(R.id.submit_order)
    public void onClickSubmitOrder() {
        String strReceiverName = receiverName.getText().toString().trim();
        if (strReceiverName.isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请输入收货人");
            return;
        }
        String strPhone = receiverPhone.getText().toString().trim();
        if (strPhone.isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请输入联系电话");
            return;
        }
        if (!TextCheckUtils.isMobileNO(strPhone)) {
            ToastUtils.getInstance(getContext()).showToast("请输入正确的手机号");
            return;
        }
        if (strPhone.isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请输入联系电话");
            return;
        }
        if (strProvince.isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请选择所在地区");
            return;
        }
        strAddress = receiverAddress.getText().toString().trim();
        if (strAddress.isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请输入详细地址");
            return;
        }
        double needPayPrice;
        if (App.isVip > 0) {
            needPayPrice = new BigDecimal(buyNumber * goodsInfo.getPrice() * DISCOUNT).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            needPayPrice = new BigDecimal(buyNumber * goodsInfo.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        CreateGoodsOrderInfo createGoodsOrderInfo = new CreateGoodsOrderInfo();
        createGoodsOrderInfo.setGoods_id(goodsInfo.getId());
        createGoodsOrderInfo.setUser_id(App.USER_ID);
        createGoodsOrderInfo.setRemark("购买商品：" + goodsInfo.getName());
        createGoodsOrderInfo.setNumber(buyNumber);
        createGoodsOrderInfo.setMoney(needPayPrice);
        createGoodsOrderInfo.setVersion(PayConfig.VERSION_NAME);
        createGoodsOrderInfo.setPay_type(paywayType);
        createGoodsOrderInfo.setMobile(strPhone);
        createGoodsOrderInfo.setName(strReceiverName);
        createGoodsOrderInfo.setAddress(strAddress);
        createGoodsOrderInfo.setProvince(strProvince);
        createGoodsOrderInfo.setCity(strCity);
        createGoodsOrderInfo.setArea(strArea);
        createGoodsOrderInfo.setAddress(strAddress);
        PayUtil.getInstance().buyGoods2Pay(_mActivity, createGoodsOrderInfo, paywayType, false);
        //保存联系人信息
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_PROVINCE_KEY, strProvince);
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_CITY_KEY, strCity);
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_AREA_KEY, strArea);
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_ADDRESS, strAddress);
        SharedPreferencesUtil.putInt(getContext(), AddressConfig.ARG_PROVINCE_position, positionProvince);
        SharedPreferencesUtil.putInt(getContext(), AddressConfig.ARG_CITY_POSITION, positionCity);
        SharedPreferencesUtil.putInt(getContext(), AddressConfig.ARG_AREA_POSITION, positionArea);
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_RECEIVER_NAME, strReceiverName);
        SharedPreferencesUtil.putString(getContext(), AddressConfig.ARG_RECEIVER_PHONE, strPhone);
    }

    /**
     * Case By:查看全部评论
     * Author: scene on 2017/5/10 12:53
     */
    @OnClick(R.id.see_all_comment)
    public void onClickSeeAllComment() {
        EventBus.getDefault().post(new StartBrotherEvent(GoodsCommentFragment.newInstance(commentList)));
    }

    /**
     * Case By:点击购买需要跳转到购买页
     * Author: scene on 2017/5/10 13:57
     */
    @OnClick({R.id.buy_send_vip, R.id.send_glod_vip, R.id.buy_now_fly})
    public void onClickBuyNeed2BuyPage() {
        EventBus.getDefault().post(new StartBrotherEvent(BuyFragment.newInstance(goodsInfo, commentList)));
    }

    @Subscribe
    public void onCheckGoodsSuccess(GoodsPaySuccessEvent event) {
        if (!event.isGoodsBuyPage) {
            ReceiverInfo receiverInfo = new ReceiverInfo();
            receiverInfo.setReceiverAddress(strAddress);
            receiverInfo.setReceiverArea(strArea);
            receiverInfo.setReceiverCity(strCity);
            receiverInfo.setReceiverProvince(strProvince);
            receiverInfo.setReceiverName(receiverName.getText().toString().trim());
            receiverInfo.setReceiverPhone(receiverPhone.getText().toString().trim());
            EventBus.getDefault().post(new StartBrotherEvent(PaySuccessFragment.newInstance(goodsInfo, receiverInfo, buyNumber)));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (noticeTextView != null && !noticeTextView.isRunning()) {
            noticeTextView.run();
        }
    }

    @Override
    public void onDestroyView() {
        if (commentRequestCall != null) {
            commentRequestCall.cancel();
        }
        if (dataRequestCall != null) {
            dataRequestCall.cancel();
        }
        if (noticeTextView != null && noticeTextView.isRunning()) {
            noticeTextView.stop();
        }
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    /**
     * Case By:上报当前页面
     * Author: scene on 2017/4/27 17:05
     */
    private void uploadCurrentPage() {
        Map<String, String> params = new HashMap<>();
        params.put("position_id", "17");
        params.put("user_id", App.USER_ID + "");
        OkHttpUtils.post().url(API.URL_PRE + API.UPLOAD_CURRENT_PAGE).params(params).build().execute(null);
    }
}
