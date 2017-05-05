package com.fldhqd.nspmalf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fldhqd.nspmalf.R;
import com.fldhqd.nspmalf.bean.BBSInfo;
import com.fldhqd.nspmalf.util.ScreenUtils;
import com.fldhqd.nspmalf.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:论坛首页
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/18 13:14
 */

public class BBSAdapter extends BaseAdapter {
    private Context context;
    private List<BBSInfo> lists;
    private ScreenUtils screenUtils;

    private BBSItemOnClickListener bbsItemOnClickListener;

    public BBSAdapter(Context context, List<BBSInfo> lists) {
        this.context = context;
        this.lists = lists;
        screenUtils = ScreenUtils.instance(context);
    }

    public void setBbsItemOnClickListener(BBSItemOnClickListener bbsItemOnClickListener) {
        this.bbsItemOnClickListener = bbsItemOnClickListener;
    }


    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BBSViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_bbs_item, parent, false);
            viewHolder = new BBSViewHolder(convertView);
            ViewUtils.setViewHeightByViewGroup(viewHolder.image, (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(30)) / 2f * 2f / 3f));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BBSViewHolder) convertView.getTag();
        }
        BBSInfo info = lists.get(position);
        viewHolder.mostNewNoteName.setText(info.getLatest());
        viewHolder.noteNumber.setText(info.getTopic_number());
        viewHolder.title.setText(info.getDescription());
        Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bbsItemOnClickListener != null)
                    bbsItemOnClickListener.onBBsItemOnClick(position);
            }
        });


        return convertView;
    }

    static class BBSViewHolder {
        @BindView(R.id.item_view)
        RelativeLayout itemView;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.note_number)
        TextView noteNumber;
        @BindView(R.id.most_new_note_name)
        TextView mostNewNoteName;

        BBSViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface BBSItemOnClickListener {
        void onBBsItemOnClick(int position);
    }
}
