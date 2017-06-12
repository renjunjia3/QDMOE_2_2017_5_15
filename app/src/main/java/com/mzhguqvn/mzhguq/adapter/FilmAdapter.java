package com.mzhguqvn.mzhguq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.RankResultInfo;
import com.mzhguqvn.mzhguq.util.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:片库
 * package:com.mzhguqvn.mzhguq.adapter
 * Author：scene on 2017/6/12 10:39
 */

public class FilmAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<RankResultInfo.DataBean> list;
    private OnClickFilmItemListener onClickFilmItemListener;

    public FilmAdapter(Context context, List<RankResultInfo.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClickFilmItemListener(OnClickFilmItemListener onClickFilmItemListener) {
        this.onClickFilmItemListener = onClickFilmItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilmViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_film_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        FilmViewHolder viewHolder = (FilmViewHolder) holder;
        RankResultInfo.DataBean info = list.get(position);
        viewHolder.title.setText(info.getTitle());
        viewHolder.hits.setText(info.getHits() + "次访问");
        GlideUtils.loadImage(context, viewHolder.image, info.getThumb());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickFilmItemListener != null) {
                    onClickFilmItemListener.onClickFilmItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.hits)
        TextView hits;

        FilmViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickFilmItemListener {
        void onClickFilmItem(int position);
    }
}
