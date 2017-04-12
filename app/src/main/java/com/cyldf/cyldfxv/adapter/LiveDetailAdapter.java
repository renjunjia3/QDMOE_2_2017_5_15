package com.cyldf.cyldfxv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cyldf.cyldfxv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.yuuymd.kkskoo.adapter
 * Author：scene on 2017/4/6 13:16
 */

public class LiveDetailAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas = new ArrayList<>();

    private Random random;

    public LiveDetailAdapter(Context context) {
        this.context = context;
        random = new Random();
        String[] list = context.getResources().getStringArray(R.array.live_array);
        for (int i = 0; i < list.length - 30; i++) {
            datas.add(list[random.nextInt(list.length - 1)]);
        }

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_live_detail_comment, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.content.setText(datas.get(position));
        viewHolder.name.setText("VIP" + random.nextInt(100000) + ":");
        viewHolder.level.setText(random.nextInt(70) + "");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.level)
        TextView level;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.content)
        TextView content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
