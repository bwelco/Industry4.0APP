package comity.bawujw.citytest.WheelView.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * Author: wangjie  email:tiantian.china.2@gmail.com
 * Date: 14-2-26
 * Time: ����3:37
 */
public class ThreadPool {
    public static final String TAG = ThreadPool.class.getSimpleName();
    static ExecutorService threadPool;

    public static void initThreadPool(int max){ // ������Application�н�������
        if(max > 0){
            max = max < 3 ? 3 : max;
            threadPool = Executors.newFixedThreadPool(max);
        }else{
            threadPool = Executors.newCachedThreadPool();
        }

        Logger.d(TAG, "[ThreadPool]ThreadPool init success...max thread: " + max);

    }

    public static ExecutorService getInstances(){
        return threadPool;
    }

    public synchronized static<U, R> void go(Runtask<U, R> runtask){
        if(null == threadPool){
            Logger.e(TAG, "ThreadPoolû�б���ʼ��������Application�н��г�ʼ������...");
            return;
        }
//        runtask.onBefore();
        threadPool.execute(runtask);
    }

    public synchronized static void go(Runnable runnable){
        if(null == threadPool){
            Logger.e(TAG, "ThreadPoolû�б���ʼ��������Application�н��г�ʼ������...");
            return;
        }
        threadPool.execute(runnable);
    }



}