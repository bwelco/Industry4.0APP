package com.bwelco.app;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.Bean.OrderBean;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import Utils.ConfigUtil;
import Utils.GsonUtil;
import Utils.MyHttpUtil;
import Utils.ToastUtil;
import comity.bawujw.citytest.View.CityPop;
import model.OrderModel;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView contact;
//    ImageView getLocation;
//    ImageView getAddress;
    EditText name;
    EditText number;
//    EditText address;
//    TextView city;
    TextView goodType;
    LocationClient mLocationClient = null;
    BDLocationListener myListener;
    ProgressDialog dialog;
    CityPop pop;
    EditText goodnum;
    TextView stopLocation;

    String mAddress = null;

    String cityStr;
    String addressStr;

    Button orderButton = null;
    EditText beizhu;

    double longitude, latitude;

    boolean isLocated = false;

    OrderBean bean;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        dialog = new ProgressDialog(this);
        dialog.show();

        MyHttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
                ConfigUtil.URL + "Gy4-new-2/AppGetOrderXiadan.jsp", new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        dialog.dismiss();
                        Log.i("admin", responseInfo.result);

                        Type type = new TypeToken<OrderBean>() {
                        }.getType();
                        bean = (OrderBean) GsonUtil.getInstance().fromJson(responseInfo.result, type);

                        initView();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        dialog.dismiss();
                        ToastUtil.toast("获取失败。请检查连接。ErrCode: " + s);
                    }
                });

    }

    public void initView() {
        contact = (ImageView) this.findViewById(R.id.contact);
        name = (EditText) this.findViewById(R.id.name);
        number = (EditText) this.findViewById(R.id.number);
//        getLocation = (ImageView) this.findViewById(R.id.getLocation);
//        getAddress = (ImageView) this.findViewById(R.id.getAddress);
//        city = (TextView) this.findViewById(R.id.city);
//        address = (EditText) this.findViewById(R.id.address);
        goodType = (TextView) this.findViewById(R.id.goodType);
        goodnum = (EditText) this.findViewById(R.id.goodnum);
        dialog = new ProgressDialog(this);
        stopLocation = (TextView) this.findViewById(R.id.stopLocation);
        beizhu = (EditText) this.findViewById(R.id.beizhu);

//        address.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                //Toast.makeText(OrderActivity.this, "afterTextChanged"+s.toString(), Toast.LENGTH_SHORT).show();
//                //s.toString();
//                addressStr = s.toString();
//            }
//        });

        //进入测试主界面
        orderButton = (Button) this.findViewById(R.id.order_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("")) {
                    name.setError("没有输入姓名！");
                    name.requestFocus();
                    return;
                }

                if (number.getText().toString().equals("")) {
                    number.setError("没有输入手机号码！");
                    number.requestFocus();
                    return;
                }

                if (goodType.getText().toString().equals("选择货物类型")) {
                    ToastUtil.toast("没有选择货物类型！");
                    return;
                }

                if (stopLocation.getText().toString().equals("选择收货地")) {
                    ToastUtil.toast("没有选择收货地！");
                    return;
                }

                if (Integer.parseInt(goodnum.getText().toString()) > 30) {
                    ToastUtil.toast("最多只可选择30个！");
                    return;
                }

//                if (city.getText().toString().equals("")) {
//                    ToastUtil.toast("没有选择城市！");
//                    return;
//                }
//                if (address.getText().toString().equals("")) {
//                    address.setError("没有输入详细地址！");
//                    address.requestFocus();
//                    return;
//                }

                OrderModel order = new OrderModel();
                order.setName(name.getText().toString());
                order.setPhone(number.getText().toString());
                //order.setAddress(city.getText().toString().replace("-", "") + address.getText().toString());
                order.setGoods_type(goodType.getText().toString().substring(6));
                order.setGoods_num(goodnum.getText().toString());
                Gson gson = new Gson();
                String jsonstr = gson.toJson(order);
                Log.i("OrderActivity", jsonstr);


                Intent intent = new Intent();

                intent.setClass(OrderActivity.this, RoutePlanActivity.class);
                Bundle bundle = new Bundle();

                //城市和地址
                bundle.putBoolean("location", isLocated);//是经纬度还是城市名

                cityStr = stopLocation.getText().toString().substring(4);
                bundle.putString("city", cityStr);
                bundle.putString("address", addressStr);

                bundle.putDouble("longitude", longitude);
                bundle.putDouble("latitude", latitude);

                intent.putExtras(bundle);

                //拥有返回值
                startActivityForResult(intent, 1);
            }
        });

        contact.setOnClickListener(this);
//        getLocation.setOnClickListener(this);
//        getAddress.setOnClickListener(this);
//        city.setOnClickListener(this);
        goodType.setOnClickListener(this);
        stopLocation.setOnClickListener(this);

        name.setText(ConfigUtil.nickName);
        number.setText(ConfigUtil.phoneNum);

       // getAddress.setVisibility(View.GONE);

     //   mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
       // myListener = new MyLocationListener();
      //  initLocation();
      //  mLocationClient.registerLocationListener(myListener);    //注册监听函数
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("1", "result:" + resultCode);
        switch (requestCode) {

            case 0: {
                if (data == null || resultCode != -1) {
                    if (resultCode == -1) {
                        ToastUtil.toast("选取联系人失败");
                    }
                    return;  //没选择联系人
                }

                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                name.setText(contacts[0]);
                number.setText(contacts[1].replace("-", ""));

                break;
            }

            //从地图界面返回
            case 1: {
                //Toast.makeText(OrderActivity.this, "地图返回", Toast.LENGTH_SHORT).show();
                RequestParams params = new RequestParams();
                JSONObject object = new JSONObject();
                try {

                    String selectProductID = null;
                    for (OrderBean.ProductClassBean b : bean.getProductClass()) {
                        Log.i("admin", goodType.getText().toString().substring("货物类型：".length()));
                        if (b.getProductClass().equals(goodType.getText().toString().substring("货物类型：".length()))) {
                            selectProductID = b.getID();
                            break;
                        }
                    }


                    String selectLocationID = null;
                    for (OrderBean.LocationBean b : bean.getLocation()) {
                        Log.i("admin", stopLocation.getText().toString().substring("收货地：".length()));
                        if (b.getLocation().equals(stopLocation.getText().toString().substring("收货地：".length()))) {
                            selectLocationID = b.getID();
                            break;
                        }
                    }

                    object.put("UserID", ConfigUtil.userID);
                    object.put("Description", beizhu.getText().toString() + "");
                    object.put("ProductNumber", goodnum.getText().toString());
                    object.put("ProductClass", selectProductID);
                    object.put("Location", selectLocationID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.setMessage("正在创建订单...");
                dialog.show();
                params.addBodyParameter("param", object.toString());
                MyHttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
                        ConfigUtil.URL + "Gy4-new-2/AppAddOrder.jsp", params, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.i("admin", responseInfo.result);
                                /* 睡眠一会。等插入好之后再去调用。 */
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ToastUtil.toast("下单成功！");
                                dialog.dismiss();

                                Intent intent = new Intent(OrderActivity.this, OrderListActivity.class);
                                startActivity(intent);
                                OrderActivity.this.finish();
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Log.i("admin","fail = " + s);
                                dialog.dismiss();
                                ToastUtil.toast("下单失败！请检查网络设置 errCode:" + s);
                            }
                        });

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.contact: {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            }

//            case R.id.getLocation: {
//                mLocationClient.start();
//                break;
//            }
//
//            case R.id.getAddress: {
//                if (mAddress != null) {
//                    this.address.setText(mAddress);
//                } else {
//                }
//                break;
//            }
//
//            case R.id.city: {
//                pop = new CityPop(this, this).builder();
//                pop.show();
//                break;
//            }

            case R.id.goodType: {
                List<OrderBean.ProductClassBean> produceType = bean.getProductClass();
                CharSequence[] charSequences = new CharSequence[produceType.size()];

                for (int i = 0; i < produceType.size(); i++) {
                    charSequences[i] = produceType.get(i).getProductClass();
                }

                showDialog(charSequences, "货物类型：");
                break;
            }

            case R.id.stopLocation:{
                List<OrderBean.LocationBean> locationBeen = bean.getLocation();
                CharSequence[] charSequences = new CharSequence[locationBeen.size()];

                for (int i = 0; i < locationBeen.size(); i++) {
                    charSequences[i] = locationBeen.get(i).getLocation();
                }

                showDialog(charSequences, "收货地：");
                break;
            }
        }
    }


    private void showDialog(final CharSequence[] charSequences, final String prefix) {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);

        builder.setItems(charSequences, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (prefix.equals("货物类型："))
                    OrderActivity.this.goodType.setText(prefix + charSequences[which]);
                if (prefix.equals("收货地："))
                    OrderActivity.this.stopLocation.setText(prefix + charSequences[which]);
            }
        }).show();
    }


    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);

            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

//
//    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        option.setIsNeedAddress(true);
//        option.setIsNeedLocationDescribe(true);
//        mLocationClient.setLocOption(option);
//    }


//    // POP选择完毕
//    @Override
//    public void OnSelectDown(String province, String city) {
//        this.city.setText(province + "-" + city);
//        this.cityStr = city;
//        isLocated = false;
//        pop.dismiss();
//    }


//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//
//            if (location.getAddrStr() != null) {
//                city.setText(location.getProvince() + "-" + location.getCity());
//
//                cityStr = location.getCity(); //获得city
//                mAddress = location.getDistrict() + location.getStreet();
//                location.getAddress();
//
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
//
//                isLocated = true;
//
//                getAddress.setVisibility(View.VISIBLE);
//            } else {
//                ToastUtil.toast("获取定位失败，请检查。");
//            }
//
//
//            mLocationClient.stop();
//        }
//
//    }

//    public void startRoutePlanDriving(LatLng ptEnd) {
//        double mLat2 = 40.056858;
//        double mLon2 = 116.308194;
//
//        LatLng ptStart = new LatLng(31.9335920000,118.8929270000);
//
//        // 构建 route搜索参数
//        RouteParaOption para = new RouteParaOption()
//                .startPoint(ptStart)
////            .startName("天安门")
////            .endPoint(ptEnd);
//                .endName("金陵御花园3号楼608室")
//                .cityName("连云港");
//
////        RouteParaOption para = new RouteParaOption()
////                .startName("天安门").endName("百度大厦");
//
////        RouteParaOption para = new RouteParaOption()
////        .startPoint(pt_start).endPoint(pt_end);
//
//
//        try {
//            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
//        public void onGetGeoCodeResult(GeoCodeResult result) {
//            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                //没有检索到结果
//            }
//            //获取地理编码结果
//            LatLng latLng = result.getLocation();
//            startRoutePlanDriving(latLng);
//
//        }
//
//        @Override
//        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                //没有找到检索结果
//            }
//            //获取反向地理编码结果
////            Log.i("1", "bbb");
//        }
//    };
//

}
