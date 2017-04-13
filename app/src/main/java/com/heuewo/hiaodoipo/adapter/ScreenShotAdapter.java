package com.heuewo.hiaodoipo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.heuewo.hiaodoipo.R;
import com.heuewo.hiaodoipo.util.ScreenUtils;
import com.heuewo.hiaodoipo.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ScreenShotAdapter extends BaseAdapter {
    private Activity activity;
    private List<String> list;
    private LayoutInflater inflater;
    private static ScreenUtils screenUtils;

    public ScreenShotAdapter(Activity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
        screenUtils = ScreenUtils.instance(activity);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_index_video_detail_screen_shot_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(activity).load(list.get(position)).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(viewHolder.image);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        ImageView image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            ViewUtils.setViewHeightByViewGroup(image, (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(30f)) / 2f));
        }
    }
}
