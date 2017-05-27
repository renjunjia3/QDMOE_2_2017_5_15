package com.mzhguqvn.mzhguq.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.base.BaseFragment;
import com.mzhguqvn.mzhguq.event.StartBrotherEvent;
import com.mzhguqvn.mzhguq.event.TabSelectedEvent;
import com.mzhguqvn.mzhguq.ui.fragment.anchor.AnchorFragment;
import com.mzhguqvn.mzhguq.ui.fragment.gallery.GalleryFragment;
import com.mzhguqvn.mzhguq.ui.fragment.mine.HotLineFragment;
import com.mzhguqvn.mzhguq.ui.fragment.mine.MineFragment;
import com.mzhguqvn.mzhguq.ui.fragment.shop.ShopFragment;
import com.mzhguqvn.mzhguq.ui.fragment.vip.GlodVip1Fragment;
import com.mzhguqvn.mzhguq.ui.fragment.vip.TrySeeFragment;
import com.mzhguqvn.mzhguq.ui.view.BottomBar;
import com.mzhguqvn.mzhguq.ui.view.BottomBarTab;
import com.mzhguqvn.mzhguq.util.API;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Case By: 主Fragment
 * package:com.hfaufhreu.hjfeuio.ui.fragment
 * Author：scene on 2017/4/18 9:06
 */

public class MainFragment extends BaseFragment {

    public static final int TAB_1 = 0;
    public static final int TAB_2 = 1;
    public static final int TAB_3 = 2;
    public static final int TAB_4 = 3;
    public static final int TAB_5 = 4;
    public static final int TAB_6 = 5;

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.to_user)
    ImageView toUser;

    private List<SupportFragment> fragments = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null || tabNames.size() == 0 || fragments.size() == 0) {
            tabNames.clear();
            fragments.clear();
            switch (App.role) {
                case 0://试看
                    fragments.add(TrySeeFragment.newInstance());
                    fragments.add(GlodVip1Fragment.newInstance());
                    fragments.add(ShopFragment.newInstance());
                    fragments.add(MineFragment.newInstance());

                    tabNames.add(getString(R.string.tab_try_see));
                    tabNames.add(getString(R.string.tab_vip));
                    tabNames.add(getString(R.string.tab_shop));
                    tabNames.add(getString(R.string.tab_mine));
                    break;
                case 1://黄金会员
                    if (App.cdn == 0) {
                        fragments.add(GlodVip1Fragment.newInstance());
                        fragments.add(AnchorFragment.newInstance());
                        fragments.add(ShopFragment.newInstance());
                        fragments.add(MineFragment.newInstance());

                        tabNames.add(getString(R.string.tab_glod));
                        tabNames.add(getString(R.string.tab_anchor));
                        tabNames.add(getString(R.string.tab_shop));
                        tabNames.add(getString(R.string.tab_mine));
                    } else {
                        fragments.add(AnchorFragment.newInstance());
                        fragments.add(ShopFragment.newInstance());
                        fragments.add(GalleryFragment.newInstance());
                        fragments.add(MineFragment.newInstance());

                        tabNames.add(getString(R.string.tab_anchor));
                        tabNames.add(getString(R.string.tab_shop));
                        tabNames.add(getString(R.string.tab_gallery));
                        tabNames.add(getString(R.string.tab_mine));
                    }

                    break;
            }

            if (fragments.size() == 4) {
                loadMultipleRootFragment(R.id.fl_container, TAB_1,
                        fragments.get(TAB_1),
                        fragments.get(TAB_2),
                        fragments.get(TAB_3),
                        fragments.get(TAB_4));
            } else if (fragments.size() == 5) {
                loadMultipleRootFragment(R.id.fl_container, TAB_1,
                        fragments.get(TAB_1),
                        fragments.get(TAB_2),
                        fragments.get(TAB_3),
                        fragments.get(TAB_4),
                        fragments.get(TAB_5));
            } else if (fragments.size() == 6) {
                loadMultipleRootFragment(R.id.fl_container, TAB_1,
                        fragments.get(TAB_1),
                        fragments.get(TAB_2),
                        fragments.get(TAB_3),
                        fragments.get(TAB_4),
                        fragments.get(TAB_5),
                        fragments.get(TAB_6));
            }
        } else {
            switch (App.role) {
                case 0:
                    fragments.add(findChildFragment(TrySeeFragment.class));
                    fragments.add(findChildFragment(GlodVip1Fragment.class));
                    fragments.add(findChildFragment(ShopFragment.class));
                    fragments.add(findChildFragment(MineFragment.class));
                    break;
                case 1:
                    if (App.cdn == 0) {
                        fragments.add(findChildFragment(GlodVip1Fragment.class));
                        fragments.add(findChildFragment(AnchorFragment.class));
                        fragments.add(findChildFragment(ShopFragment.class));
                        fragments.add(findChildFragment(MineFragment.class));
                    } else {
                        //已开通cdn
                        fragments.add(findChildFragment(AnchorFragment.class));
                        fragments.add(findChildFragment(ShopFragment.class));
                        fragments.add(findChildFragment(GalleryFragment.class));
                        fragments.add(findChildFragment(MineFragment.class));
                    }
                    break;
            }
        }
        try {
            name.setText(tabNames.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initPayDialog();
        initView();
        return view;
    }

    private void initView() {
        EventBus.getDefault().register(this);
        switch (App.role) {
            case 0:
                toUser.setImageResource(R.drawable.ic_toolbar_vip_try_see);
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_try_see_d, R.drawable.ic_bottom_bar_try_see_s, tabNames.get(0)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_glod_d, R.drawable.ic_bottom_bar_glod_s, tabNames.get(1)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_shop_d, R.drawable.ic_bottom_bar_shop_s, tabNames.get(2)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine_d, R.drawable.ic_bottom_bar_mine_s, tabNames.get(3)));
                break;
            case 1:
                if (App.cdn == 0) {
                    toUser.setImageResource(R.drawable.ic_toolbar_vip_glod);
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_glod_d, R.drawable.ic_bottom_bar_glod_s, tabNames.get(0)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_anchor_d, R.drawable.ic_bottom_bar_anchor_s, tabNames.get(1)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_shop_d, R.drawable.ic_bottom_bar_shop_s, tabNames.get(2)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine_d, R.drawable.ic_bottom_bar_mine_s, tabNames.get(3)));
                } else {
                    toUser.setImageResource(R.drawable.ic_toolbar_vip_glod);
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_anchor_d, R.drawable.ic_bottom_bar_anchor_s, tabNames.get(0)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_shop_d, R.drawable.ic_bottom_bar_shop_s, tabNames.get(1)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_gallery_d, R.drawable.ic_bottom_bar_gallery_s, tabNames.get(2)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine_d, R.drawable.ic_bottom_bar_mine_s, tabNames.get(3)));
                }
                break;
        }

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                hideSoftInput();
                showHideFragment(fragments.get(position), fragments.get(prePosition));
                name.setText(tabNames.get(position));
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                //目前这个不用也可以，作用是可以在切换到fragment的时候做一些处理比如可以让列表回到顶部
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @OnClick(R.id.complaint)
    public void onClickComplaint() {
        EventBus.getDefault().post(new StartBrotherEvent(HotLineFragment.newInstance()));
    }

    @OnClick(R.id.to_user)
    public void onClickTop() {
        if (fragments.get(fragments.size() - 1) instanceof MineFragment) {
            mBottomBar.setCurrentItem(fragments.size() - 1);
        }
    }


    /**
     * Case By:初始化支付的dialog
     * Author: scene on 2017/4/18 18:52
     */
    private void initPayDialog() {

    }


    /**
     * 弹出支付窗口之后调用
     */
    public static void clickWantPay() {
        HashMap<String, String> params = API.createParams();
        OkHttpUtils.get().url(API.URL_PRE + API.PAY_CLICK).params(params).build().execute(null);
    }

}
