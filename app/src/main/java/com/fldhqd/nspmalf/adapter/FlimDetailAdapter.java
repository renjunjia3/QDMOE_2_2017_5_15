package com.fldhqd.nspmalf.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fldhqd.nspmalf.R;
import com.fldhqd.nspmalf.bean.VideoInfo;
import com.fldhqd.nspmalf.util.ScreenUtils;
import com.fldhqd.nspmalf.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/19 17:44
 */

public class FlimDetailAdapter extends BaseAdapter {
    private Context context;
    private List<VideoInfo> list;

    private OnClickItemVideoListener onClickItemVideoListener;

    public FlimDetailAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClickItemVideoListener(OnClickItemVideoListener onClickItemVideoListener) {
        this.onClickItemVideoListener = onClickItemVideoListener;
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
        FlimDetailViewHolder viewHolder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.fragment_glod_vip_item_item, parent, false);
            viewHolder=new FlimDetailViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (FlimDetailViewHolder) convertView.getTag();
        }
        VideoInfo info = list.get(position);
        viewHolder.playCount.setText(info.getHits() + "次");
        viewHolder.title.setText(info.getTitle());
        viewHolder.tag.setVisibility(View.GONE);
        viewHolder.title.setTextColor(Color.parseColor("#FFFFFF"));
        viewHolder.updateNumber.setText("更新至："+info.getUpdate_number()+"期");
        ScreenUtils screenUtils = ScreenUtils.instance(context);
        int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(9)) * 9f / 16f / 2f);
        ViewUtils.setViewHeightByViewGroup(viewHolder.image, height);
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemVideoListener != null) {
                    onClickItemVideoListener.onClickItemVideo(position);
                }
            }
        });

        return convertView;
    }

    static class FlimDetailViewHolder  {
        @BindView(R.id.item_view)
        RelativeLayout itemView;
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

        FlimDetailViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickItemVideoListener {
        void onClickItemVideo(int positon);
    }
}
