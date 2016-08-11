package xd.fw.job;


public class ParkNative {

    static class QueryCarResult{
        public float money;
        public String inTime;
        public int parkedTime;
    }

    public static native QueryCarResult getParkedCarInfo(int carType, String license);

    public static native int initialized(String ip, String dbName, String user, String password);
}
