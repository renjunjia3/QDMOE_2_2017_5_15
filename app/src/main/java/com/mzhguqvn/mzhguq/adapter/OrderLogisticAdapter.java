package com.mzhguqvn.mzhguq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.LogisticInfo;
import com.mzhguqvn.mzhguq.ui.view.timelineview.TimelineView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By: 快递信息
 * package:
 * Author：scene on 2017/5/11 14:26
 */
public class OrderLogisticAdapter extends RecyclerView.Adapter {

    private List<LogisticInfo.TracesBean> mFeedList;
    private Context mContext;

    public OrderLogisticAdapter(List<LogisticInfo.TracesBean> feedList) {
        mFeedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        View view;
        view = mLayoutInflater.inflate(R.layout.fragment_order_detail_item, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TimeLineViewHolder viewHolder = (TimeLineViewHolder) holder;

        LogisticInfo.TracesBean timeLineModel = mFeedList.get(position);
        viewHolder.time.setText(timeLineModel.getAcceptTime());
        viewHolder.content.setText(timeLineModel.getAcceptStation());
        if (position != 0) {
            viewHolder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.marker), Color.parseColor("#F0D09D"));
            viewHolder.content.setTextColor(Color.parseColor("#F0D09D"));
            viewHolder.time.setTextColor(Color.parseColor("#F0D09D"));
        } else {
            viewHolder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.marker), Color.parseColor("#F25077"));
            viewHolder.content.setTextColor(Color.parseColor("#F25077"));
            viewHolder.time.setTextColor(Color.parseColor("#F25077"));
        }
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    static class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.time_marker)
        TimelineView mTimelineView;

        TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mTimelineView.initLine(viewType);
        }
    }
}
