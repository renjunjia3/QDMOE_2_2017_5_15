package com.enycea.owlumguk.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.enycea.owlumguk.R;
import com.enycea.owlumguk.app.App;
import com.enycea.owlumguk.base.BaseFragment;
import com.enycea.owlumguk.event.StartBrotherEvent;
import com.enycea.owlumguk.event.TabSelectedEvent;
import com.enycea.owlumguk.ui.dialog.SubmitAndCancelDialog;
import com.enycea.owlumguk.ui.fragment.bbs.BBSFragment;
import com.enycea.owlumguk.ui.fragment.film.FilmFragment;
import com.enycea.owlumguk.ui.fragment.magnet.MagnetFragment;
import com.enycea.owlumguk.ui.fragment.mine.HotLineFragment;
import com.enycea.owlumguk.ui.fragment.mine.Mine2Fragment;
import com.enycea.owlumguk.ui.fragment.mine.MineFragment;
import com.enycea.owlumguk.ui.fragment.rank.RankFragment;
import com.enycea.owlumguk.ui.fragment.vip.BlackGlodVipFragment;
import com.enycea.owlumguk.ui.fragment.vip.DiamondVipFragment;
import com.enycea.owlumguk.ui.fragment.vip.GlodVipFragment;
import com.enycea.owlumguk.ui.fragment.vip.TrySeeFragment;
import com.enycea.owlumguk.ui.view.BottomBar;
import com.enycea.owlumguk.ui.view.BottomBarTab;
import com.enycea.owlumguk.util.API;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;


/**
 * Case By: 主Fragment
 * package:com.hfaufhreu.hjfeuio.ui.fragment
 * Author：scene on 2017/4/18 9:06
 */

public class MainFragment extends BaseFragment {
    private static final int REQ_MSG = 10;

    public static final int TAB_1 = 0;
    public static final int TAB_2 = 1;
    public static final int TAB_3 = 2;
    public static final int TAB_4 = 3;
    public static final int TAB_5 = 4;

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


    //提示框
    private SubmitAndCancelDialog submitAndCancelDialog;
    private SubmitAndCancelDialog.Builder submitAndCancelBuilder;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            tabNames.clear();
            fragments.clear();
            switch (App.isVip) {
                case 0://试看
                    fragments.add(TrySeeFragment.newInstance());
                    fragments.add(GlodVipFragment.newInstance());
                    fragments.add(MagnetFragment.newInstance());
                    fragments.add(RankFragment.newInstance());
                    fragments.add(BBSFragment.newInstance());

                    tabNames.add("体验区");
                    tabNames.add("会员区");
                    tabNames.add("磁力链");
                    tabNames.add("排行榜");
                    tabNames.add("福利社");
                    break;
                case 1://黄金会员
                    fragments.add(GlodVipFragment.newInstance());
                    fragments.add(DiamondVipFragment.newInstance());
                    fragments.add(MagnetFragment.newInstance());
                    fragments.add(RankFragment.newInstance());
                    fragments.add(BBSFragment.newInstance());
                    tabNames.add("黄金区");
                    tabNames.add("钻石区");
                    tabNames.add("磁力链");
                    tabNames.add("排行榜");
                    tabNames.add("福利社");
                    break;
                case 2://钻石会员
                    if (App.isHeijin == 1) {
                        fragments.add(BlackGlodVipFragment.newInstance());
                        fragments.add(FilmFragment.newInstance());
                        fragments.add(RankFragment.newInstance());
                        fragments.add(MineFragment.newInstance());
                        tabNames.add("黑金区");
                        tabNames.add("片库");
                        tabNames.add("排行榜");
                        tabNames.add("我的");
                    } else {
                        fragments.add(DiamondVipFragment.newInstance());
                        fragments.add(BlackGlodVipFragment.newInstance());
                        fragments.add(FilmFragment.newInstance());
                        fragments.add(RankFragment.newInstance());
                        fragments.add(MineFragment.newInstance());
                        tabNames.add("钻石区");
                        tabNames.add("黑金区");
                        tabNames.add("片库");
                        tabNames.add("排行榜");
                        tabNames.add("我的");
                    }
                    break;
                case 3://VPN海外会员
                    if (App.isHeijin == 1) {
                        fragments.add(BlackGlodVipFragment.newInstance());
                        fragments.add(FilmFragment.newInstance());
                        fragments.add(RankFragment.newInstance());
                        fragments.add(MineFragment.newInstance());
                        tabNames.add("黑金区");
                        tabNames.add("片库");
                        tabNames.add("排行榜");
                        tabNames.add("我的");
                    } else {
                        fragments.add(DiamondVipFragment.newInstance());
                        fragments.add(BlackGlodVipFragment.newInstance());
                        fragments.add(FilmFragment.newInstance());
                        fragments.add(RankFragment.newInstance());
                        fragments.add(MineFragment.newInstance());

                        tabNames.add("海外钻石");
                        tabNames.add("海外黑金");
                        tabNames.add("片库");
                        tabNames.add("排行榜");
                        tabNames.add("我的");
                    }
                    break;
                case 4://海外服务商
                    if (App.isHeijin == 1) {
                        fragments.add(BlackGlodVipFragment.newInstance());
                        fragments.add(FilmFragment.newInstance());
                        fragments.add(RankFragment.newInstance());
                        fragments.add(MineFragment.newInstance());

                        tabNames.add("海外黑金");
                        tabNames.add("片库");
                        tabNames.add("排行榜");
                        tabNames.add("我的");
                    } else {
                        fragments.add(DiamondVipFragment.newInstance());
                        fragments.add(BlackGlodVipFragment.newInstance());
                        fragments.add(FilmFragment.newInstance());
                        fragments.add(RankFragment.newInstance());
                        fragments.add(MineFragment.newInstance());

                        tabNames.add("海外钻石");
                        tabNames.add("海外黑金");
                        tabNames.add("片库");
                        tabNames.add("排行榜");
                        tabNames.add("我的");
                    }
                    break;
                case 5://黑金会员
                    fragments.add(BlackGlodVipFragment.newInstance());
                    fragments.add(FilmFragment.newInstance());
                    fragments.add(RankFragment.newInstance());
                    fragments.add(MineFragment.newInstance());

                    tabNames.add("海外黑金");
                    tabNames.add("片库");
                    tabNames.add("排行榜");
                    tabNames.add("我的");
                    break;
                case 6://海外通道
                    fragments.add(BlackGlodVipFragment.newInstance());
                    fragments.add(FilmFragment.newInstance());
                    fragments.add(RankFragment.newInstance());
                    fragments.add(MineFragment.newInstance());

                    tabNames.add("急速黑金");
                    tabNames.add("片库");
                    tabNames.add("排行榜");
                    tabNames.add("我的");
                    break;
                case 7://海外双线
                    fragments.add(BlackGlodVipFragment.newInstance());
                    fragments.add(FilmFragment.newInstance());
                    fragments.add(RankFragment.newInstance());
                    fragments.add(MineFragment.newInstance());

                    tabNames.add("超速黑金");
                    tabNames.add("片库");
                    tabNames.add("排行榜");
                    tabNames.add("我的");
                    break;
            }
            if (fragments.size() == 4) {
                loadMultipleRootFragment(R.id.fl_container, TAB_1,
                        fragments.get(TAB_1),
                        fragments.get(TAB_2),
                        fragments.get(TAB_3),
                        fragments.get(TAB_4));
            } else {
                loadMultipleRootFragment(R.id.fl_container, TAB_1,
                        fragments.get(TAB_1),
                        fragments.get(TAB_2),
                        fragments.get(TAB_3),
                        fragments.get(TAB_4),
                        fragments.get(TAB_5));
            }
        } else {
            switch (App.isVip) {
                case 0:
                    fragments.add(findChildFragment(TrySeeFragment.class));
                    fragments.add(findChildFragment(GlodVipFragment.class));
                    fragments.add(findChildFragment(MagnetFragment.class));
                    fragments.add(findChildFragment(RankFragment.class));
                    fragments.add(findChildFragment(BBSFragment.class));
                    break;
                case 1:
                    fragments.add(findChildFragment(GlodVipFragment.class));
                    fragments.add(findChildFragment(DiamondVipFragment.class));
                    fragments.add(findChildFragment(MagnetFragment.class));
                    fragments.add(findChildFragment(RankFragment.class));
                    fragments.add(findChildFragment(BBSFragment.class));
                    break;
                case 2:
                    if(App.isHeijin==1){
                        fragments.add(findChildFragment(BlackGlodVipFragment.class));
                        fragments.add(findChildFragment(FilmFragment.class));
                        fragments.add(findChildFragment(RankFragment.class));
                        fragments.add(findChildFragment(MineFragment.class));
                    }else{
                        fragments.add(findChildFragment(DiamondVipFragment.class));
                        fragments.add(findChildFragment(BlackGlodVipFragment.class));
                        fragments.add(findChildFragment(FilmFragment.class));
                        fragments.add(findChildFragment(RankFragment.class));
                        fragments.add(findChildFragment(MineFragment.class));
                    }

                    break;
                case 3:
                    if (App.isHeijin == 1) {
                        fragments.add(findChildFragment(BlackGlodVipFragment.class));
                        fragments.add(findChildFragment(FilmFragment.class));
                        fragments.add(findChildFragment(RankFragment.class));
                        fragments.add(findChildFragment(MineFragment.class));

                    } else {
                        fragments.add(findChildFragment(DiamondVipFragment.class));
                        fragments.add(findChildFragment(BlackGlodVipFragment.class));
                        fragments.add(findChildFragment(FilmFragment.class));
                        fragments.add(findChildFragment(RankFragment.class));
                        fragments.add(findChildFragment(MineFragment.class));
                    }

                    break;
                case 4:
                    if (App.isHeijin == 1) {
                        fragments.add(findChildFragment(BlackGlodVipFragment.class));
                        fragments.add(findChildFragment(FilmFragment.class));
                        fragments.add(findChildFragment(RankFragment.class));
                        fragments.add(findChildFragment(MineFragment.class));
                    } else {
                        fragments.add(findChildFragment(DiamondVipFragment.class));
                        fragments.add(findChildFragment(BlackGlodVipFragment.class));
                        fragments.add(findChildFragment(FilmFragment.class));
                        fragments.add(findChildFragment(RankFragment.class));
                        fragments.add(findChildFragment(MineFragment.class));
                    }
                    break;
                case 5:
                    fragments.add(findChildFragment(BlackGlodVipFragment.class));
                    fragments.add(findChildFragment(FilmFragment.class));
                    fragments.add(findChildFragment(RankFragment.class));
                    fragments.add(findChildFragment(MineFragment.class));
                    break;
                case 6:
                    fragments.add(findChildFragment(BlackGlodVipFragment.class));
                    fragments.add(findChildFragment(FilmFragment.class));
                    fragments.add(findChildFragment(RankFragment.class));
                    fragments.add(findChildFragment(MineFragment.class));
                    break;
                case 7:
                    fragments.add(findChildFragment(BlackGlodVipFragment.class));
                    fragments.add(findChildFragment(FilmFragment.class));
                    fragments.add(findChildFragment(RankFragment.class));
                    fragments.add(findChildFragment(MineFragment.class));
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
        switch (App.isVip) {
            case 0:
                toUser.setImageResource(R.drawable.ic_toolbar_vip_try_see);
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_try_see, tabNames.get(0)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_glod, tabNames.get(1)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_magnet, tabNames.get(2)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(3)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_bbs, tabNames.get(4)));
                break;
            case 1:
                toUser.setImageResource(R.drawable.ic_toolbar_vip_glod);
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_glod, tabNames.get(0)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_diamond, tabNames.get(1)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_magnet, tabNames.get(2)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(3)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_bbs, tabNames.get(4)));
                break;
            case 2:
                if(App.isHeijin==1){
                    toUser.setImageResource(R.drawable.ic_toolbar_vip_black_glod);
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(0)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_flim, tabNames.get(1)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(2)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(3)));
                }else{
                    toUser.setImageResource(R.drawable.ic_toolbar_vip_diamond);
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_diamond, tabNames.get(0)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(1)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_flim, tabNames.get(2)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(3)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(4)));
                }
                break;
            case 3:
                if (App.isHeijin == 1) {
                    toUser.setImageResource(R.drawable.ic_toolbar_vip_black_glod);
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(0)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_flim, tabNames.get(1)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(2)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(3)));
                } else {
                    toUser.setImageResource(R.drawable.ic_toolbar_vip_diamond);
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_diamond, tabNames.get(0)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(1)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_flim, tabNames.get(2)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(3)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(4)));
                }


                break;
            case 4:
                if (App.isHeijin == 1) {
                    toUser.setImageResource(R.drawable.ic_toolbar_vip_black_glod);
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(0)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_flim, tabNames.get(1)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(2)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(3)));
                } else {
                    toUser.setImageResource(R.drawable.ic_toolbar_vip_diamond);
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_diamond, tabNames.get(0)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(1)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_flim, tabNames.get(2)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(3)));
                    mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(4)));
                }
                break;
            case 5:
                toUser.setImageResource(R.drawable.ic_toolbar_vip_black_glod);
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(0)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_flim, tabNames.get(1)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(2)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(3)));
                break;
            case 6:
                toUser.setImageResource(R.drawable.ic_toolbar_vip_black_glod);
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(0)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_flim, tabNames.get(1)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(2)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(3)));
                break;
            case 7:
                toUser.setImageResource(R.drawable.ic_toolbar_vip_black_glod);
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_black_glod, tabNames.get(0)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_index, tabNames.get(1)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_rank, tabNames.get(2)));
                mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine, tabNames.get(3)));
                break;
        }

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(fragments.get(position), fragments.get(prePosition));
                name.setText(tabNames.get(position));
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
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

    @OnClick({R.id.search, R.id.to_user})
    public void onClickTop(View v) {
        if (v.getId() == R.id.search) {
            EventBus.getDefault().post(new StartBrotherEvent(MagnetFragment.newInstance()));
        } else {
            if (fragments.get(fragments.size() - 1) instanceof MineFragment) {
                mBottomBar.setCurrentItem(fragments.size() - 1);
            } else {
                EventBus.getDefault().post(new StartBrotherEvent(Mine2Fragment.newInstance()));
            }

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
        OkHttpUtils.get().url(API.URL_PRE + API.PAY_CLICK + App.IMEI).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s, int i) {

            }
        });
    }


}
