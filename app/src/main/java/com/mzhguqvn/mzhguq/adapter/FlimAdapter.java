package com.mzhguqvn.mzhguq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.FlimInfo;
import com.mzhguqvn.mzhguq.util.ScreenUtils;
import com.mzhguqvn.mzhguq.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/19 17:14
 */

public class FlimAdapter extends BaseAdapter {
    private Context context;
    private List<FlimInfo> list;

    private OnClickFlimItemListener onClickFlimItemListener;

    public FlimAdapter(Context context, List<FlimInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClickFlimItemListener(OnClickFlimItemListener onClickFlimItemListener) {
        this.onClickFlimItemListener = onClickFlimItemListener;
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
        FlimViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_film_item, parent, false);
            viewHolder = new FlimViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FlimViewHolder) convertView.getTag();
        }

        FlimInfo info = list.get(position);
        viewHolder.title.setText(info.getTitle());
        viewHolder.updateNumber.setText("更新至" + info.getUpdate_number() + "部");
        int height = (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(20)) / 2f);
        ViewUtils.setViewHeightByViewGroup(viewHolder.image, height);
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickFlimItemListener != null) {
                    onClickFlimItemListener.onClickFlimItem(position);
                }
            }
        });

        return convertView;
    }

    static class FlimViewHolder {
        @BindView(R.id.item_view)
        RelativeLayout itemView;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.update_number)
        TextView updateNumber;

        FlimViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public interface OnClickFlimItemListener {
        void onClickFlimItem(int position);
    }
}
