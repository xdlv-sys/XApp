package xd.fw.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xd.fw.FwUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface IDongHui {

    default String md5(String... strings) throws Exception {
        StringBuffer buffer = new StringBuffer();
        for (String string : strings) {
            buffer.append(string);
        }
        return FwUtil.md5(buffer.toString());
    }

    default String getTimeStamp(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    default Logger logger(){
        return LoggerFactory.getLogger(getClass());
    }

    default String getUserAgent(){
        StringBuffer buffer = new StringBuffer();
        String sdk = "SZFS/" + "1.0.0";
        String javaVersion = "Java/" + System.getProperty("java.version");
        buffer.append(sdk).append(" (").append(System.getProperty("os.name")).append(" ")
                .append(System.getProperty("os.arch")).append(" ").append(System.getProperty("os.version"))
                .append(") ").append(javaVersion);
        return buffer.toString();
    }
}
