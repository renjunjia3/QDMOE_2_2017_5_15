package com.eexikn.uxcebc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eexikn.uxcebc.R;
import com.eexikn.uxcebc.bean.RankInfo;
import com.eexikn.uxcebc.util.ScreenUtils;
import com.eexikn.uxcebc.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/20 12:51
 */

public class RankHeaderAdapter extends BaseAdapter {
    private Context context;
    private List<RankInfo> list;
    private LayoutInflater inflater;

    public RankHeaderAdapter(Context context, List<RankInfo> list) {
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
        RankHeaderViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_rank_header_item, null);
            viewHolder = new RankHeaderViewHolder(convertView);
            int height=(int)((ScreenUtils.instance(context).getScreenWidth()-ScreenUtils.instance(context).dip2px(100))*8f/5f/3f);
            ViewUtils.setViewHeightByViewGroup(viewHolder.image,height);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RankHeaderViewHolder) convertView.getTag();
        }
        RankInfo rankInfo = list.get(position);
        Glide.with(context).load(rankInfo.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        viewHolder.name.setText(rankInfo.getActor_name());
        viewHolder.number.setText(rankInfo.getVotes() + "票");
        if (position == 0) {
            viewHolder.huangguan.setImageResource(R.drawable.ic_huangguan_2);
            viewHolder.no.setImageResource(R.drawable.ic_no_2);
        } else if (position == 1) {
            viewHolder.huangguan.setImageResource(R.drawable.ic_huangguan_1);
            viewHolder.no.setImageResource(R.drawable.ic_no_1);
        } else {
            viewHolder.huangguan.setImageResource(R.drawable.ic_huangguan_3);
            viewHolder.no.setImageResource(R.drawable.ic_no_3);
        }
        return convertView;
    }

    static class RankHeaderViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.no)
        ImageView no;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.zan)
        ImageView zan;
        @BindView(R.id.video_layout)
        RelativeLayout videoLayout;
        @BindView(R.id.huangguan)
        ImageView huangguan;

        RankHeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
