package com.mzhguqvn.mzhguq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.GalleryResultInfo;
import com.mzhguqvn.mzhguq.util.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:图库
 * package:com.mzhguqvn.mzhguq.adapter
 * Author：scene on 2017/5/23 17:04
 */

public class GalleryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<GalleryResultInfo.GalleryInfo> list;

    private OnGalleryClickListener onGalleryClickListener;

    public void setOnGalleryClickListener(OnGalleryClickListener onGalleryClickListener) {
        this.onGalleryClickListener = onGalleryClickListener;
    }

    public GalleryAdapter(Context context, List<GalleryResultInfo.GalleryInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GalleryViewHolder viewHolder = new GalleryViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_gallery_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GalleryViewHolder viewHolder = (GalleryViewHolder) holder;
        GalleryResultInfo.GalleryInfo info = list.get(position);
        viewHolder.name.setText(info.getTitle());
        viewHolder.hits.setText(String.valueOf(info.getHits()));

        GlideUtils.loadIntoUseFitWidth(context, info.getThumb(), R.drawable.bg_error, viewHolder.image);
        //Glide.with(context).load(info.getThumb()).into(viewHolder.image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGalleryClickListener != null) {
                    onGalleryClickListener.onGalleryClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.hits)
        TextView hits;

        GalleryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnGalleryClickListener {
        void onGalleryClick(int position);
    }
}
