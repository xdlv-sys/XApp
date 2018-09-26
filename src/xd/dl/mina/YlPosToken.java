package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import xd.dl.job.IDongHui;
import xd.fw.HttpClientTpl;
import xd.fw.WxUtil;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class YlPosToken implements IDongHui {
    @Value("${yl_pos_flag}")
    boolean ylFlag;
    @Value("${pos_token_url}")
    String tokenUrl;
    @Value("${app_id}")
    String appId;
    @Value("${app_key}")
    String appKey;

    @Value("${yl_tid_map}")
    String ylTidValue;
    Map<String, String> ylTidMap = new HashMap<>();

    volatile String accessToken;

    Logger logger = LoggerFactory.getLogger(YlPosToken.class);

    @Autowired
    TaskScheduler scheduler;

    @PostConstruct
    void init() {
        if (!ylFlag) {
            logger.info("yl configure false");
            return;
        }
        if (StringUtils.isNotBlank(ylTidValue)) {
            // yl_tid_map=0001-00810001;0002-00810001
            Arrays.stream(ylTidValue.split(";")).forEach(s -> ylTidMap.put(s.substring(0, s.indexOf("-"))
                    , s.substring(s.indexOf("-") + 1)));
        }
        scheduler.scheduleAtFixedRate(this::getToken, new Date(), 58 * 60 * 1000);
    }
    void getToken() {

        logger.info("start to get token");
        String timeStamp = getTimeStamp();
        String nonce = WxUtil.getRandomStringByLength(32);

        JSONObject json = new JSONObject();
        json.put("appId", appId);
        json.put("timestamp", timeStamp);
        json.put("nonce", nonce);
        json.put("signature", sha1(appId + timeStamp + nonce + appKey));
        try {
            String ret = HttpClientTpl.postJson(tokenUrl, json.toString(), null);
            logger.info("token:{}", ret);
            accessToken = (String)JSONObject.fromObject(ret).get("accessToken");
        } catch (Exception e) {
            logger.error("can not get token", e);
        }
    }

    public String getAccessToken() {
        return accessToken;
    }


    private static String sha1(String str){
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(StandardCharsets.UTF_8));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    String getTidBySessionId(String sessionId) {
        return this.ylTidMap.get(sessionId);
    }
}
