package xd.fw.job;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface IDongHui {
    String CODE = "fsAsf2015";

    String serverHostName = "wap.dh-etc.com";

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

    default JSONObject post(String address, String[][] params){
        JSONObject jsonObject;
        String json = null;
        try {
            json = HttpClientTpl.post(address, params);
            jsonObject = JSONObject.fromObject(json);
        } catch (Exception e) {
            logger().error("error json:" + json ,e);
            jsonObject = new JSONObject();
            jsonObject.put("state",false);
            jsonObject.put("msg","网络出现问题");
        }
        return jsonObject;
    }

    default Logger logger(){
        return LoggerFactory.getLogger(getClass());
    }
}
