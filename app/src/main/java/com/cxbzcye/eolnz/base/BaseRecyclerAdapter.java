package com.cxbzcye.eolnz.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import java.util.List;

/**
 * Created by scene on 2017/3/14.
 */

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter {

    private BaseFragment fragment;
    private ItemClickListener clickListener;

    public BaseRecyclerAdapter(BaseFragment fragment) {
        this.fragment = fragment;
    }


    protected abstract List<?> setDataList();

    public void setItemClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return setDataList().size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int position);
    }

}
