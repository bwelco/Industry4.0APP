package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Bean.OrderListBean;
import com.bwelco.app.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by bwelco on 2016/8/15.
 */
public class OrderListAdapter extends ArrayAdapter<OrderListBean.OrderInfoBean> {

    List<OrderListBean.OrderInfoBean> list;

    public OrderListAdapter(Context context, int resource, List<OrderListBean.OrderInfoBean> objects) {
        super(context, resource, objects);
        list = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productclass.setText(list.get(position).getProductClass());
        holder.num.setText("数量：" + list.get(position).getProductNumber());
        holder.startLocation.setText("发货地：" + list.get(position).getLocation());
        holder.creatTime.setText("创建时间：" + list.get(position).getCreateTime());
        holder.orderState.setText(list.get(position).getOrderState());

        if (list.get(position).getDescription() != "") {
            holder.description.setText("备注：" + list.get(position).getDescription());
        } else {
            holder.description.setText("备注：无");
        }

        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.typeImage)
        ImageView typeImage;
        @InjectView(R.id.productclass)
        TextView productclass;
        @InjectView(R.id.creatTime)
        TextView creatTime;
        @InjectView(R.id.startLocation)
        TextView startLocation;
        @InjectView(R.id.num)
        TextView num;
        @InjectView(R.id.orderState)
        TextView orderState;
        @InjectView(R.id.description)
        TextView description;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
