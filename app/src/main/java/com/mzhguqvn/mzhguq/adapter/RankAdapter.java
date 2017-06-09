package com.mzhguqvn.mzhguq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.RankResultInfo;
import com.mzhguqvn.mzhguq.util.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Case By:排行榜
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/19 21:05
 */

public class RankAdapter extends BaseAdapter {
    private Context context;
    private List<RankResultInfo.DataBean> list;
    private LayoutInflater inflater;

    public RankAdapter(Context context, List<RankResultInfo.DataBean> list) {
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
        RankViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_rank_item, parent, false);
            viewHolder = new RankViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RankViewHolder) convertView.getTag();
        }
        RankResultInfo.DataBean dataBean = list.get(position);
        viewHolder.number.setText("NO." + (position + 4));
        viewHolder.name.setText(dataBean.getTitle());
        viewHolder.tag.setText(dataBean.getTag());
        viewHolder.tag.setVisibility(TextUtils.isEmpty(dataBean.getTag()) ? View.GONE : View.VISIBLE);
        viewHolder.votes.setText(String.valueOf(dataBean.getScore()));
        viewHolder.videos.setText(dataBean.getDescription());
        GlideUtils.loadImage(context, viewHolder.image, dataBean.getThumb());
        return convertView;
    }

    static class RankViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.votes)
        TextView votes;
        @BindView(R.id.videos)
        TextView videos;

        RankViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
