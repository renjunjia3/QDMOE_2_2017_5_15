package com.tdhtzpkmo.pxzol.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tdhtzpkmo.pxzol.R;
import com.tdhtzpkmo.pxzol.bean.VideoInfo;
import com.tdhtzpkmo.pxzol.util.ScreenUtils;
import com.tdhtzpkmo.pxzol.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/19 11:06
 */

public class GlodVipItemAdapter extends BaseAdapter {

    private Context context;
    private List<VideoInfo> list;
    private LayoutInflater inflater;

    public GlodVipItemAdapter(Context context, List<VideoInfo> list) {
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
            convertView = inflater.inflate(R.layout.fragment_glod_vip_item_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        VideoInfo info = list.get(position);
        viewHolder.title.setText(info.getTitle());
        viewHolder.playCount.setText(info.getHits() + "次");
        if (info.getTag() == null || info.getTag().isEmpty()) {
            viewHolder.tag.setVisibility(View.GONE);
        } else {
            viewHolder.tag.setVisibility(View.VISIBLE);
            viewHolder.tag.setText(info.getTag());
        }

        if (info.getTag_color() == null || info.getTag_color().isEmpty()) {
            viewHolder.tag.setBackgroundColor(Color.parseColor("#02adfd"));
        } else {
            viewHolder.tag.setBackgroundColor(Color.parseColor(info.getTag_color()));
        }

        ViewUtils.setViewHeightByViewGroup(viewHolder.image, (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(9)) * 9f / 2f / 16f));
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.update_number)
        TextView updateNumber;
        @BindView(R.id.play_count)
        TextView playCount;
        @BindView(R.id.tag)
        TextView tag;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
