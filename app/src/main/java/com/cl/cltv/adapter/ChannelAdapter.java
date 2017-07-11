package com.cl.cltv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cl.cltv.R;
import com.cl.cltv.bean.ChannelResultInfo;
import com.cl.cltv.ui.view.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:adapter
 * package:com.mzhguqvn.mzhguq.adapter
 * Author：scene on 2017/6/1 16:44
 */

public class ChannelAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ChannelResultInfo.DataBean> list;
    private OnClickChannelItemListener onClickChannelItemListener;

    public ChannelAdapter(Context context, List<ChannelResultInfo.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClickChannelItemListener(OnClickChannelItemListener onClickChannelItemListener) {
        this.onClickChannelItemListener = onClickChannelItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChannelViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_channel_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ChannelViewHolder viewHolder = (ChannelViewHolder) holder;
        ChannelResultInfo.DataBean info = list.get(position);
        viewHolder.name.setText(info.getTitle());
        viewHolder.hits.setText(String.format("%d人观看", info.getHits()));
        Glide.with(context).load(info.getThumb()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickChannelItemListener!=null){
                    onClickChannelItemListener.onClickChannelItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.hits)
        TextView hits;
        @BindView(R.id.image)
        RatioImageView image;
        ChannelViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickChannelItemListener {
        void onClickChannelItem(int position);
    }
}
