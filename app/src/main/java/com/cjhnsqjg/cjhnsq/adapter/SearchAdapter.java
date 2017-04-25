package com.cjhnsqjg.cjhnsq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cjhnsqjg.cjhnsq.R;
import com.cjhnsqjg.cjhnsq.bean.SearchInfo;

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
    private OnClickDownloadListener onClickDownloadListener;

    public SearchAdapter(Context context, List<SearchInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClickDownloadListener(OnClickDownloadListener onClickDownloadListener) {
        this.onClickDownloadListener = onClickDownloadListener;
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
        MyViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_search_item, parent, false);
            viewHolder = new MyViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        final SearchInfo info = list.get(position);
        viewHolder.name.setText(info.getTitle());
        viewHolder.size.setText(info.getSize());
        viewHolder.videoName.setText(info.getFiles());
        if (info.isShowPlay()) {
            viewHolder.download.setText("播放");
        } else {
            viewHolder.download.setText("立即下载");
        }
        viewHolder.watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickDownloadListener != null) {
                    onClickDownloadListener.onClickPlay();
                }
            }
        });
        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickDownloadListener != null) {
                    if (info.isShowPlay()) {
                        onClickDownloadListener.onClickPlay();
                    } else {
                        onClickDownloadListener.onClickDownLoad(position);
                    }
                }
            }
        });
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

    public interface OnClickDownloadListener {
        void onClickDownLoad(int position);

        void onClickPlay();
    }
}
