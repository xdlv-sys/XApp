package xd.fw.job;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;

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
}
