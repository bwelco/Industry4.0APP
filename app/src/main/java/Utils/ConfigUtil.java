package Utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.bwelco.app.MyApp;

/**
 * Created by bwelco on 2016/8/14.
 */
public class ConfigUtil {

    public static String URL = "http://10.0.46.222:8080/";

    public static String phoneNum = "18115162181";

    public static String userID = "3";

    public static String nickName = "-1";

    public static String getURL() {
        String ret = null;
        SharedPreferences sp =
                MyApp.context.getSharedPreferences("ipset", Activity.MODE_PRIVATE);



         ret  = "http://" + sp.getString("url", null) + ":8080/";

        return ret;
    }
}
