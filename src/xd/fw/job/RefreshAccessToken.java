package xd.fw.job;

import org.springframework.stereotype.Controller;
import xd.fw.HttpClientTpl;
import xd.fw.I18n;
import net.sf.json.JSONObject;
@Controller
public class RefreshAccessToken extends BaseJob{

    String accessToken;

    public synchronized String getAccessToken() throws Exception {
        if (accessToken == null){
            execute();
        }
        return accessToken;
    }
    synchronized void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
    public void doExecute() throws Exception {
        logger.info("start to refresh token");
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token" +
                "?grant_type=client_credential&appid=%s&secret=%s"
                , I18n.getI18n("appID"),I18n.getI18n("appsecret"));
        String json = HttpClientTpl.get(url);
        String token = JSONObject.fromObject(json).getString("access_token");
        setAccessToken(token);
        logger.info("get token:" + token);
    }
}
