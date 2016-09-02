package xd.fw.mina;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class Thumb extends SendRequest {

    @Value("${file_server_url}")
    String fileServerUrl;

    Logger logger = LoggerFactory.getLogger(Thumb.class);
    String HEADER_AGENT = "User-Agent";
    String AUTHORIZATION = "Authorization";
    String NEW_FILE_NAME = "Filename";

    @Override
    String[][] constructParams(TLVMessage request) throws Exception {
        logger.info("start to send to thumb");

        String fileName = String.valueOf(System.currentTimeMillis()) + "-hjc.jpg";

        //首先上传到文件服务器
        String ret = HttpClientTpl.executeMulti("http://" + fileServerUrl,
                new String[][]{
                        {AUTHORIZATION, "etc"},
                        {NEW_FILE_NAME, fileName},
                        {HEADER_AGENT, getUserAgent()}
                },
                new Object[][]{{fileName, request.getNextValue(0)}});

        logger.info("return from file sever:" + ret);
        JSONObject retJson = JSONObject.fromObject(ret);
        if (200 == retJson.getInt("status")) {
            String timeStamp = getTimeStamp();
            return new String[][]{
                    {"Parkingno", parkingNo},
                    {"Carnumber", (String) request.getValue()},
                    {"Thumbname", retJson.getString("key")},
                    {"Uniquekey", uniqueKey},
                    {"Timestamp", timeStamp},
                    {"Token", md5(timeStamp, parkingNo, uniqueKey)}
            };
        }
        throw new Exception("file upload failed");
    }

    @Override
    void constructMessage(TLVMessage ret, TLVMessage request) {
        ret.setNext(request.getNextValue(0)).setNext(0);
    }

    @Override
    String svrAddress() {
        return "http://" + dhHost + "/mobile/index.php?v=2.0&act=thirdparking&op=thumb";
    }
}
