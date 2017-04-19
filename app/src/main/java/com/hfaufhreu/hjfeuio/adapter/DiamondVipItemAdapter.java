package com.hfaufhreu.hjfeuio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Author：scene on 2017/4/19 11:06
 */

public class DiamondVipItemAdapter extends BaseAdapter {

    private Context context;
    private List<VideoInfo> list;
    private LayoutInflater inflater;

    public DiamondVipItemAdapter(Context context, List<VideoInfo> list) {
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
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_diamond_vip_item_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        VideoInfo info = list.get(position);
        viewHolder.title.setText(info.getTitle());
        viewHolder.playCount.setText(info.getHits() + "次");
        viewHolder.score.setText(info.getScore() + "分");
        if (info.getTag() == null || info.getTag().isEmpty()) {
            viewHolder.tag.setVisibility(View.GONE);
        } else {
            viewHolder.tag.setVisibility(View.VISIBLE);
            viewHolder.tag.setText(info.getTag());
        }
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        int height = (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(12)) * 24f / 3f / 18f);
        ViewUtils.setViewHeightByViewGroup(viewHolder.image, height);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.score)
        TextView score;
        @BindView(R.id.play_count)
        TextView playCount;
        @BindView(R.id.tag)
        TextView tag;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
