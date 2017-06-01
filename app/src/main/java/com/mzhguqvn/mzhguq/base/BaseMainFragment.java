package com.mzhguqvn.mzhguq.base;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.ui.dialog.BackGlodVipDialog;
import com.mzhguqvn.mzhguq.ui.fragment.MainFragment;

import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * 懒加载
 * Created by scene on 17/3/13.
 */
public abstract class BaseMainFragment extends BaseFragment {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    private BackGlodVipDialog dialog;
    private BackGlodVipDialog.Builder builder;


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
        if (App.role == 0) {
            if (builder == null) {
                builder = new BackGlodVipDialog.Builder(_mActivity, 0, false, PageConfig.BACK_OPEN_VIP_POSITOTN_ID);
            }
            if (dialog == null) {
                dialog = builder.create();
            }
            dialog.show();
            MainFragment.clickWantPay();
            MainActivity.upLoadPageInfo(PageConfig.BACK_OPEN_VIP_POSITOTN_ID, 0, 0);
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
