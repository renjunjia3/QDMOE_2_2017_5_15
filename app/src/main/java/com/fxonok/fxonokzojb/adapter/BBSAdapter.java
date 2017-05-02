package com.fxonok.fxonokzojb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fxonok.fxonokzojb.R;
import com.fxonok.fxonokzojb.bean.BBSInfo;
import com.fxonok.fxonokzojb.util.ScreenUtils;
import com.fxonok.fxonokzojb.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:论坛首页
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/18 13:14
 */

public class BBSAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<BBSInfo> lists;
    private static ScreenUtils screenUtils;

    private BBSItemOnClickListener bbsItemOnClickListener;

    public BBSAdapter(Context context, List<BBSInfo> lists) {
        this.context = context;
        this.lists = lists;
        screenUtils = ScreenUtils.instance(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BBSViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_bbs_item, parent, false));
    }

    public void setBbsItemOnClickListener(BBSItemOnClickListener bbsItemOnClickListener) {
        this.bbsItemOnClickListener = bbsItemOnClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BBSViewHolder viewHolder = (BBSViewHolder) holder;
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
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    static class BBSViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.note_number)
        TextView noteNumber;
        @BindView(R.id.most_new_note_name)
        TextView mostNewNoteName;

        BBSViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ViewUtils.setViewHeightByViewGroup(image, (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(30)) / 2f * 2f / 3f));
        }
    }

    public interface BBSItemOnClickListener {
        void onBBsItemOnClick(int position);
    }
}
