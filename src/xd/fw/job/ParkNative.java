package xd.fw.job;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class ParkNative {

    Logger logger = LoggerFactory.getLogger(ParkNative.class);

    public static class ParkedInfo {
        public String sInTime;
        public float fMoney;
        public int iParkedTime;
        public int iReturn;
    }

    Method initialized, unitialized, getParkedCarInfo;

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
    }

    public ParkedInfo getParkedInfo(int carType, String carNumber) {
        try {
            initialized.invoke(null, host, dbName, user, pwd);
            Object obj = getParkedCarInfo.invoke(null, carType, carNumber);
            ParkedInfo parkedInfo = new ParkedInfo();
            copyProperties(parkedInfo, obj);
            return parkedInfo;
        } catch (Exception e) {
            logger.error("",e);
        } finally {
            try {
                unitialized.invoke(null);
            } catch (Exception e) {
            }
        }
        return null;
    }

    static class ParkedInfo2 {
        public String sInTime;
        public float fMoney;
        public int iParkedTime;
        public int iReturn;
    }

    private static void copyProperties(Object dest, Object origin) throws Exception{
        Field tmp;
        for (Field f: dest.getClass().getDeclaredFields()){
            tmp = origin.getClass().getDeclaredField(f.getName());
            if (tmp == null || tmp.get(origin) == null){
                continue;
            }
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
