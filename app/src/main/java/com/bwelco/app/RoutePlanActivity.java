package com.bwelco.app;

import android.app.Activity;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRoutePlanOption.TransitPolicy;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 路线规划
 *
 * @author ys
 */

/**
 * 此demo用来展示如何进行驾车、步行、公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 *
 */
public class RoutePlanActivity extends Activity {

    private MapView mapView;
    private BaiduMap bdMap;

//        private EditText startEt;
//        private EditText endEt;

//        private String startPlace;// 开始地点
//        private String endPlace;// 结束地点
//
//        private Button driveBtn;// 驾车
//        private Button walkBtn;// 步行
//        private Button transitBtn;// 换成 （公交）
//        private Button nextLineBtn;
//
//        private Spinner drivingSpinner, transitSpinner;

    private Button checkButton;

    private RoutePlanSearch routePlanSearch;// 路径规划搜索接口

    private int index = -1;
    private int totalLine = 0;// 记录某种搜索出的方案数量
    private int drivintResultIndex = 0;// 驾车路线方案index
    private int transitResultIndex = 0;// 换乘路线方案index

    private String city; //城市
    private String address;//详细地址

    private double longitude, latitude;

    boolean isLoacted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏
        setContentView(R.layout.activity_routeplan);
        Bundle bundle = this.getIntent().getExtras();

        isLoacted = bundle.getBoolean("location"); //是经纬度还是城市名

        city = bundle.getString("city");
        address = bundle.getString("address");

        //经纬度
        longitude = bundle.getDouble("longitude");
        latitude = bundle.getDouble("latitude");

        init();

        drivingSearch(0);
    }

    /**
     *
     */
    private void init() {
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.showZoomControls(false);
        bdMap = mapView.getMap();

        checkButton = (Button) findViewById(R.id.check_button);
        //确定按钮
        checkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();//结束该activity
            }
        });
//            startEt = (EditText) findViewById(R.id.start_et);
//            endEt = (EditText) findViewById(R.id.end_et);
//            driveBtn = (Button) findViewById(R.id.drive_btn);
//            transitBtn = (Button) findViewById(R.id.transit_btn);
//            walkBtn = (Button) findViewById(R.id.walk_btn);
//            nextLineBtn = (Button) findViewById(R.id.nextline_btn);
//            nextLineBtn.setEnabled(false);
//            driveBtn.setOnClickListener(this);
//            transitBtn.setOnClickListener(this);
//            walkBtn.setOnClickListener(this);
//            nextLineBtn.setOnClickListener(this);
//
//            drivingSpinner = (Spinner) findViewById(R.id.driving_spinner);
//            String[] drivingItems = getResources().getStringArray(
//                    R.array.driving_spinner);
//            ArrayAdapter<String> drivingAdapter = new ArrayAdapter<>(this,
//                    android.R.layout.simple_spinner_item, drivingItems);
//            drivingSpinner.setAdapter(drivingAdapter);
//            drivingSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                    if (index == 0) {
//                        drivintResultIndex = 0;
//                        drivingSearch(drivintResultIndex);
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//
//            transitSpinner = (Spinner) findViewById(R.id.transit_spinner);
//            String[] transitItems = getResources().getStringArray(
//                    R.array.transit_spinner);
//            ArrayAdapter<String> transitAdapter = new ArrayAdapter<>(this,
//                    android.R.layout.simple_spinner_item, transitItems);
//            transitSpinner.setAdapter(transitAdapter);
//            transitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                    if (index == 1) {
//                        transitResultIndex = 0;
//                        transitSearch(transitResultIndex);
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });

        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch
                .setOnGetRoutePlanResultListener(routePlanResultListener);
    }

    /**
     * 路线规划结果回调
     */
    OnGetRoutePlanResultListener routePlanResultListener = new OnGetRoutePlanResultListener() {

        /**
         * 步行路线结果回调
         */
        @Override
        public void onGetWalkingRouteResult(
                WalkingRouteResult walkingRouteResult) {
            bdMap.clear();
            if (walkingRouteResult == null
                    || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // TODO
                return;
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(
                        bdMap);
                walkingRouteOverlay.setData(walkingRouteResult.getRouteLines()
                        .get(drivintResultIndex));
                bdMap.setOnMarkerClickListener(walkingRouteOverlay);
                walkingRouteOverlay.addToMap();
                walkingRouteOverlay.zoomToSpan();
                totalLine = walkingRouteResult.getRouteLines().size();
//                Toast.makeText(RoutePlanActivity.this,
//                        "共查询出" + totalLine + "条符合条件的线路", 1000).show();
                if (totalLine > 1) {
//                        nextLineBtn.setEnabled(true);
                }
            }
        }

        /**
         * 换成路线结果回调
         */
        @Override
        public void onGetTransitRouteResult(
                TransitRouteResult transitRouteResult) {
            bdMap.clear();
            if (transitRouteResult == null
                    || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // drivingRouteResult.getSuggestAddrInfo()
                return;
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                TransitRouteOverlay transitRouteOverlay = new TransitRouteOverlay(
                        bdMap);
                transitRouteOverlay.setData(transitRouteResult.getRouteLines()
                        .get(drivintResultIndex));// 设置一条驾车路线方案
                bdMap.setOnMarkerClickListener(transitRouteOverlay);
                transitRouteOverlay.addToMap();
                transitRouteOverlay.zoomToSpan();
                totalLine = transitRouteResult.getRouteLines().size();
//                Toast.makeText(RoutePlanActivity.this,
//                        "共查询出" + totalLine + "条符合条件的线路", 1000).show();
                if (totalLine > 1) {
//                        nextLineBtn.setEnabled(true);
                }
                // 通过getTaxiInfo()可以得到很多关于打车的信息
                Toast.makeText(
                        RoutePlanActivity.this,
                        "该路线打车总路程"
                                + transitRouteResult.getTaxiInfo()
                                .getDistance(), 1000).show();
            }
        }

        /**
         * 驾车路线结果回调 查询的结果可能包括多条驾车路线方案
         */
        @Override
        public void onGetDrivingRouteResult(
                DrivingRouteResult drivingRouteResult) {
            bdMap.clear();
            if (drivingRouteResult == null
                    || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
//                Toast.makeText(RoutePlanActivity.this, "没有查找到该地点！",
//                        Toast.LENGTH_SHORT).show();

                drivingCitySearch();
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // drivingRouteResult.getSuggestAddrInfo()
                return;
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        bdMap);
                drivingRouteOverlay.setData(drivingRouteResult.getRouteLines()
                        .get(drivintResultIndex));// 设置一条驾车路线方案
                bdMap.setOnMarkerClickListener(drivingRouteOverlay);
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
                totalLine = drivingRouteResult.getRouteLines().size();
//                Toast.makeText(RoutePlanActivity.this,
//                        "共查询出" + totalLine + "条符合条件的线路", 1000).show();
                if (totalLine > 1) {
//                        nextLineBtn.setEnabled(true);
                }
                // 通过getTaxiInfo()可以得到很多关于打车的信息
//                    Toast.makeText(
//                            RoutePlanActivity.this,
//                            "该路线打车总路程"
//                                    + drivingRouteResult.getTaxiInfo()
//                                    .getDistance(), 1000).show();
            }
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    /**
     * 驾车线路查询
     */
    private void drivingSearch(int index) {
        DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();
//            drivingOption.policy(DrivingPolicy.values()[drivingSpinner
//                    .getSelectedItemPosition()]);// 设置驾车路线策略
        drivingOption.policy(DrivingPolicy.values()[1]);// 设置驾车路线策略
        drivingOption.from(PlanNode.withCityNameAndPlaceName("南京", "南京工程学院(江宁校区)-4号门"));// 设置起点
        //drivingOption.to(PlanNode.withCityNameAndPlaceName("南京", endPlace));// 设置终点
//        drivingOption.to(PlanNode.withCityNameAndPlaceName(city, address));
        //drivingOption.to(PlanNode.withCityNameAndPlaceName("南京", address));
        if(isLoacted)
        {
            drivingOption.to(PlanNode.withLocation(new LatLng(latitude, longitude))); //经纬度
        }
        else
        {
            //Toast.makeText(RoutePlanActivity.this, ""+city+" "+address, Toast.LENGTH_SHORT).show();
            drivingOption.to(PlanNode.withCityNameAndPlaceName(city, address));
        }
        routePlanSearch.drivingSearch(drivingOption);// 发起驾车路线规划
    }

    private void drivingCitySearch() {
        DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();

        drivingOption.policy(DrivingPolicy.values()[1]);// 设置驾车路线策略
        drivingOption.from(PlanNode.withCityNameAndPlaceName("南京", "南京工程学院(江宁校区)-4号门"));// 设置起点
        drivingOption.to(PlanNode.withCityNameAndPlaceName(city, "市政府"));

        routePlanSearch.drivingSearch(drivingOption);// 发起驾车路线规划
    }

//        /**
//         * 换乘路线查询
//         */
//        private void transitSearch(int index) {
//            TransitRoutePlanOption transitOption = new TransitRoutePlanOption();
//            transitOption.city("北京");// 设置换乘路线规划城市，起终点中的城市将会被忽略
//            transitOption.from(PlanNode.withCityNameAndPlaceName("北京", startPlace));
//
////            transitOption.to( PlanNode.withLocation(new LatLng(118.894324, 31.940233)));
//            transitOption.to(PlanNode.withCityNameAndPlaceName("南京", "新街口"));
//            transitOption.policy(TransitPolicy.values()[transitSpinner
//                    .getSelectedItemPosition()]);// 设置换乘策略
//            routePlanSearch.transitSearch(transitOption);
//        }

//        /**
//         * 步行路线查询
//         */
//        private void walkSearch() {
//            WalkingRoutePlanOption walkOption = new WalkingRoutePlanOption();
//            walkOption.from(PlanNode.withCityNameAndPlaceName("北京", startPlace));
//            walkOption.to(PlanNode.withCityNameAndPlaceName("北京", endPlace));
//            routePlanSearch.walkingSearch(walkOption);
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.drive_btn:// 驾车
//                    index = 0;
//                    drivintResultIndex = 0;
//                    startPlace = startEt.getText().toString();
//                    endPlace = endEt.getText().toString();
//                    driveBtn.setEnabled(false);
//                    transitBtn.setEnabled(true);
//                    walkBtn.setEnabled(true);
//                    nextLineBtn.setEnabled(false);
//                    drivingSearch(drivintResultIndex);
//                    break;
//                case R.id.transit_btn:// 换乘
//                    index = 1;
//                    transitResultIndex = 0;
//                    startPlace = startEt.getText().toString();
//                    endPlace = endEt.getText().toString();
//                    transitBtn.setEnabled(false);
//                    driveBtn.setEnabled(true);
//                    walkBtn.setEnabled(true);
//                    nextLineBtn.setEnabled(false);
//                    transitSearch(transitResultIndex);
//                    break;
//                case R.id.walk_btn:// 步行
//                    index = 2;
//                    startPlace = startEt.getText().toString();
//                    endPlace = endEt.getText().toString();
//                    walkBtn.setEnabled(false);
//                    driveBtn.setEnabled(true);
//                    transitBtn.setEnabled(true);
//                    nextLineBtn.setEnabled(false);
//                    walkSearch();
//                    break;
//                case R.id.nextline_btn:// 下一条
//                    switch (index) {
//                        case 0:
//                            drivingSearch(++drivintResultIndex);
//                            break;
//                        case 1:
//                            transitSearch(transitResultIndex);
//                            break;
//                        case 2:
//
//                            break;
//                    }
//                    break;
//            }
//        }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        routePlanSearch.destroy();// 释放检索实例
        mapView.onDestroy();
    }
}
