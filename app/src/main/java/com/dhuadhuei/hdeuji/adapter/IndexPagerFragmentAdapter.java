package com.dhuadhuei.hdeuji.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dhuadhuei.hdeuji.ui.fragment.index.HotRecommendFragment;
import com.dhuadhuei.hdeuji.ui.fragment.index.OtherTypeFragment;

/**
 * 最新
 * Created by scene on 16/6/5.
 */
public class IndexPagerFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{"热门推荐", "日韩色片", "丝袜制服", "熟女人妻", "丰乳肥臀"};

    public IndexPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return HotRecommendFragment.newInstance();
        } else {
            return OtherTypeFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return mTab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTab[position];
    }
}
