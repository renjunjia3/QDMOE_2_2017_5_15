package com.cyldf.cyldfxv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cyldf.cyldfxv.R;
import com.cyldf.cyldfxv.base.BaseFragment;
import com.cyldf.cyldfxv.base.BaseRecyclerAdapter;
import com.cyldf.cyldfxv.bean.Actor;
import com.cyldf.cyldfxv.util.ScreenUtils;
import com.cyldf.cyldfxv.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 女优 on 2017/3/14.
 */

public class ActorAdapter extends BaseRecyclerAdapter {
    private BaseFragment fragment;
    private List<Actor> list;
    private static ScreenUtils screenUtils;

    public ActorAdapter(BaseFragment fragment, List<Actor> list) {
        super(fragment);
        this.fragment = fragment;
        this.list = list;
        screenUtils = ScreenUtils.instance(fragment.getContext());
    }

    @Override
    protected List<?> setDataList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder1(LayoutInflater.from(fragment.getContext()).inflate(R.layout.fragment_actor_item, parent, false));
        } else {
            return new ViewHolder2(LayoutInflater.from(fragment.getContext()).inflate(R.layout.fragment_actor_item1, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (position % 2 == 0) {
            ViewHolder1 viewHolder = (ViewHolder1) holder;
            Actor actor = list.get(position);
            viewHolder.hits.setText(actor.getHits());
            viewHolder.name.setText(actor.getActor_name());
            Glide.with(fragment).load(actor.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        } else {
            ViewHolder2 viewHolder = (ViewHolder2) holder;
            Actor actor = list.get(position);
            viewHolder.hits.setText(actor.getHits());
            viewHolder.name.setText(actor.getActor_name());
            Glide.with(fragment).load(actor.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);
        }

    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.hits)
        TextView hits;

        ViewHolder1(View view) {
            super(view);
            ButterKnife.bind(this, view);
            int height = (int) (screenUtils.getScreenWidth() * 360f / 575f);
            ViewUtils.setViewHeightByViewGroup(view, height);
            ViewUtils.setViewHeightByRelativeLayout(image, height);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.hits)
        TextView hits;
        @BindView(R.id.item)
        RelativeLayout item;
        @BindView(R.id.see_all)
        TextView seeAll;

        ViewHolder2(View view) {
            super(view);
            ButterKnife.bind(this, view);
            int height = (int) (screenUtils.getScreenWidth() * 360f / 575f);
            ViewUtils.setViewHeightByViewGroup(view, height);
            ViewUtils.setViewHeightByRelativeLayout(image, height);
        }
    }

}
