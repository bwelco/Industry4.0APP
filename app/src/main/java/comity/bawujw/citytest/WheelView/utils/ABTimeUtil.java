package comity.bawujw.citytest.WheelView.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ����ʱ��ת������
 * @author wangjie
 * @version ����ʱ�䣺2013-2-19 ����11:35:53
 */
public class ABTimeUtil {
    public static final String TAG = ABTimeUtil.class.getSimpleName();

    /**
     * ��һ��������ת����ʱ���ַ�������ʽΪСʱ/��/��/���루�磺24903600 --> 06Сʱ55��03��600���룩��
     * @author wangjie
     * @param millis Ҫת���ĺ�������
     * @param isWhole �Ƿ�ǿ��ȫ����ʾСʱ/��/��/���롣
     * @param isFormat ʱ�������Ƿ�Ҫ��ʽ�������true����λ��ǰ�油ȫ�����false����λ��ǰ�治��ȫ��
     * @return ����ʱ���ַ�����Сʱ/��/��/����ĸ�ʽ���磺24903600 --> 06Сʱ55��03��600���룩��
     */
    public static String millisToString(long millis, boolean isWhole, boolean isFormat){
        String h = "";
        String m = "";
        String s = "";
        String mi = "";
        if(isWhole){
            h = isFormat ? "00Сʱ" : "0Сʱ";
            m = isFormat ? "00��" : "0��";
            s = isFormat ? "00��" : "0��";
            mi = isFormat ? "00����" : "0����";
        }

        long temp = millis;

        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;

        if(temp / hper > 0){
            if(isFormat){
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            }else{
                h = temp / hper + "";
            }
            h += "Сʱ";
        }
        temp = temp % hper;

        if(temp / mper > 0){
            if(isFormat){
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            }else{
                m = temp / mper + "";
            }
            m += "��";
        }
        temp = temp % mper;

        if(temp / sper > 0){
            if(isFormat){
                s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";
            }else{
                s = temp / sper + "";
            }
            s += "��";
        }
        temp = temp % sper;
        mi = temp + "";

        if(isFormat){
            if(temp < 100 && temp >= 10){
                mi = "0" + temp;
            }
            if(temp < 10){
                mi = "00" + temp;
            }
        }

        mi += "����";
        return h + m + s + mi;
    }
    /**
     * ��һ��������ת����ʱ���ַ�������ʽΪСʱ/��/��/���루�磺24903600 --> 06Сʱ55���ӣ���
     * @author wangjie
     * @param millis Ҫת���ĺ�������
     * @param isWhole �Ƿ�ǿ��ȫ����ʾСʱ/�֡�
     * @param isFormat ʱ�������Ƿ�Ҫ��ʽ�������true����λ��ǰ�油ȫ�����false����λ��ǰ�治��ȫ��
     * @return ����ʱ���ַ�����Сʱ/��/��/����ĸ�ʽ���磺24903600 --> 06Сʱ55���ӣ���
     */
    public static String millisToStringShort(long millis, boolean isWhole, boolean isFormat){
        String h = "";
        String m = "";
        if(isWhole){
            h = isFormat ? "00Сʱ" : "0Сʱ";
            m = isFormat ? "00����" : "0����";
        }

        long temp = millis;

        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;

        if(temp / hper > 0){
            if(isFormat){
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            }else{
                h = temp / hper + "";
            }
            h += "Сʱ";
        }
        temp = temp % hper;

        if(temp / mper > 0){
            if(isFormat){
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            }else{
                m = temp / mper + "";
            }
            m += "����";
        }

        return h + m;
    }

    /**
     * �����ں���ת��Ϊ�ַ�����Ĭ�ϸ�ʽ��yyyy-MM-dd HH:mm:ss��
     * @author wangjie
     * @param millis Ҫת�������ں�������
     * @return ���������ַ������磺"2013-02-19 11:48:31"����
     */
    public static String millisToStringDate(long millis){
        return millisToStringDate(millis, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * �����ں���ת��Ϊ�ַ�����
     * @author wangjie
     * @param millis Ҫת�������ں�������
     * @param pattern Ҫת��Ϊ���ַ�����ʽ���磺yyyy-MM-dd HH:mm:ss����
     * @return ���������ַ�����
     */
    public static String millisToStringDate(long millis, String pattern){
        SimpleDateFormat format =  new SimpleDateFormat(pattern);
        return format.format(new Date(millis));

    }

    /**
     * �����ں���ת��Ϊ�ַ������ļ�������
     * @author wangjie
     * @param millis Ҫת�������ں�������
     * @param pattern Ҫת��Ϊ���ַ�����ʽ���磺yyyy-MM-dd HH:mm:ss����
     * @return ���������ַ�����yyyy_MM_dd_HH_mm_ss����
     */
    public static String millisToStringFilename(long millis, String pattern){
        String dateStr = millisToStringDate(millis, pattern);
        return dateStr.replaceAll("[- :]", "_");
    }

    /**
     * �����ں���ת��Ϊ�ַ������ļ�������
     * @author wangjie
     * @param millis Ҫת�������ں�������
     * @return ���������ַ�����yyyy_MM_dd_HH_mm_ss����
     */
    public static String millisToStringFilename(long millis){
        String dateStr = millisToStringDate(millis, "yyyy-MM-dd HH:mm:ss");
        return dateStr.replaceAll("[- :]", "_");
    }



    public static long oneHourMillis = 60 * 60 * 1000; // һСʱ�ĺ�����
    public static long oneDayMillis = 24 * oneHourMillis; // һ��ĺ�����
    public static long oneYearMillis = 365 * oneDayMillis; // һ��ĺ�����

    /**
     * ʱ���ʽ��
     * 1Сʱ���ã����ٷ���ǰ��
     * ����1Сʱ����ʾʱ��������ڣ�
     * ��������죬����ʾ����
     * ������������ʾ���ڣ�
     * ����1������ʾ�ꡣ
     * @param millis
     * @return
     */
    public static String millisToLifeString(long millis){
        long now = System.currentTimeMillis();
        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

        if(now - millis <= oneHourMillis && now - millis > 0l){ // һСʱ��
            String m = millisToStringShort(now - millis, false, false);
            return "".equals(m) ? "1������" : m + "ǰ";
        }

        if(millis >= todayStart && millis <= oneDayMillis + todayStart){ // ���ڽ��쿪ʼ��ʼֵ��С�ڽ��쿪ʼֵ��һ�죨���������ֵ��
            return "���� " + millisToStringDate(millis, "HH:mm");
        }

        if(millis > todayStart - oneDayMillis){ // ���ڣ����쿪ʼֵ��һ�죬�����쿪ʼֵ��
            return "���� " + millisToStringDate(millis, "HH:mm");
        }

        long thisYearStart = string2Millis(millisToStringDate(now, "yyyy"), "yyyy");
        if(millis > thisYearStart){ // ���ڽ���С�ڽ���
            return millisToStringDate(millis, "MM��dd�� HH:mm");
        }

        return millisToStringDate(millis, "yyyy��MM��dd�� HH:mm");
    }

    /**
     * ʱ���ʽ��
     * ���죬��ʾʱ��������ڣ�
     * ��������죬����ʾ����
     * ������������ʾ���ڣ�
     * ����1������ʾ�ꡣ
     * @param millis
     * @return
     */
    public static String millisToLifeString2(long millis){
        long now = System.currentTimeMillis();
        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

        if(millis > todayStart + oneDayMillis && millis < todayStart + 2 * oneDayMillis){ // ����
            return "����" + millisToStringDate(millis, "HH:mm");
        }
        if(millis > todayStart + 2 * oneDayMillis && millis < todayStart + 3 * oneDayMillis){ // ����
            return "����" + millisToStringDate(millis, "HH:mm");
        }

        if(millis >= todayStart && millis <= oneDayMillis + todayStart){ // ���ڽ��쿪ʼ��ʼֵ��С�ڽ��쿪ʼֵ��һ�죨���������ֵ��
            return "���� " + millisToStringDate(millis, "HH:mm");
        }

        if(millis > todayStart - oneDayMillis && millis < todayStart){ // ���ڣ����쿪ʼֵ��һ�죬�����쿪ʼֵ��
            return "���� " + millisToStringDate(millis, "HH:mm");
        }

        long thisYearStart = string2Millis(millisToStringDate(now, "yyyy"), "yyyy");
        if(millis > thisYearStart){ // ���ڽ���С�ڽ���
            return millisToStringDate(millis, "MM��dd�� HH:mm");
        }

        return millisToStringDate(millis, "yyyy��MM��dd�� HH:mm");
    }

    /**
     * ʱ���ʽ��
     * ���죬��ʾʱ��������ڣ�
     * ��������죬����ʾ����
     * ������������ʾ���ڣ�
     * ����1������ʾ�ꡣ
     * @param millis
     * @return
     */
    public static String millisToLifeString3(long millis){
        long now = System.currentTimeMillis();
        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"), "yyyy-MM-dd");

        if(millis > todayStart + oneDayMillis && millis < todayStart + 2 * oneDayMillis){ // ����
            return "����";
        }
        if(millis > todayStart + 2 * oneDayMillis && millis < todayStart + 3 * oneDayMillis){ // ����
            return "����";
        }

        if(millis >= todayStart && millis <= oneDayMillis + todayStart){ // ���ڽ��쿪ʼ��ʼֵ��С�ڽ��쿪ʼֵ��һ�죨���������ֵ��
            return millisToStringDate(millis, "HH:mm");
        }

        if(millis > todayStart - oneDayMillis && millis < todayStart){ // ���ڣ����쿪ʼֵ��һ�죬�����쿪ʼֵ��
            return "���� ";
        }

        return millisToStringDate(millis, "MM��dd��");
    }

    /**
     * �ַ��������ɺ�����
     * @param str
     * @param pattern
     * @return
     */
    public static long string2Millis(String str, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        long millis = 0;
        try {
            millis = format.parse(str).getTime();
        } catch (ParseException e) {
            Logger.e(TAG, e);
        }
        return millis;
    }

    /**
     * ��ý��쿪ʼ�ĺ���ֵ
     * @return
     */
    public static long getTodayStartMillis(){
//        String dateStr = millisToStringDate(System.currentTimeMillis(), "yyyy-MM-dd");
//        return string2Millis(dateStr, "yyyy-MM-dd");
        return getOneDayStartMillis(System.currentTimeMillis());
    }

    public static long getOneDayStartMillis(long millis){
        String dateStr = millisToStringDate(millis, "yyyy-MM-dd");
        return string2Millis(dateStr, "yyyy-MM-dd");
    }


}