package xd.fw;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class FwUtil {

	public static String UTF8 = "UTF-8";
    //static int[] months = new int[]{0,31,29,31,30,31,30,31,31,30,31,30,31};
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getValidateCode(){
        return String.format("%04d",Math.abs(new Random().nextInt(9999)));
    }

    public static Object getBean(String name){
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return wac.getBean(name);
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
    public static void main(String[] args){
        for (int i=1; i< 13;i++)
        System.out.println(i + " = " + getLastDayInMonth(2015,i));
    }
}
