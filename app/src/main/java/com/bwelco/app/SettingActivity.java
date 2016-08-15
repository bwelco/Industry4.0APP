package com.bwelco.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Utils.ToastUtil;

public class SettingActivity extends AppCompatActivity
implements View.OnClickListener{

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        editText = (EditText) this.findViewById(R.id.edittext);
        button = (Button) this.findViewById(R.id.setURL);

        button.setOnClickListener(this);

        SharedPreferences sp =
                getSharedPreferences("ipset", Activity.MODE_PRIVATE);
        String url = sp.getString("url", null);

        editText.setText(url);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setURL) {
            SharedPreferences sp =
                    getSharedPreferences("ipset", Activity.MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();

            editor.putString("url", editText.getText().toString());

            editor.commit();

            ToastUtil.toast("保存成功。");

        }
    }
}
