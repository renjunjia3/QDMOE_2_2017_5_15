package com.ofgvyiss.ofgvyi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ofgvyiss.ofgvyi.R;
import com.ofgvyiss.ofgvyi.bean.VideoInfo;
import com.ofgvyiss.ofgvyi.util.ScreenUtils;
import com.ofgvyiss.ofgvyi.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/20 09:03
 */

public class RankVideoListAdapter extends BaseAdapter {
    private Context context;
    private List<VideoInfo> list;
    private LayoutInflater inflater;

    public RankVideoListAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
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
        RankVideoListViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_rank_video_list_item, null);
            viewHolder = new RankVideoListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RankVideoListViewHolder) convertView.getTag();
        }
        VideoInfo info = list.get(position);
        viewHolder.title.setText(info.getTitle());
        viewHolder.playCount.setText(info.getHits() + "次");
        viewHolder.tag.setVisibility(View.GONE);
        viewHolder.updateNumber.setText("更新至"+info.getUpdate_number()+"期");
        int height = (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(3)) * 24f / 2f / 17f);
        ViewUtils.setViewHeightByViewGroup(viewHolder.image,height);
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        return convertView;
    }

    static class RankVideoListViewHolder {
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

        RankVideoListViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
