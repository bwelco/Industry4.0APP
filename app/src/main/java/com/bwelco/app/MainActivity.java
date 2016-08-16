package com.bwelco.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fragments.LoginFragment;

import Utils.ConfigUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    LinearLayout send;
    LinearLayout query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Explode explode = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            explode = new Explode();
            explode.setDuration(500);
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }

        getSupportActionBar().setTitle("智能管理系统");
        send = (LinearLayout) this.findViewById(R.id.send);
        query = (LinearLayout) this.findViewById(R.id.query);


        send.setOnClickListener(this);
        query.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send: {
                if (ConfigUtil.nickName.equals("-1")) {

                    LoginFragment fragment = new LoginFragment();
                    fragment.show(getSupportFragmentManager(), null);

                } else {
                    Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                    startActivity(intent);
                }
                break;
            }

            case R.id.query: {
                if (ConfigUtil.nickName.equals("-1")) {

                    LoginFragment fragment = new LoginFragment();
                    fragment.show(getSupportFragmentManager(), null);
                } else {
                    Intent intent = new Intent(MainActivity.this, OrderListActivity.class);
                    startActivity(intent);
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.login:
                LoginFragment fragment = new LoginFragment();
                fragment.show(getSupportFragmentManager(), null);

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
