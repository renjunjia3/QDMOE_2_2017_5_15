package com.ebcnke.knulg.base;

import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by scene on 17/3/13.
 */
public class BaseFragment extends SupportFragment {
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
}
