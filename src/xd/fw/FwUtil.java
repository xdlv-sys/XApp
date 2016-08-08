package xd.fw;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public class FwUtil {

	public static String UTF8 = "UTF-8";
    //static int[] months = new int[]{0,31,29,31,30,31,30,31,31,30,31,30,31};
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getValidateCode(){
        return String.format("%04d",Math.abs(new Random().nextInt(9999)));
    }

    public static Object getBean(String name){
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return wac == null ? null : wac.getBean(name);
    }

    // sdf is not thread safe so we need add synchronized
    public synchronized static int getLastDayInMonth(int year,int month){
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(String.format("%d-%02d-%02d 00:00:00",year,month,1)));
        } catch (Exception e) {
            throw new IllegalArgumentException("can not parse data", e);
        }
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int[] getLastMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return new int[]{calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1};
    }

    public static String md5(String plainText) throws Exception{
        byte[] secretBytes = MessageDigest.getInstance("md5").digest(
                plainText.getBytes(HttpClientTpl.UTF8));
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static void invokeBeanFields(Object o, BeanFieldProcess p)throws IllegalAccessException{
        Field[] fields = o.getClass().getDeclaredFields();
        Object v;
        for (Field f : fields) {
            f.setAccessible(true);
            v = f.get(o);
            p.process(f, v);
        }
    }
    public static void invokeBeanFieldsWidthConditions(Object o, BeanFieldProcess p, BeanFieldCondition condition)throws IllegalAccessException{
        invokeBeanFields(o,(f,v)->{
            if (condition.accept(f,v)){
                p.process(f,v);
            }
        });
    }

    public interface BeanFieldCondition{
        boolean accept(Field f, Object o);
    }

    public interface BeanFieldProcess{
        void process(Field f, Object o);
    }
    public interface SafeEachProcess<T>{
        void process(T t);
    }

    public static <T> void safeEach(Collection<T> list, SafeEachProcess<T> p){
        if (list == null || list.size() < 1){
            return;
        }
        list.forEach(p::process);
    }

    public static boolean verify(Map<String, Object> params, String key){
        List<String> list = new ArrayList<String>();
        String sign = null;
        String value;
        for (Map.Entry<String,Object> entry : params.entrySet()){
            if (entry.getValue() instanceof String[]){
                value = ((String[])entry.getValue())[0];
            } else {
                value = String.valueOf(entry.getValue());
            }
            if (entry.getKey().equals("sign")){
                sign = value;
                continue;
            }
            if (StringUtils.isBlank(value)){
                continue;
            }
            list.add(entry.getKey() + "=" + value+ "&");
        }
        return getSign(list, key).equals(sign);
    }

    public static String getSign(List<String> params, String key){
        int size = params.size();
        String[] arrayToSort = params.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        sb.append("key=").append(key);
        return MD5.MD5Encode(sb.toString());
    }

    public static void main(String[] args){
        for (int i=1; i< 13;i++)
        System.out.println(i + " = " + getLastDayInMonth(2015,i));
    }
}
