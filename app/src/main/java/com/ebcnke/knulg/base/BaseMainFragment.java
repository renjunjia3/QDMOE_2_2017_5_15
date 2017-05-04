package com.ebcnke.knulg.base;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ebcnke.knulg.R;
import com.ebcnke.knulg.app.App;
import com.ebcnke.knulg.pay.PayUtil;
import com.ebcnke.knulg.ui.dialog.BackOpenVipDialog;
import com.ebcnke.knulg.ui.fragment.MainFragment;
import com.ebcnke.knulg.util.ToastUtils;

import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * 懒加载
 * Created by scene on 17/3/13.
 */
public abstract class BaseMainFragment extends BaseFragment {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    private BackOpenVipDialog dialog;
    private BackOpenVipDialog.Builder builder;


    protected OnFragmentOpenDrawerListener mOpenDraweListener;

    protected void initToolbarNav(Toolbar toolbar) {
        initToolbarNav(toolbar, false);
    }

    protected void initToolbarNav(Toolbar toolbar, boolean isHome) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOpenDraweListener != null) {
                    mOpenDraweListener.onOpenDrawer();
                }
            }
        });
    }

    /**
     * 处理回退事件
     */
    @Override
    public boolean onBackPressedSupport() {
        if (App.isVip == 0) {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {

                if (builder == null) {
                    builder = new BackOpenVipDialog.Builder(_mActivity);
                    builder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PayUtil.getInstance().payByWeChat(_mActivity, null, PayUtil.VIP_TYPE_2, 0, false);
                        }
                    });

                    builder.setAliPayClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PayUtil.getInstance().payByAliPay(_mActivity, null, PayUtil.VIP_TYPE_2, 0, false);
                        }
                    });
                }
                if (dialog == null) {
                    dialog = builder.create();
                }
                dialog.show();
                MainFragment.clickWantPay();
                MainFragment.openPayDialog(0);

            } else {
                TOUCH_TIME = System.currentTimeMillis();
                ToastUtils.getInstance(_mActivity).showToast(getString(R.string.press_again_exit));
            }
        }
        return true;
    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        FragmentAnimator fragmentAnimator = _mActivity.getFragmentAnimator();
        fragmentAnimator.setEnter(0);
        fragmentAnimator.setExit(0);
        return fragmentAnimator;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentOpenDrawerListener) {
            mOpenDraweListener = (OnFragmentOpenDrawerListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentOpenDrawerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOpenDraweListener = null;
    }

    public interface OnFragmentOpenDrawerListener {
        void onOpenDrawer();
    }
}
