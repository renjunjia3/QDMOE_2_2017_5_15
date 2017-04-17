package com.hfaufhreu.hjfeuio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.base.SearchInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.cyldf.cyldfxv.adapter
 * Author：scene on 2017/4/14 17:10
 */

public class SearchAdapter extends BaseAdapter {
    private List<SearchInfo> list;
    private Context context;

    public SearchAdapter(Context context, List<SearchInfo> list) {
        this.context = context;
        this.list = list;
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
        MyViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_search_item, parent, false);
            viewHolder = new MyViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        SearchInfo info = list.get(position);
        viewHolder.name.setText(info.getTitle());
        viewHolder.size.setText(info.getSize());
        viewHolder.videoName.setText(info.getFile());
        return convertView;
    }

    static class MyViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.size)
        TextView size;
        @BindView(R.id.download)
        TextView download;
        @BindView(R.id.watch)
        TextView watch;
        @BindView(R.id.video_name)
        TextView videoName;

        MyViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
