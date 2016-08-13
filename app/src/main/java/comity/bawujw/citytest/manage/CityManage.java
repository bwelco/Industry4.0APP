package comity.bawujw.citytest.manage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import comity.bawujw.citytest.db.CityDatabase;
import comity.bawujw.citytest.model.CityModel;

/**
 * Created by Sunday on 15/8/5.
 */
public class CityManage {

    private SQLiteDatabase database;

    public CityManage(Context context){
        database = CityDatabase.openDatabase(context);

    }

    /**
     * 获取所有的省市名称
     */
    public List<String> getAllNames(){
        List<String> result = new ArrayList<>();

        Cursor cursor = database.query("region",new String[]{"name"},null,null,null,null,null);

        while (cursor.moveToNext()){
                result.add(cursor.getString(0));

        }

        return result;
    }

    /**
     * 获取所有省的名称
     */
    public List<String> getProvinceNames(){
        List<String> result = new ArrayList<>();

        Cursor cursor = database.query("region",new String[]{"name"},"parent_id = 1",null,null,null,null,null);

        while (cursor.moveToNext()){
            result.add(cursor.getString(0));

        }

        return result;
    }

    /**
     * 根据省市的ID获取省市的名称
     */
    public String getProvinceNameFromProvinceId(String provinceId){
        String result = "找不到桑";
        Cursor cursor = database.query("region",new String[]{"name"},"region_id = ?",new String[]{provinceId},null,null,null,null);
        while (cursor.moveToNext()){
            result = cursor.getString(0);

        }

        return result;
    }

    /**
     * 根据省市的名称获取省市的ID
     */
    public String getProvinceIdFromProvinceName(String provinceName){
        String result = "找不到桑";
        Cursor cursor = database.query("region",new String[]{"region_id"},"name = ?",new String[]{provinceName},null,null,null,null);
        while (cursor.moveToNext()){
            result = cursor.getString(0);

        }

        return result;
    }

    /**
     * 根据省的ID获取它包含的所有市的名称
     */
    public List<String> getCityNameFromProvinceId(String provinceId){
        List<String> result = new ArrayList<>();

        Cursor cursor = database.query("region",new String[]{"name"},"parent_id = ?",new String[]{provinceId},null,null,null,null);

        while (cursor.moveToNext()){
            result.add(cursor.getString(0));

        }

        return result;
    }

    /**
     * 根据省的ID获取它包含的所有市的模型
     */
    public List<CityModel> getCityModelFromProvinceId(String provinceId){
        List<CityModel> result = new ArrayList<>();

        Cursor cursor = database.query("region",null,"parent_id = ?",new String[]{provinceId},null,null,null,null);

        while (cursor.moveToNext()){
            CityModel cityModel = new CityModel();

            //城市ID
            int indexId = cursor.getColumnIndex("region_id");
            cityModel.setCityId(cursor.getString(indexId));

            //城市名
            int indexCityName = cursor.getColumnIndex("name");
            cityModel.setCityName(cursor.getString(indexCityName));

            //所属省ID
            int indexProvinceId = cursor.getColumnIndex("parent_id");
            cityModel.setProvinceId(cursor.getString(indexProvinceId));

            //所属省名称
            cityModel.setProvinceName(getProvinceNameFromProvinceId(cityModel.getProvinceId()));

            result.add(cityModel);

        }

        return result;
    }

    /**
     * 根据省市的ID获取省市的模型
     */
    public CityModel getCityModelFormCityId(String cityId){
        CityModel result = new CityModel();

        Cursor cursor = database.query("region",null,"region_id = ?",new String[]{cityId},null,null,null,null);

        while (cursor.moveToNext()){
            //城市ID
            int indexId = cursor.getColumnIndex("region_id");
            result.setCityId(cursor.getString(indexId));

            //城市名
            int indexCityName = cursor.getColumnIndex("name");
            result.setCityName(cursor.getString(indexCityName));

            //所属省ID
            int indexProvinceId = cursor.getColumnIndex("parent_id");
            result.setProvinceId(cursor.getString(indexProvinceId));

            //所属省名称
            result.setProvinceName(getProvinceNameFromProvinceId(result.getProvinceId()));
        }

        return result;
    }

    /**
     * 根据省市的名称获取省市的模型
     */
    public CityModel getCityModelFormCityName(String cityName){
        CityModel result = new CityModel();

        Cursor cursor = database.query("region",null,"name = ?",new String[]{cityName},null,null,null,null);

        while (cursor.moveToNext()){
            //城市ID
            int indexId = cursor.getColumnIndex("region_id");
            result.setCityId(cursor.getString(indexId));

            //城市名
            int indexCityName = cursor.getColumnIndex("name");
            result.setCityName(cursor.getString(indexCityName));

            //所属省ID
            int indexProvinceId = cursor.getColumnIndex("parent_id");
            result.setProvinceId(cursor.getString(indexProvinceId));

            //所属省名称
            result.setProvinceName(getProvinceNameFromProvinceId(result.getProvinceId()));
        }

        return result;
    }

}
