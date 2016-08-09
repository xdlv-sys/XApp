package xd.fw.job;

import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotifyUserUpdatedJob extends EventJob {

    @Override
    protected byte process(JknEvent event) throws Exception {
        JknUser user = jknService.get(JknUser.class, event.getDbKey());
        String[][] params = {
                {"userId", String.valueOf(user.getUserId())},
                {"vip", String.valueOf(user.getVip())},
                {"userLevelOne", String.valueOf(user.getUserLevel())},
                {"userLevelTwo", String.valueOf(user.getAreaLevel())},
                {"count", String.valueOf(user.getCount())},
                {"unApplyCount", String.valueOf(user.getCountOne() + user.getCountTwo() + user.getCountThree())},
                {"discount", String.valueOf(user.getVip() == VIP ? JKN.vip_discount : 100)},
                {"sign",""}
        };
        List<String> paramList = new ArrayList<>(params.length);
        for (String[] param : params){
            if (param[0].equals("sign")){
                continue;
            }
            paramList.add(param[0] + "=" + param[1] + "&");
        }
        params[params.length -1][1] = FwUtil.getSign(paramList,JKN.security_key);
        logger.info("notify user:" + ArrayUtils.toString(params));
        String ret = HttpClientTpl.post(JKN.user_notify_url, params);
        logger.info("return:" + ret);
        return JSONObject.fromObject(ret).getInt("code") == 200 ? ES_DONE : ES_FAIL;
    }

    @Override
    protected byte[] processType() {
        return new byte[]{EV_USER_NOTIFY, EV_USER_NOTIFY_COUNT};
    }
}
