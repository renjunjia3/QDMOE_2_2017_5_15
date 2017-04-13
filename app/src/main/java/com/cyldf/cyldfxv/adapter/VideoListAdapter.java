package com.cyldf.cyldfxv.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cyldf.cyldfxv.R;
import com.cyldf.cyldfxv.base.BaseFragment;
import com.cyldf.cyldfxv.base.BaseRecyclerAdapter;
import com.cyldf.cyldfxv.util.ScreenUtils;
import com.cyldf.cyldfxv.util.ViewUtils;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.VideoInfo;

/**
 * 视频列表每行3个适配器
 * Created by scene on 2017/3/13.
 */

public class VideoListAdapter extends BaseRecyclerAdapter {

    private BaseFragment baseFragment;
    private List<VideoInfo> list;

    private static ScreenUtils screenUtils;

    private String colors[] = {"#3399FF", "#FF3300", "#00CC66", "#9966FF"};

    public VideoListAdapter(BaseFragment baseFragment, List<VideoInfo> list) {
        super(baseFragment);
        this.baseFragment = baseFragment;
        this.list = list;
        screenUtils = ScreenUtils.instance(baseFragment.getContext());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(
                baseFragment.getContext()).inflate(R.layout.fragment_index_other_type_item, parent, false));
        return holder;
    }

    @Override
    protected List<?> setDataList() {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        Holder mHolder = (Holder) holder;
        VideoInfo videoInfo = list.get(position);
        mHolder.name.setText(videoInfo.getTitle());
        mHolder.tag.setText(videoInfo.getAttr());
        mHolder.tag.setBackgroundColor(Color.parseColor(colors[new Random().nextInt(4)]));
        mHolder.playTime.setText(videoInfo.getHits() + "次播放");
        Glide.with(baseFragment).load(videoInfo.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(mHolder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.play_time)
        TextView playTime;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(40f)) * 205f / 3f / 145f);
            ViewUtils.setViewHeightByViewGroup(image, height);
        }
    }
}
