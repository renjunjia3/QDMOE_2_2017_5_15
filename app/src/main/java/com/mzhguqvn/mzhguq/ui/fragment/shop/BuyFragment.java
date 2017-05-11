package com.mzhguqvn.mzhguq.ui.fragment.shop;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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
import com.mzhguqvn.mzhguq.base.BaseBackFragment;
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
import com.mzhguqvn.mzhguq.ui.view.CustomListView;
import com.mzhguqvn.mzhguq.ui.view.verticalrollingtextview.DataSetAdapter;
import com.mzhguqvn.mzhguq.ui.view.verticalrollingtextview.VerticalRollingTextView;
import com.mzhguqvn.mzhguq.util.GetAssestDataUtil;
import com.mzhguqvn.mzhguq.util.SharedPreferencesUtil;
import com.mzhguqvn.mzhguq.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Case By:立即购买
 * package:com.mzhguqvn.mzhguq.ui.fragment.shop
 * Author：scene on 2017/5/10 13:27
 */

public class BuyFragment extends BaseBackFragment {
    private static final int MSG_LOAD_SUCCESS = 10001;
    private static final String ARG_GOODS_INFO = "goods_info";
    private static final String ARG_COMMENT_LIST = "comment_list";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
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
    @BindView(R.id.notice_textView)
    VerticalRollingTextView noticeTextView;
    @BindView(R.id.comment_size)
    TextView commentSize;
    @BindView(R.id.comment_listView)
    CustomListView commentListView;
    @BindView(R.id.comment_layout)
    LinearLayout commentLayout;
    @BindView(R.id.see_all_comment)
    TextView seeAllComment;
    @BindView(R.id.buy_now)
    TextView buyNow;
    @BindView(R.id.text1)
    TextView text;

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
    //支付
    private int paywayType = 1;
    //滚动的文本
    private List<String> noticeList = new ArrayList<>();
    //评论
    private ArrayList<GoodsCommentInfo> commentList;
    private GoodsCommentAdapter goodsCommentAdapter;

    private GoodsInfo info;
    //位置信息是否解析完成
    private boolean isAddressDataSuccess = false;


    //数据解析完成的通知
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_SUCCESS:
                    isAddressDataSuccess = true;
                    //显示默认的地址
                    receiverAddress.setText(strAddress);
                    province.setText(strProvince);
                    city.setText(strCity);
                    area.setText(strArea);
                    break;
            }
        }
    };

    public static BuyFragment newInstance(GoodsInfo goodsInfo, ArrayList<GoodsCommentInfo> commentList) {
        BuyFragment fragment = new BuyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GOODS_INFO, goodsInfo);
        args.putSerializable(ARG_COMMENT_LIST, commentList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            info = (GoodsInfo) args.getSerializable(ARG_GOODS_INFO);
            commentList = (ArrayList<GoodsCommentInfo>) args.getSerializable(ARG_COMMENT_LIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_buy, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        toolbarTitle.setText("购买清单");
        initToolbarNav(toolbar);
        initAddressData();
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
    }

    private void initView() {
        if (info != null) {
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

            Glide.with(getContext()).load(info.getThumb()).into(goodsImage);
            goodsName.setText(info.getName());
            goodsPrice.setText("￥" + info.getPrice());
            numbers1.setText("1");
            numbers2.setText("1");
            totalPrice.setText("￥" + info.getPrice());
            //滚动文本
            Random random = new Random();
            for (int i = 0; i < 50; i++) {
                noticeList.add("用户：" + (random.nextInt(50000) + 12345) + "订购的" + info.getName() + "已经发货");
            }
            noticeTextView.setDataSetAdapter(new DataSetAdapter<String>(noticeList) {
                @Override
                protected String text(String s) {
                    return s;
                }
            });
            noticeTextView.run();

            //绑定评论数据
            if (commentList != null && commentList.size() > 0) {


                goodsCommentAdapter = new GoodsCommentAdapter(getContext(), commentList);
                commentListView.setAdapter(goodsCommentAdapter);
                commentSize.setText("宝贝评价（" + commentList.size() + "）");
            } else {
                commentLayout.setVisibility(View.GONE);
            }
            text.setFocusable(true);
            text.setFocusableInTouchMode(true);
            text.requestFocus();
        }
    }

    /**
     * Case By:初始化省市区的数据
     * Author: scene on 2017/5/10 8:58
     */
    private void initAddressData() {
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
     * Case By:省市区的选择事件
     * Author: scene on 2017/5/10 9:38
     */
    @OnClick({R.id.province, R.id.city, R.id.area})
    public void onChooseAddress() {
        if (isAddressDataSuccess) {
            showPickerView();
        } else {
            ToastUtils.getInstance(getContext()).showToast("正在获取地区信息...");
        }
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
            totalPrice.setText("￥" + buyNumber * info.getPrice());
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
        totalPrice.setText("￥" + buyNumber * info.getPrice());
    }

    /**
     * Case By:查看全部评论
     * Author: scene on 2017/5/10 12:53
     */
    @OnClick(R.id.see_all_comment)
    public void onClickSeeAllComment() {
        start(GoodsCommentFragment.newInstance(commentList));
    }

    /**
     * Case By:立即购买
     * Author: scene on 2017/5/10 14:38
     */
    @OnClick(R.id.buy_now)
    public void onClickBuyNow() {

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
        if (strProvince.isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请选择所在地区");
            return;
        }
        strAddress = receiverAddress.getText().toString().trim();
        if (strAddress.isEmpty()) {
            ToastUtils.getInstance(getContext()).showToast("请输入详细地址");
            return;
        }
        double needPayPrice = buyNumber * info.getPrice();
        CreateGoodsOrderInfo createGoodsOrderInfo = new CreateGoodsOrderInfo();
        createGoodsOrderInfo.setGoods_id(info.getId());
        createGoodsOrderInfo.setUser_id(App.USER_ID);
        createGoodsOrderInfo.setRemark("购买商品：" + info.getName());
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
        PayUtil.getInstance().buyGoods2Pay(_mActivity, createGoodsOrderInfo, paywayType, true);
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
        if (noticeTextView != null && noticeTextView.isRunning()) {
            noticeTextView.stop();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onCheckGoodsSuccess(GoodsPaySuccessEvent event) {
        if (event.isGoodsBuyPage) {
            ReceiverInfo receiverInfo = new ReceiverInfo();
            receiverInfo.setReceiverAddress(strAddress);
            receiverInfo.setReceiverArea(strArea);
            receiverInfo.setReceiverCity(strCity);
            receiverInfo.setReceiverProvince(strProvince);
            receiverInfo.setReceiverName(receiverName.getText().toString().trim());
            receiverInfo.setReceiverPhone(receiverPhone.getText().toString().trim());
            EventBus.getDefault().post(new StartBrotherEvent(PaySuccessFragment.newInstance(info, receiverInfo, buyNumber)));
        }
    }

}
