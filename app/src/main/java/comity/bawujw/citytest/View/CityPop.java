package comity.bawujw.citytest.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bwelco.app.R;
import com.interfaces.OnPopSelectListener;

import java.util.ArrayList;
import java.util.List;

import comity.bawujw.citytest.WheelView.WheelView;
import comity.bawujw.citytest.manage.CityManage;

/**
 * Created by Sunday on 15/8/7.
 */
public class CityPop {
    private Context context;
    private Display display;
    private OnPopSelectListener mListener;

    /**
     * 取消按钮
     */
    private Button btnCancel;

    /**
     * 确定按钮
     */
    private Button btnConfirm;

    /**
     *  屏幕的宽度，用于动态设定滑动块的宽度
     * */
    private int screenWidth;

    //省
    private WheelView wvProvince;
    //市
    private WheelView wvCity;
    //关闭按钮
    private ImageView ivDismiss;

    /**
     * 省数据列表
     */
    private List<String> provinceList;

    /**
     * 省对应的市的数据列表
     */
    private  List<String> cityList;

    /**
     * 管理器
     */
    private CityManage cityManage;

    private PopupWindow popupWindow;

    public CityPop(Context context, OnPopSelectListener listener){
        this.context = context;
        this.mListener = listener;

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();

        cityManage = new CityManage(context);
    }

    /**
     * 创建PHRightShareAndSaveView对象
     * @return PHRightShareAndSaveView实体
     */
    public CityPop builder(){

        // PopView
        View view = LayoutInflater.from(context).inflate(
                R.layout.pop_city, null);

        loadCity(view);
// android.view.View#getWindowToken()
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new PopDismissListener());
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
        popupWindow.setAnimationStyle(R.style.PopAnimationDownUp);
        return this;
    }

    public  void loadCity(View view){

        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityPop.this.mListener.OnSelectDown(wvProvince.getSeletedItem(), wvCity.getSeletedItem());
            }
        });


        wvProvince = (WheelView) view.findViewById(R.id.wheel_view1);
        wvProvince.getLayoutParams().width = screenWidth / 2;
        wvCity = (WheelView) view.findViewById(R.id.wheel_view2);
        wvCity.getLayoutParams().width = screenWidth / 2;
        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();

        //获取 北京 作为默认
        provinceList = cityManage.getCityNameFromProvinceId("1");
        cityList = cityManage.getCityNameFromProvinceId("2");

        wvProvince.setOffset(2);
        wvProvince.setItems(provinceList);


        wvCity.setOffset(2);
        wvCity.setItems(cityList);

        wvProvince.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);

                //重新初始化城市数据
                cityList = cityManage.getCityNameFromProvinceId(cityManage.getProvinceIdFromProvinceName(item));
                //替换显示数据
                wvCity.replace(cityList);
                wvCity.setSeletion(0);
            }
        });

        wvCity.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);

            }
        });
    }


    /**
     * 显示
     */
    public void show(){
        if (null != popupWindow){
            setBackgroundAlpha(0.5f);
            popupWindow.update();
        }
    }

    /**
     * 撤销
     */
    public void dismiss(){
        if (null != popupWindow){
            popupWindow.dismiss();
        }
    }

    /**
     * 设置背景透明度
     * @param alpha 背景的Alpha
     */
    private void setBackgroundAlpha(float alpha){
        WindowManager.LayoutParams lp =  ((Activity)context).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity)context).getWindow().setAttributes(lp);
    }

    private class PopDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            //更改背景透明度
            setBackgroundAlpha(1.0f);
        }
    }

}
