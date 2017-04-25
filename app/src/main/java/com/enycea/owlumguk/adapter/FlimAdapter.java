package com.enycea.owlumguk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.enycea.owlumguk.R;
import com.enycea.owlumguk.bean.FlimInfo;
import com.enycea.owlumguk.util.ScreenUtils;
import com.enycea.owlumguk.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/19 17:14
 */

public class FlimAdapter extends RecyclerView.Adapter {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlimViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_film_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        FlimInfo info = list.get(position);
        FlimViewHolder viewHolder = (FlimViewHolder) holder;
        viewHolder.title.setText(info.getTitle());
        viewHolder.updateNumber.setText("更新至" + info.getUpdate_number() + "部");
        int height = (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(20))/2f);
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class FlimViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.update_number)
        TextView updateNumber;

        FlimViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnClickFlimItemListener {
        void onClickFlimItem(int position);
    }
}
