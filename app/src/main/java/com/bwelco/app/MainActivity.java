package com.bwelco.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.widget.LinearLayout;

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
        switch (v.getId()){
            case R.id.send:{
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.query:{
                Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                startActivity(intent);
                break;
            }

        }
    }
}
