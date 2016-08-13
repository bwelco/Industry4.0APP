package com.bwelco.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adapter.LogisticsAdapter;
import com.entity.LogisticsInfo;
import com.interfaces.ScrollViewListener;
import com.logisticsView.CustomNodeListView;
import com.logisticsView.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

public class QueryActivity extends AppCompatActivity implements ScrollViewListener {

    ObservableScrollView scrollView;
    View headView;
    int head_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        scrollView = (ObservableScrollView) this.findViewById(R.id.lv_data);

        CustomNodeListView listview = (CustomNodeListView) this
                .findViewById(R.id.listview);
        getSupportActionBar().setTitle("订单信息");

        headView = getLayoutInflater().inflate(R.layout.logistics_head_layout, null);
        headView.setFocusable(true);
        headView.setFocusableInTouchMode(true);
        headView.requestFocus();
        listview.addHeaderView(headView);

        scrollView.setScrollViewListener(this);

        List<LogisticsInfo> datas = new ArrayList<LogisticsInfo>();
        for (int i = 0; i < 20; i++) {
            LogisticsInfo data = new LogisticsInfo();
            data.setDateTime("2015-10-9");
            if (i == 0 || i == 3 || i == 8) {
                data.setInfo("快件已被 拍照 签收快件已被 拍照 签收快件已被 拍");
            } else if (i == -4) {
                data.setInfo("在XXXX公司XX县分部进行签收扫描，快件已被 拍照 签收在XXXX公司XX县分部进行签收扫描，快件已被 拍照 签收");

            } else {
                data.setInfo("在XXXX公司XX县分部进行签收扫描，快件已被 拍照 签收");
            }
            datas.add(data);
        }

        LogisticsAdapter adapter = new LogisticsAdapter(datas, this);
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
        } else if (y < head_height - 6){
            getSupportActionBar().setTitle("订单信息");
        }
    }
}
