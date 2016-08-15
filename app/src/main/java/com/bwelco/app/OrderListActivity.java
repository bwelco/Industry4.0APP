package com.bwelco.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.Bean.OrderListBean;
import com.adapter.OrderListAdapter;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Utils.ConfigUtil;
import Utils.GsonUtil;
import Utils.MyHttpUtil;
import Utils.ToastUtil;

public class OrderListActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static OrderListBean orderList;
    private static ArrayList<OrderListBean.OrderInfoBean> dealingList;
    private static ArrayList<OrderListBean.OrderInfoBean> overList;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("订单查询");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("订单查询中");
        dialog.setCancelable(false);
        dialog.show();

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
                        dialog.dismiss();
                        Log.i("admin", responseInfo.result);

                        Type type = new TypeToken<OrderListBean>() {
                        }.getType();

                        orderList = (OrderListBean) GsonUtil.getInstance().fromJson(responseInfo.result, type);

                        dealingList = new ArrayList<OrderListBean.OrderInfoBean>();
                        overList = new ArrayList<OrderListBean.OrderInfoBean>();

                        for (OrderListBean.OrderInfoBean bean : orderList.getOrderInfo()) {
                            if (bean.getOrderStateCode().equals("-1") || bean.getOrderStateCode().equals("0")) {
                                /* 还没开始生产 */
                                dealingList.add(bean);

                            } else if (bean.getOrderStateCode().equals("1")) {
                                /* 生产结束 */
                                overList.add(bean);
                            }
                        }

                        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                        // Set up the ViewPager with the sections adapter.
                        mViewPager = (ViewPager) findViewById(R.id.container);
                        mViewPager.setAdapter(mSectionsPagerAdapter);

                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(mViewPager);

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.i("admin", "failcode = " + s);
                        dialog.dismiss();
                        ToastUtil.toast("请求失败。errCode：" + s);
                    }
                });

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static SwipeRefreshLayout refresh;
        private static OrderListAdapter adapter;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.orderListView);
            TextView textView = (TextView) rootView.findViewById(R.id.noItem);
            refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
            refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                    android.R.color.holo_orange_light, android.R.color.holo_green_light);

            /* 取消右侧滑动条 */

            listView.setVerticalScrollBarEnabled(false);
            listView.setEmptyView(textView);

            Log.i("admin", dealingList.size() + "  " + overList.size());
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                /* 生产中 */

                adapter = new OrderListAdapter(getContext(),
                        R.layout.item_order_list, dealingList);
                listView.setAdapter(adapter);
                textView.setText("无生产中订单");

            } else {
                adapter = new OrderListAdapter(getContext(),
                        R.layout.item_order_list, overList);
                listView.setAdapter(adapter);

                textView.setText("无历史订单");

            }

            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshList();
                    adapter.notifyDataSetChanged();
                }
            });


            return rootView;
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

                            orderList = (OrderListBean) GsonUtil.getInstance().fromJson(responseInfo.result, type);

                            dealingList = new ArrayList<OrderListBean.OrderInfoBean>();
                            overList = new ArrayList<OrderListBean.OrderInfoBean>();

                            for (OrderListBean.OrderInfoBean bean : orderList.getOrderInfo()) {
                                if (bean.getOrderStateCode().equals("-1") || bean.getOrderStateCode().equals("0")) {
                                /* 还没开始生产 */
                                    dealingList.add(bean);

                                } else if (bean.getOrderStateCode().equals("1")) {
                                /* 生产结束 */
                                    overList.add(bean);
                                }
                            }


                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Log.i("admin", "failcode = " + s);
                            if (refresh.isRefreshing()) {
                                refresh.setRefreshing(false);
                            }
                            ToastUtil.toast("请求失败。errCode：" + s);
                        }
                    });
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "处理中订单";
                case 1:
                    return "历史订单";
            }
            return null;
        }
    }
}
