package com.bwelco.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.Bean.DealingFormBean;
import com.Bean.OrderListBean;
import com.Bean.WuLiuBean;
import com.adapter.LogisticsAdapter;
import com.entity.LogisticsInfo;
import com.google.gson.reflect.TypeToken;
import com.interfaces.ScrollViewListener;
import com.john.waveview.WaveView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.logisticsView.CustomNodeListView;
import com.logisticsView.ObservableScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Utils.ConfigUtil;
import Utils.GsonUtil;
import Utils.MyHttpUtil;
import Utils.ToastUtil;

public class QueryActivity extends AppCompatActivity implements ScrollViewListener {

    ObservableScrollView scrollView;
    View headView;
    int head_height;
    OrderListBean.OrderInfoBean bean;

    TextView progress;
    TextView makeState;
    ProgressDialog dialog;
    DealingFormBean dealingFormBean;
    WuLiuBean wuLiuBean;
    WaveView waveView;
    CustomNodeListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        OrderListBean list = (OrderListBean) getIntent().getExtras().getSerializable("order");
        String id = getIntent().getStringExtra("id");


        for (OrderListBean.OrderInfoBean b : list.getOrderInfo()) {
            if (b.getOrderID().equals(id)) {
                bean = b;
                break;
            }
        }

        headView = getLayoutInflater().inflate(R.layout.logistics_head_layout, null);
        headView.setFocusable(true);
        headView.setFocusableInTouchMode(true);
        headView.requestFocus();

        progress = (TextView) headView.findViewById(R.id.progress);
        makeState = (TextView) headView.findViewById(R.id.makeState);
        waveView = (WaveView) headView.findViewById(R.id.wave_view);

        scrollView = (ObservableScrollView) this.findViewById(R.id.lv_data);

        listview = (CustomNodeListView) this
                .findViewById(R.id.listview);
        getSupportActionBar().setTitle("订单信息");

        listview.addHeaderView(headView);


        scrollView.setScrollViewListener(this);

        dialog = new ProgressDialog(this);
        dialog.show();

        if (!bean.getOrderStateCode().equals("1")) {
            JSONObject object = new JSONObject();
            try {
                object.put("OrderID", bean.getOrderID());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestParams params = new RequestParams();
            params.addBodyParameter("params", object.toString());

            MyHttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
                    ConfigUtil.URL + "gy4/AppGetRunningOrderInfo.jsp", params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            dialog.dismiss();

                            Log.i("admin", responseInfo.result);
                            Type type = new TypeToken<DealingFormBean>() {
                            }.getType();

                            dealingFormBean = (DealingFormBean) GsonUtil.getInstance().fromJson(responseInfo.result, type);

                            initViewMaking();

                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            dialog.dismiss();
                            ToastUtil.toast("请求失败。请检查网络设置或者重新设置IP。");
                        }
                    });
        } else {
            JSONObject object = new JSONObject();
            try {
                object.put("OrderID", bean.getOrderID());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestParams params = new RequestParams();
            params.addBodyParameter("params", object.toString());

            MyHttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
                    ConfigUtil.URL + "gy4/AppGetFinishOrderInfo.jsp", params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            dialog.dismiss();

                            Log.i("admin", responseInfo.result);
                            Type type = new TypeToken<WuLiuBean>() {
                            }.getType();

                            wuLiuBean = (WuLiuBean) GsonUtil.getInstance().fromJson(responseInfo.result, type);
                            initWuLiuView();

                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            dialog.dismiss();
                            ToastUtil.toast("请求失败。请检查网络设置或者重新设置IP。");
                        }
                    });
        }


    }

    public void initViewMaking() {

        List<LogisticsInfo> list = new ArrayList<LogisticsInfo>();
        LogisticsInfo data = new LogisticsInfo();
        data.setDateTime(bean.getCreateTime());
        data.setInfo("订单已创建");
        list.add(data);
        progress.setText(dealingFormBean.getOrderProgress() + "");
        makeState.setText(dealingFormBean.getOrderState());
        waveView.setProgress(Integer.parseInt(dealingFormBean.getOrderProgress().replace("%", "")));

        LogisticsAdapter adapter = new LogisticsAdapter(list, this);
        listview.setAdapter(adapter);
    }

    public void initWuLiuView() {
        List<LogisticsInfo> list = new ArrayList<LogisticsInfo>();

        progress.setText("100%");
        makeState.setText("订单已完成");
        waveView.setProgress(100);

        if (wuLiuBean.getLogistics().get(0).getLocation() == null) {
            LogisticsInfo data = new LogisticsInfo();
            data.setDateTime(bean.getCreateTime());
            data.setInfo("订单已完成，暂无物流信息");
            list.add(data);
        } else {
            for (WuLiuBean.LogisticsBean b : wuLiuBean.getLogistics()) {
                LogisticsInfo data = new LogisticsInfo();
                data.setDateTime(b.getTime());
                data.setInfo(b.getLocation());

                list.add(data);
            }
        }

        LogisticsAdapter adapter = new LogisticsAdapter(list, this);
        listview.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        head_height = headView.getMeasuredHeight();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y > head_height - 5) {
            getSupportActionBar().setTitle("物流信息");
        } else if (y < head_height - 6) {
            getSupportActionBar().setTitle("订单信息");
        }
    }
}
