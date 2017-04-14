package com.dhuadhuei.hdeuji.ui.fragment.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhuadhuei.hdeuji.R;
import com.dhuadhuei.hdeuji.adapter.FragmentViewPagerAdapter;
import com.dhuadhuei.hdeuji.base.BaseMainFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 * Created by scene on 2017/3/13.
 */

public class IndexFragment extends BaseMainFragment {

    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static IndexFragment newInstance() {
        Bundle args = new Bundle();
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tab.addTab(tab.newTab());
        tab.addTab(tab.newTab());
        tab.addTab(tab.newTab());
        tab.addTab(tab.newTab());
        tab.addTab(tab.newTab());
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HotRecommendFragment.newInstance());
        fragments.add(OtherTypeFragment.newInstance(OtherTypeFragment.TYPE_2));
        fragments.add(OtherTypeFragment.newInstance(OtherTypeFragment.TYPE_3));
        fragments.add(OtherTypeFragment.newInstance(OtherTypeFragment.TYPE_4));
        fragments.add(OtherTypeFragment.newInstance(OtherTypeFragment.TYPE_5));
        fragments.add(OtherTypeFragment.newInstance(OtherTypeFragment.TYPE_6));
        new FragmentViewPagerAdapter(getChildFragmentManager(), viewPager, fragments);
        tab.setupWithViewPager(viewPager);
    }
}
