package xd.fw.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Service
public class ParkNative {

    Logger logger = LoggerFactory.getLogger(ParkNative.class);

    public static class ParkedInfo {
        public String carNumber;
        public String sInTime;
        public float fMoney;
        public int iParkedTime;
        public int iReturn;
    }

    Method initialized, unitialized, getParkedCarInfo, payParkCarFee;

    @Value("${db_host}")
    String host;
    @Value("${db_name}")
    String dbName;
    @Value("${db_user}")
    String user;
    @Value("${db_pwd}")
    String pwd;

    //@PostConstruct
    public void init() throws Exception {
        Class<?> cls = Class.forName("ParkNative");
        initialized = cls.getMethod("initialized", String.class, String.class, String.class, String.class);
        initialized.setAccessible(true);

        unitialized = cls.getMethod("unitialized");
        unitialized.setAccessible(true);

        getParkedCarInfo = cls.getMethod("getParkedCarInfo", int.class, String.class);
        getParkedCarInfo.setAccessible(true);

        payParkCarFee = cls.getMethod("payParkCarFee",String.class,String.class,float.class);
        payParkCarFee.setAccessible(true);

        initialized.invoke(null, host, dbName, user, pwd);
    }

    @PreDestroy
    public void destroy()throws Exception{
        if (unitialized != null){
            unitialized.invoke(null);
        }
    }

    public ParkedInfo getParkedInfo(int carType, String carNumber) {
        return (ParkedInfo) executeTemplate(args -> {
            Object obj = getParkedCarInfo.invoke(null, carType, carNumber);
            ParkedInfo parkedInfo = new ParkedInfo();
            copyProperties(parkedInfo, obj);
            return parkedInfo;
        });
    }

    private Object executeTemplate(NativeProcess pro, Object... args){
        try{
            return pro.process(args);
        } catch (Exception e) {
            logger.error("",e);
        }
        return null;
    }

    public boolean payFee(String carNumber,String timeStamp, float totalFee){
        return (boolean)executeTemplate(args->{
            return ((int)payParkCarFee.invoke(null, carNumber, timeStamp, totalFee)) == 0;
        });
    }

    static class ParkedInfo2 {
        public String sInTime;
        public float fMoney;
        public int iParkedTime;
        public int iReturn;
    }

    interface NativeProcess{
        Object process(Object... args) throws Exception;
    }

    private static void copyProperties(Object dest, Object origin)throws Exception{
        Field tmp;
        for (Field f: dest.getClass().getDeclaredFields()){
            try{
                tmp = origin.getClass().getDeclaredField(f.getName());
            } catch (NoSuchFieldException e){
                continue;
            }

            tmp.setAccessible(true);
            if (tmp == null || tmp.get(origin) == null){
                continue;
            }
            f.setAccessible(true);
            f.set(dest,tmp.get(origin));
        }
    }

    public static void main(String[] args) throws Exception {
        ParkedInfo2 obj = new ParkedInfo2();
        obj.sInTime = "123";
        obj.fMoney = 1.0f;
        obj.iParkedTime = 1;
        obj.iReturn = 11;


        ParkedInfo parkedInfo = new ParkedInfo();
        copyProperties(parkedInfo, obj);
    }

}
