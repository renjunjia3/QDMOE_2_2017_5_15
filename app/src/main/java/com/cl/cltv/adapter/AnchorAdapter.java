package com.cl.cltv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cl.cltv.R;
import com.cl.cltv.bean.VideoInfo;
import com.cl.cltv.util.GlideUtils;
import com.cl.cltv.util.ScreenUtils;
import com.cl.cltv.util.ViewUtils;
import com.cl.cltv.video.JCUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:主播
 * package:com.mzhguqvn.mzhguq.adapter
 * Author：scene on 2017/5/24 09:56
 */

public class AnchorAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<VideoInfo> list;
    private OnClickAnchorItemListener onClickAnchorItemListener;

    public AnchorAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClickAnchorItemListener(OnClickAnchorItemListener onClickAnchorItemListener) {
        this.onClickAnchorItemListener = onClickAnchorItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AnchorViewHolder viewHolder = new AnchorViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_anchor_item, parent, false));
        ScreenUtils screenUtils = ScreenUtils.instance(context);
        int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dp2px(10)) * 28f / 25f / 2f);
        ViewUtils.setViewHeightByViewGroup(viewHolder.image, height);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VideoInfo info = list.get(position);
        AnchorViewHolder viewHolder = (AnchorViewHolder) holder;
        viewHolder.hits.setText(String.valueOf(info.getHits()));
        viewHolder.title.setText(String.valueOf(info.getTitle()));
        viewHolder.time.setText(JCUtils.stringForTime(info.getReal_duration()));
        ScreenUtils screenUtils = ScreenUtils.instance(context);
        if(position==0){
            int width = screenUtils.getScreenWidth();
            ViewUtils.setDialogViewWidth(viewHolder.image, width);
        }else{
            int width = (screenUtils.getScreenWidth()-screenUtils.dp2px(10))/2;
            ViewUtils.setDialogViewWidth(viewHolder.image, width);
        }
        GlideUtils.loadImage(context, viewHolder.image, info.getThumb());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickAnchorItemListener != null)
                    onClickAnchorItemListener.onClickAnchorItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class AnchorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.hits)
        TextView hits;
        @BindView(R.id.time)
        TextView time;

        AnchorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickAnchorItemListener {
        void onClickAnchorItem(int position);
    }
}
