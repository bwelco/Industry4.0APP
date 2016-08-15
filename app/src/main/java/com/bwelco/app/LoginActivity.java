package com.bwelco.app;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Utils.ConfigUtil;
import Utils.MyHttpUtil;
import Utils.ToastUtil;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                final Explode[] explode = {null};

                RequestParams params = new RequestParams();
                //params.setBodyEntity(b);
                params.addBodyParameter("request",
                        "{\"UserName\":\"18115162181\",\"Password\":\"181\"}");


                MyHttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
                        ConfigUtil.URL + "Gy4-new-2/AppCheckUser.jsp", params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.i("admin", responseInfo.result);

                                try {
                                    JSONObject object = new JSONObject(responseInfo.result);
                                    String userID = object.getString("UserID");
                                    String nickName = object.getString("Nickname");

                                    Log.i("admin", "userid = " + userID + "  nickname = " + nickName);
                                    if (userID.equals("-1") && nickName.equals("-1")) {
                                        ToastUtil.toast("登录失败！请检查用户名密码");
                                    } else {
                                        ToastUtil.toast("登录成功！");
                                        ConfigUtil.userID = userID;
                                        ConfigUtil.nickName = nickName;

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                            explode[0] = new Explode();
                                            explode[0].setDuration(500);

                                            getWindow().setExitTransition(explode[0]);
                                            getWindow().setEnterTransition(explode[0]);
                                        }

                                        ActivityOptionsCompat oc2 =
                                                ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                                        Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i2, oc2.toBundle());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                ToastUtil.toast("登录失败！请检查用户名密码" + " 错误码：" + s);
                            }
                        });
                break;
        }
    }
}
