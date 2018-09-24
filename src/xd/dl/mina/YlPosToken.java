package xd.dl.mina;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xd.fw.HttpClientTpl;
import xd.fw.WxUtil;

@Service
public class YlPosToken extends PayYl {
    @Value("${pos_token_url}")
    String tokenUrl;

    String accessToken;

    Logger logger = LoggerFactory.getLogger(YlPosToken.class);

    @Scheduled(cron = "0 0/55 * * * ?")
    public void getToken() throws Exception {

        logger.info("start to get token");
        String timeStamp = getTimeStamp();
        String nonce = WxUtil.getRandomStringByLength(32);

        JSONObject json = new JSONObject();
        json.put("appId", appId);
        json.put("timestamp", timeStamp);
        json.put("nonce", nonce);
        json.put("signature", sha1(appId + timeStamp + nonce + appKey));

        String ret = HttpClientTpl.postJson(tokenUrl, json.toString(), null);
        logger.info("token:{}", ret);

        accessToken = (String)JSONObject.fromObject(ret).get("accessToken");
    }
}
