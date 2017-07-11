package com.cl.cltv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.cltv.R;
import com.cl.cltv.bean.VideoInfo;
import com.cl.cltv.util.ScreenUtils;
import com.cl.cltv.util.ViewUtils;
import com.cl.cltv.video.JCUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/24 13:31
 */

public class DiamondVipAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<VideoInfo> list;
    private LayoutInflater inflater;
    private OnClickDiamondVipItemListener onClickDiamondVipItemListener;

    public DiamondVipAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setOnClickDiamondVipItemListener(OnClickDiamondVipItemListener onClickDiamondVipItemListener) {
        this.onClickDiamondVipItemListener = onClickDiamondVipItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new TitleViewHolder(inflater.inflate(R.layout.fragment_diamond_vip_item, parent, false));
        } else {
            ItemViewHolder itemViewHolder = new ItemViewHolder(inflater.inflate(R.layout.fragment_diamond_vip_item_item, parent, false));
            int height = (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(12)) * 24f / 3f / 18f);
            ViewUtils.setViewHeightByViewGroup(itemViewHolder.image, height);
            return itemViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VideoInfo info = list.get(position);
        if (getItemViewType(position) == 0) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.title.setText(info.getTitle());
        } else {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.title.setText(info.getTitle());
            itemViewHolder.playCount.setText(info.getHits() + "次");
            itemViewHolder.score.setText(info.getScore() + "分");
            itemViewHolder.updateNumber.setText("第" + info.getUpdate_number() + "期");
            itemViewHolder.time.setText(JCUtils.stringForTime(info.getDuration()));
            Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(itemViewHolder.image);
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickDiamondVipItemListener != null)
                        onClickDiamondVipItemListener.onClickDiamondVipItem(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isTilteType() ? 0 : 1;
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.score)
        TextView score;
        @BindView(R.id.play_count)
        TextView playCount;
        @BindView(R.id.update_number)
        TextView updateNumber;
        @BindView(R.id.time)
        TextView time;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickDiamondVipItemListener {
        void onClickDiamondVipItem(int position);
    }
}
