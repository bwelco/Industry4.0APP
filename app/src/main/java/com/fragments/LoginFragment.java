package com.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bwelco.app.R;
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

/**
 * Created by bwelco on 2016/8/16.
 */
public class LoginFragment extends DialogFragment {


    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_login, null);


        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.bt_go)
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_go:
                final Explode[] explode = {null};

                RequestParams params = new RequestParams();
                //params.setBodyEntity(b);

                JSONObject object = new JSONObject();
                try {
                    object.put("UserName", etUsername.getText().toString());
                    object.put("Password", etPassword.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                params.addBodyParameter("request", object.toString());
                Log.i("admin", ConfigUtil.getURL());

                MyHttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
                        ConfigUtil.URL + "gy4/AppCheckUser.jsp", params,
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
                                        saveUserInfo();
                                        ConfigUtil.userID = userID;
                                        ConfigUtil.nickName = nickName;

//                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                                            explode[0] = new Explode();
//                                            explode[0].setDuration(500);
//
//                                            getActivity().getWindow().setExitTransition(explode[0]);
//                                            getActivity().getWindow().setEnterTransition(explode[0]);
//                                        }
//
//                                        ActivityOptionsCompat oc2 =
//                                                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
//                                        Intent i2 = new Intent(getActivity(), MainActivity.class);
//                                        startActivity(i2, oc2.toBundle());
                                        LoginFragment.this.dismiss();
                                       // LoginActivity.this.finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                ToastUtil.toast("登录失败！请检查网络连接。\n" + "请检查网络设置或者重新设置IP。");
                            }
                        });
                break;
        }
    }

    public void saveUserInfo(){
        SharedPreferences sp = getActivity().getSharedPreferences("userinfo", Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("username", etUsername.getText().toString());
        editor.putString("password", etPassword.getText().toString());

        editor.commit();
    }

    public void setInfo(){
        SharedPreferences sp = getActivity().getSharedPreferences("userinfo",Activity.MODE_PRIVATE);

        etUsername.setText(sp.getString("username", null));
        etPassword.setText(sp.getString("password", null));

    }
}
