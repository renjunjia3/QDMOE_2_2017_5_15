package com.mzhguqvn.mzhguq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.GoodsCommentInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.mzhguqvn.mzhguq.adapter
 * Author：scene on 2017/5/10 12:01
 */

public class GoodsComment2Adapter extends BaseAdapter {
    private Context context;
    private List<GoodsCommentInfo> list;
    private LayoutInflater inflater;

    public GoodsComment2Adapter(Context context, List<GoodsCommentInfo> list) {
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
        GoodsCommentViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_goods_comment_item2, parent, false);
            viewHolder = new GoodsCommentViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsCommentViewHolder) convertView.getTag();
        }
        GoodsCommentInfo info = list.get(position);
        if (info != null) {
            viewHolder.content.setText(info.getContent());
            viewHolder.userName.setText(info.getName());
            viewHolder.time.setText(info.getCreated_at());
            viewHolder.goodNumber.setText(info.getGood() + "");
            viewHolder.hits.setText(info.getHits() + "次");
            Glide.with(context).load(info.getAvatar()).into(viewHolder.userAvater);
        }
        return convertView;
    }


    static class GoodsCommentViewHolder {
        @BindView(R.id.user_avater)
        ImageView userAvater;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.hits)
        TextView hits;
        @BindView(R.id.good_number)
        TextView goodNumber;

        GoodsCommentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
