package com.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.Bean.OrderListBean;
import com.adapter.OrderListAdapter;
import com.bwelco.app.OrderListActivity;
import com.bwelco.app.QueryActivity;
import com.bwelco.app.R;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import Utils.ConfigUtil;
import Utils.GsonUtil;
import Utils.MyHttpUtil;
import Utils.ToastUtil;

/**
 * Created by bwelco on 2016/8/15.
 */
public class DealingListFragment extends Fragment implements AdapterView.OnItemClickListener {


    private static SwipeRefreshLayout refresh;
    private static OrderListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_order_list, null);

        ListView listView = (ListView) view.findViewById(R.id.orderListView);
        TextView textView = (TextView) view.findViewById(R.id.noItem);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);

            /* 取消右侧滑动条 */

        listView.setVerticalScrollBarEnabled(false);
        listView.setEmptyView(textView);
        listView.setOnItemClickListener(this);

        adapter = new OrderListAdapter(getContext(),
                R.layout.item_order_list, OrderListActivity.dealingList);

        listView.setAdapter(adapter);
        textView.setText("无生产中订单");

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        return view;
    }



    public void refreshList() {
        RequestParams params = new RequestParams();
        JSONObject object = new JSONObject();

        try {
            object.put("UserID", ConfigUtil.userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.addBodyParameter("userid", object.toString());
        MyHttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
                ConfigUtil.URL + "Gy4-new-2/AppGetAllOrderInfo.jsp", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        if (refresh.isRefreshing()) {
                            refresh.setRefreshing(false);
                        }

                        Log.i("admin", responseInfo.result);

                        Type type = new TypeToken<OrderListBean>() {
                        }.getType();

                        OrderListActivity.orderList = (OrderListBean) GsonUtil.getInstance().fromJson(responseInfo.result, type);

                        OrderListActivity.dealingList.clear();
                        OrderListActivity.overList.clear();

                        for (OrderListBean.OrderInfoBean bean : OrderListActivity.orderList.getOrderInfo()) {
                            if (bean.getOrderStateCode().equals("-1") || bean.getOrderStateCode().equals("0")) {
                                /* 还没开始生产 */
                                OrderListActivity.dealingList.add(bean);

                            } else if (bean.getOrderStateCode().equals("1")) {
                                /* 生产结束 */
                                OrderListActivity.overList.add(bean);
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.i("admin", "failcode = " + s);
                        if (refresh.isRefreshing()) {
                            refresh.setRefreshing(false);
                        }
                        ToastUtil.toast("请求失败。请检查网络设置或者重新设置IP。");
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String orderID = OrderListActivity.dealingList.get(position).getOrderID();

        Intent intent = new Intent(getActivity(), QueryActivity.class);
        intent.putExtra("order", OrderListActivity.orderList);
        intent.putExtra("id", OrderListActivity.dealingList.get(position).getOrderID());


        startActivity(intent);

    }

}
