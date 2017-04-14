package com.dhuadhuei.hdeuji.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhuadhuei.hdeuji.R;
import com.dhuadhuei.hdeuji.base.BaseFragment;
import com.dhuadhuei.hdeuji.listener.RecyclerViewOnItemClickListener;
import com.dhuadhuei.hdeuji.util.ScreenUtils;
import com.dhuadhuei.hdeuji.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 最新模块除了热门推荐的类型的列表适配器
 * Created by scene on 2017/3/13.
 */

public class IndexOtherTypeAdapter extends RecyclerView.Adapter {

    private BaseFragment baseFragment;
    private List<String> list;

    private RecyclerViewOnItemClickListener listener;

    private static ScreenUtils screenUtils;

    public IndexOtherTypeAdapter(BaseFragment baseFragment, List<String> list) {
        this.baseFragment = baseFragment;
        this.list = list;
        screenUtils = ScreenUtils.instance(baseFragment.getContext());
    }

    public void setRecyclerViewItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(
                baseFragment.getContext()).inflate(R.layout.fragment_index_other_type_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Holder mHolder = (Holder) holder;
        mHolder.name.setText(list.get(position));
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClickListener(position, list);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(40f)) * 210f / 3f / 150f);
            ViewUtils.setViewHeightByViewGroup(itemView, height);
        }
    }
}
