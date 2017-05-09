package com.mzhguqvn.mzhguq.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.CommentInfo;
import com.joooonho.SelectableRoundedImageView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/16.
 */

public class CommentAdapter extends BaseAdapter {
    private Activity activity;
    private List<CommentInfo> list;
    private LayoutInflater inflater;
    private Random random;

    public CommentAdapter(Activity activity, List<CommentInfo> list) {
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
        random = new Random();
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
            convertView = inflater.inflate(R.layout.fragment_video_detail_coment_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CommentInfo info = list.get(position);
        viewHolder.name.setText("VIP" + (random.nextInt(10000) + 10000));
        viewHolder.content.setText(info.getText());
        viewHolder.time.setText(info.getTime());
        Glide.with(activity).load(info.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.ic_user_avatar).error(R.drawable.ic_user_avatar).into(viewHolder.headImage);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.headImage)
        SelectableRoundedImageView headImage;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.time)
        TextView time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
