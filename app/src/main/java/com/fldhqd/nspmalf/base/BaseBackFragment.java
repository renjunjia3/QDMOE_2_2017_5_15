package com.fldhqd.nspmalf.base;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fldhqd.nspmalf.R;

import butterknife.Unbinder;
import me.yokeyword.fragmentation.SwipeBackFragment;

/**
 * Created by scene on 17/3/13.
 */
public class BaseBackFragment extends SwipeBackFragment {
    protected Unbinder unbinder;

    /**
     * 销毁butterknife
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }
}
