package com.bwelco.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.Bean.OrderListBean;
import com.fragments.DealingListFragment;
import com.fragments.OverListFragment;
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
    public static OrderListBean orderList;
    public static ArrayList<OrderListBean.OrderInfoBean> dealingList;
    public static ArrayList<OrderListBean.OrderInfoBean> overList;
    public DealingListFragment dealingListFragment;
    public OverListFragment overListFragment;


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

        dealingListFragment = new DealingListFragment();
        overListFragment = new OverListFragment();

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
            if (position == 0) {
                return dealingListFragment;
            } else if (position == 1) {
                return overListFragment;
            }

            return null;
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
