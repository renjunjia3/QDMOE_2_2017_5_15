package com.cl.cltv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.cltv.R;
import com.cl.cltv.bean.OrderInfo;
import com.cl.cltv.util.DecimalUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:订单
 * package:com.mzhguqvn.mzhguq.adapter
 * Author：scene on 2017/5/10 17:34
 */

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderInfo> list;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, List<OrderInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        OrderViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_mine_order_item, parent, false);
            viewHolder = new OrderViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderViewHolder) convertView.getTag();
        }
        OrderInfo info = list.get(position);
        if (info != null) {
            Glide.with(context).load(info.getGoods_thumb()).centerCrop().into(viewHolder.image);
            viewHolder.orderId.setText(info.getOrder_id());
            viewHolder.goodsName.setText(info.getGood_name());
            viewHolder.price.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(info.getPrice()));
            viewHolder.totalPrice.setText("￥" + DecimalUtils.formatPrice2BlankToBlank(info.getMoney()));
            viewHolder.number.setText(info.getNumber() + "件");
        }

        return convertView;
    }

    static class OrderViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.order_id)
        TextView orderId;
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.total_price)
        TextView totalPrice;

        OrderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
