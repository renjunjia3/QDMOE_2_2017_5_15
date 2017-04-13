package com.cyldf.cyldfxv.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cyldf.cyldfxv.R;
import com.cyldf.cyldfxv.util.ScreenUtils;
import com.cyldf.cyldfxv.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:视频详情-视频截图
 * package:com.cyldf.cyldfxv.adapter
 * Author：scene on 2017/4/13 9:06
 */
public class ScreenShotRecyclerViewAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<String> list;
    private LayoutInflater inflater;
    private ScreenUtils screenUtils;

    public ScreenShotRecyclerViewAdapter(Activity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
        screenUtils = ScreenUtils.instance(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_index_video_detail_screen_shot_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(activity).load(list.get(position)).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        if (position == list.size() - 1) {
            viewHolder.space.setVisibility(View.GONE);
        } else {
            viewHolder.space.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.space)
        View space;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            ViewUtils.setViewHeightByViewGroup(image, (int) screenUtils.dip2px(150));
            ViewUtils.setDialogViewWidth(image, (int) (screenUtils.dip2px(150) * 16f / 9f));
        }
    }
}
