package xd.fw.job;

import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.OrderSettlement;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotifyUserUpdatedJob extends EventJob {

    @Override
    protected byte process(JknEvent event) throws Exception {
        if (event.getEventType() == EV_USER_NOTIFY){
            return notifyUser(event.getDbKey()) ? ES_DONE : ES_FAIL;
        }
        OrderSettlement orderSettlement = jknService.get(OrderSettlement.class, event.getDbKey());

        if (orderSettlement.getCountOne() > 0){
            notifyUser(orderSettlement.getUserIdOne());
        }
        if (orderSettlement.getCountTwo() > 0){
            notifyUser(orderSettlement.getUserIdTwo());
        }
        if (orderSettlement.getCountThree() > 0){
            notifyUser(orderSettlement.getUserIdThree());
        }
        // notify store user
        if (orderSettlement.getStoreUserId() > 0){
            if (orderSettlement.getStoreUserId() != orderSettlement.getUserIdOne()
                    && orderSettlement.getStoreUserId() != orderSettlement.getUserIdTwo()
                    && orderSettlement.getStoreUserId() != orderSettlement.getUserIdThree()){
                notifyUser(orderSettlement.getStoreUserId());
            }
        }

        return ES_DONE ;
    }

    private boolean notifyUser(int userId) throws Exception {
        JknUser user = jknService.get(JknUser.class, userId);
        String[][] params = {
                {"userId", String.valueOf(user.getUserId())},
                {"vip", String.valueOf(user.getVip())},
                {"userLevelOne", String.valueOf(user.getUserLevel())},
                {"userLevelTwo", String.valueOf(user.getAreaLevel())},
                {"count", String.valueOf(user.getCount())},
                {"unApplyCount", String.valueOf(user.getStoreCount() + user.getCountOne() + user.getCountTwo() + user.getCountThree())},
                {"discount", "100"/*String.valueOf(user.getVip() == VIP ? JKN.vip_discount : 100)*/},
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
        return JSONObject.fromObject(ret).getInt("code") == 200;
    }

    @Override
    protected byte[] processType() {
        return new byte[]{EV_USER_NOTIFY, EV_USER_NOTIFY_COUNT};
    }
}
