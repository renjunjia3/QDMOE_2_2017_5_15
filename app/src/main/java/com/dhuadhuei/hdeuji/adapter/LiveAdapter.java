package com.dhuadhuei.hdeuji.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;
import com.dhuadhuei.hdeuji.R;
import com.dhuadhuei.hdeuji.base.BaseFragment;
import com.dhuadhuei.hdeuji.base.BaseRecyclerAdapter;
import com.dhuadhuei.hdeuji.bean.LiveInfo;
import com.dhuadhuei.hdeuji.util.ScreenUtils;
import com.dhuadhuei.hdeuji.util.ViewUtils;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/15.
 */

public class LiveAdapter extends BaseRecyclerAdapter {
    private BaseFragment fragment;
    private List<LiveInfo> lists;

    private Random random;
    private static ScreenUtils screenUtils;

    public LiveAdapter(BaseFragment fragment, List<LiveInfo> lists) {
        super(fragment);
        this.fragment = fragment;
        this.lists = lists;
        random = new Random();
        screenUtils = ScreenUtils.instance(fragment.getContext());
    }


    @Override
    protected List<?> setDataList() {
        return lists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(fragment.getActivity()).inflate(R.layout.fragment_live_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder viewHolder = (ViewHolder) holder;
        LiveInfo info = lists.get(position);
        viewHolder.hits.setText(info.getHits());
        viewHolder.name.setText(info.getName());
        viewHolder.address.setText(info.getCity());
        viewHolder.fravetor.setText("(" + info.getFavor() + ")");
        Glide.with(fragment).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        Glide.with(fragment).load(info.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.ic_default_femail).error(R.drawable.ic_default_femail).into(viewHolder.headImage);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.headImage)
        SelectableRoundedImageView headImage;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.hits)
        TextView hits;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.fravetor)
        TextView fravetor;
        @BindView(R.id.bottom_layout)
        RelativeLayout bottomLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(20)) * 330f / 550f);
            ViewUtils.setViewHeightByViewGroup(image, height);
        }
    }
}
