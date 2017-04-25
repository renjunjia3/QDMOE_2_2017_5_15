/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.enycea.owlumguk.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.enycea.owlumguk.R;
import com.enycea.owlumguk.bean.VideoInfo;

import java.util.List;

/**
 * Created by mikeafc on 15/11/26.
 */
public class BlackGlodAdapter extends PagerAdapter {

    private Context context;
    private List<VideoInfo> list;

    private OnItemClickBlackGlodListener onItemClickBlackGlodListener;

    public BlackGlodAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickBlackGlodListener(OnItemClickBlackGlodListener onItemClickBlackGlodListener) {
        this.onItemClickBlackGlodListener = onItemClickBlackGlodListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        RelativeLayout linearLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_black_glod_vip_item, null);
        ImageView image = (ImageView) linearLayout.findViewById(R.id.image);
        linearLayout.setId(R.id.item_id);
        Glide.with(container.getContext()).load(list.get(position).getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(image);
        container.addView(linearLayout);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickBlackGlodListener != null) {
                    onItemClickBlackGlodListener.onItemClickBlackGlod(position);
                }
            }
        });
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        RelativeLayout view = (RelativeLayout) object;
        container.removeView(view);
    }

    public interface OnItemClickBlackGlodListener {
        void onItemClickBlackGlod(int position);
    }
}
