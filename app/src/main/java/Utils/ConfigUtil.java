package Utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.bwelco.app.MyApp;

/**
 * Created by bwelco on 2016/8/14.
 */
public class ConfigUtil {
    public static String URL =  "http://10.0.46.222:8080/";

    public static String userID = "1";

    public static String nickName = "null";

    public static String getURL(){
        String ret = null;
        SharedPreferences sp =
                MyApp.context.getSharedPreferences("ipset", Activity.MODE_PRIVATE);
        ret = sp.getString("url", null);

        return ret;
    }
}
