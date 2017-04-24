package com.tdhtzpkmo.pxzol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tdhtzpkmo.pxzol.R;
import com.tdhtzpkmo.pxzol.bean.RankInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/19 21:05
 */

public class RankAdapter extends BaseAdapter {
    private Context context;
    private List<RankInfo> list;
    private LayoutInflater inflater;

    public RankAdapter(Context context, List<RankInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankViewHolder rankViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_rank_item, null);
            rankViewHolder = new RankViewHolder(convertView);
            convertView.setTag(rankViewHolder);
        } else {
            rankViewHolder = (RankViewHolder) convertView.getTag();
        }
        RankInfo info = list.get(position);

        rankViewHolder.name.setText(info.getActor_name());
        rankViewHolder.videos.setText(info.getVideos());
        rankViewHolder.votes.setText(info.getVotes() + "票");
        rankViewHolder.number.setText("NO" + (position + 4));
        rankViewHolder.progressBar.setProgress(info.getPercent());
        if (info.getTag() == null || info.getTag().isEmpty()) {
            rankViewHolder.tag.setVisibility(View.GONE);
        } else {
            rankViewHolder.tag.setVisibility(View.VISIBLE);
            rankViewHolder.tag.setText(info.getTag());
        }
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(rankViewHolder.image);
        return convertView;
    }

    static class RankViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.progressBar)
        MaterialProgressBar progressBar;
        @BindView(R.id.votes)
        TextView votes;
        @BindView(R.id.ding)
        TextView ding;
        @BindView(R.id.videos)
        TextView videos;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.image)
        ImageView image;

        RankViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
