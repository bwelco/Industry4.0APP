package com.bwelco.app;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by bwelco on 2016/7/1.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastMgr.builder.init(getApplicationContext());
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }

    public enum ToastMgr {
        builder;
        private View view;
        private TextView tv;
        private Toast toast;

        /**
         * 初始化Toast
         *
         * @param context
         */
        public void init(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            tv = (TextView) view.findViewById(R.id.toast_textview);
            toast = new Toast(context);
            toast.setView(view);
        }

        /**
         * 显示Toast
         * @param content
         * @param duration Toast持续时间
         */
        public void display(CharSequence content, int duration) {
            if (content.length() != 0) {
                tv.setText(content);
                toast.setDuration(duration);
                toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
    }
}
