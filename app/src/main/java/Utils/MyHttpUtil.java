package Utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by bwelco on 2016/8/14.
 */

public class MyHttpUtil {
    private static MyHttpUtil mInstance;
    private HttpUtils utils;


    public static synchronized MyHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (MyHttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new MyHttpUtil();
                }
                return mInstance;
            }
        }

        return mInstance;
    }

    private MyHttpUtil() {
        if (utils == null) {
            utils = new HttpUtils(1000 * 10);
            utils.configRequestRetryCount(0);  //不再重复请求
        }
    }

    public void send(HttpRequest.HttpMethod method, String url, RequestCallBack<String> callBack) {

        this.send(method, url, null, callBack);

    }

    public void send(final HttpRequest.HttpMethod method, final String url, final RequestParams params, final RequestCallBack<String> callBack) {

        utils.configCurrentHttpCacheExpiry(2000 * 1);

        utils.send(method, url, params, callBack);

    }

}
