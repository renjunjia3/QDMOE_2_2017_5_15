package com.hfaufhreu.hjfeuio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.bean.TrySeeContentInfo;
import com.hfaufhreu.hjfeuio.ui.view.CustomeGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/19 10:57
 */

public class GlodVipAdpter extends BaseAdapter {
    private Context context;
    private List<TrySeeContentInfo> lists;
    private LayoutInflater inflater;

    private OnGlodVipItemClickListener onGlodVipItemClickListener;

    public GlodVipAdpter(Context context, List<TrySeeContentInfo> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    public void setOnGlodVipItemClickListener(OnGlodVipItemClickListener onGlodVipItemClickListener) {
        this.onGlodVipItemClickListener = onGlodVipItemClickListener;
    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists == null ? null : lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_glod_vip_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TrySeeContentInfo info = lists.get(position);
        viewHolder.title.setText(info.getTitle());
        viewHolder.gridView.setVisibility(info.getData() != null ? View.VISIBLE : View.GONE);
        GlodVipItemAdapter adapter = new GlodVipItemAdapter(context, info.getData());
        viewHolder.gridView.setAdapter(adapter);
        viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int childPosition, long id) {
                if (onGlodVipItemClickListener != null) {
                    onGlodVipItemClickListener.onGlodVipItemClick(position, childPosition);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.gridView)
        CustomeGridView gridView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnGlodVipItemClickListener {
        void onGlodVipItemClick(int position, int childPosition);
    }
}
