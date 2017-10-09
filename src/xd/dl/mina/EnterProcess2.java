package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xd.fw.HttpClientTpl;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;

/**
 * 上报云平台，用于推送消息
 */
@Service
public class EnterProcess2 extends BaseEnterOutProcess {
    @Value("${sq_scan_login_address}")
    String loginAddress;
    @Value("${sq_user}")
    String user;
    @Value("${sq_password}")
    String password;

    @PostConstruct
    public void init() throws Exception{
        if (sq){
            loginTo();
        }
    }

    @Override
    public String[][] constructParams(TLVMessage request) throws Exception {
        String[][] superParams = super.constructParams(request);
        if (superParams != null){
            return superParams;
        }

        String carNumber = (String) request.getValue();
        String enterTime = (String) request.getNextValue(0);
        return new String[][]{
                {"parkId", parkId},
                {"carNumber", carNumber},
                //2017-05-22 164708
                {"enterTime", convertDate(enterTime)}
        };
    }
    @Override
    int accessType() {
        return 0;
    }

    @Scheduled(cron="0 * * * * ?")
    public void loginTo() throws Exception {
        logger().info("start to login {} with {}/{}", loginAddress, user, password);
        String json = HttpClientTpl.post(loginAddress,new String[][]{
                {"userAccount", user},
                {"password", password}
        });
        JSONObject jsonObject = JSONObject.fromObject(json);
        if (jsonObject.getInt("code") != 200){
            logger().error("login error: {}", jsonObject);
            return;
        }
        token = jsonObject.getString("_TK");
        logger().info("get TK:{}", token);
    }
}
