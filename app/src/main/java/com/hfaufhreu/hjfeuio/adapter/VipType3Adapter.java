package com.hfaufhreu.hjfeuio.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.VideoDetailActivity;
import com.hfaufhreu.hjfeuio.bean.VideoInfo;
import com.hfaufhreu.hjfeuio.util.ScreenUtils;
import com.hfaufhreu.hjfeuio.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:
 * Author：scene on 2017/4/13 10:03
 */

public class VipType3Adapter extends BaseAdapter {

    private Context context;
    private List<VideoInfo> list;

    private ScreenUtils screenUtils;
    private LayoutInflater inflater;

    public VipType3Adapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;

        screenUtils = ScreenUtils.instance(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_index_other_type_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VideoInfo info = list.get(position);
        holder.name.setText(info.getTitle());
        if(info.getTag_color()!=null&&!info.getTag_color().isEmpty()){
            holder.tag.setBackgroundColor(Color.parseColor(info.getTag_color()));
        }
        int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(30f)) * 9f / 2f / 16f);
        ViewUtils.setViewHeightByViewGroup(holder.image, height);
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(holder.image);
        holder.playTime.setText(info.getHits() + "次播放");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, info);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_view)
        LinearLayout itemView;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.play_time)
        TextView playTime;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
