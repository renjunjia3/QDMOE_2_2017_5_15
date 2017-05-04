package com.ebcnke.knulg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ebcnke.knulg.R;
import com.ebcnke.knulg.bean.VideoInfo;
import com.ebcnke.knulg.util.ScreenUtils;
import com.ebcnke.knulg.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/24 13:31
 */

public class GlodVipAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<VideoInfo> list;
    private LayoutInflater inflater;
    private OnClickGlodVipItemListener onClickGlodVipItemListener;

    public GlodVipAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setOnClickGlodVipItemListener(OnClickGlodVipItemListener onClickGlodVipItemListener) {
        this.onClickGlodVipItemListener = onClickGlodVipItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new TitleViewHolder(inflater.inflate(R.layout.fragment_glod_vip_item, parent, false));
        } else {
            ItemViewHolder itemViewHolder = new ItemViewHolder(inflater.inflate(R.layout.fragment_glod_vip_item_item, parent, false));
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
            itemViewHolder.updateNumber.setText("更新至" + info.getUpdate_number() + "期");
            if (info.getTag() == null || info.getTag().isEmpty()) {
                itemViewHolder.tag.setVisibility(View.GONE);
            } else {
                itemViewHolder.tag.setVisibility(View.VISIBLE);
                itemViewHolder.tag.setText(info.getTag());
            }

            if (info.getTag_color() == null || info.getTag_color().isEmpty()) {
                itemViewHolder.tag.setBackgroundColor(Color.parseColor("#02adfd"));
            } else {
                itemViewHolder.tag.setBackgroundColor(Color.parseColor(info.getTag_color()));
            }

            ViewUtils.setViewHeightByViewGroup(itemViewHolder.image, (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(9)) * 9f / 2f / 16f));
            Glide.with(context).load(info.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(itemViewHolder.image);
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickGlodVipItemListener!=null){
                        onClickGlodVipItemListener.onClickGlodVipItem(position);
                    }
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
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.update_number)
        TextView updateNumber;
        @BindView(R.id.play_count)
        TextView playCount;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickGlodVipItemListener {
        void onClickGlodVipItem(int position);
    }

}
