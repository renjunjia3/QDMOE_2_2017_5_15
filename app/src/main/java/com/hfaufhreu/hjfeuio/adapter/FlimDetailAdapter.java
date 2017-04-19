package com.hfaufhreu.hjfeuio.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.bean.VideoInfo;
import com.hfaufhreu.hjfeuio.util.ScreenUtils;
import com.hfaufhreu.hjfeuio.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/19 17:44
 */

public class FlimDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<VideoInfo> list;

    private OnClickItemVideoListener onClickItemVideoListener;

    public FlimDetailAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClickItemVideoListener(OnClickItemVideoListener onClickItemVideoListener) {
        this.onClickItemVideoListener = onClickItemVideoListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlimDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_glod_vip_item_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VideoInfo info = list.get(position);
        FlimDetailViewHolder viewHolder = (FlimDetailViewHolder) holder;
        viewHolder.playCount.setText(info.getHits() + "次");
        viewHolder.title.setText(info.getTitle());
        viewHolder.tag.setVisibility(View.GONE);
        viewHolder.title.setTextColor(Color.parseColor("#FFFFFF"));
        ScreenUtils screenUtils = ScreenUtils.instance(context);
        int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(9)) * 9f / 16f / 2f);
        ViewUtils.setViewHeightByViewGroup(viewHolder.image, height);
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemVideoListener != null) {
                    onClickItemVideoListener.onClickItemVideo(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class FlimDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.update_number)
        TextView updateNumber;
        @BindView(R.id.play_count)
        TextView playCount;

        FlimDetailViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickItemVideoListener {
        void onClickItemVideo(int positon);
    }
}
