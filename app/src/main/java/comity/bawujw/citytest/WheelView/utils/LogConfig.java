package comity.bawujw.citytest.WheelView.utils;

/**
 * ��־����
 * Created with IntelliJ IDEA.
 * Author: wangjie  email:tiantian.china.2@gmail.com
 * Date: 14-3-26
 * Time: ����4:12
 */
public class LogConfig {
    private boolean debug = true; // ��������̨���ģʽ
    private boolean logFile = false; // �����ͻ��˱�����־��¼ģʽ
    private String logFilePath; // ������־��¼��·��

    public boolean isDebug() {
        return debug;
    }

    public LogConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public boolean isLogFile() {
        return logFile;
    }

    public LogConfig setLogFile(boolean logFile) {
        this.logFile = logFile;
        return this;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public LogConfig setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
        return this;
    }

}