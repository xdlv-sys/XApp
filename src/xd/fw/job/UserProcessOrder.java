package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.service.JknService;

import java.util.Map;

@Service
public class UserProcessOrder implements UserHandler {
    @Autowired
    JknService jknService;

    @Override
    public byte process(JknEvent event, Map<Integer, UserDesc> userMap) {
        Order order = jknService.get(Order.class, event.getDbKey());

        //若是提现，则修改用户余额无须通知
        if (order.getTradeType() == TR_TYPE_MONEY) {
            jknService.modifyUserCount(order.getUserId(), order.getTotalFee() * -1, 0, 0, 0);
            return ES_DONE;
        }

        byte[] ret = jknService.updateUserProsForProcessOrder(order);

        if (ret[0] == TRUE){
            jknService.triggerEvent(new JknEvent(EV_USER_UPGRADE,order.getUserId(),null));
        }

        //三级分销 当前用户是会员
        if (ret[0] == TRUE || ret[1] > UL_NON){
            jknService.triggerEvent(new JknEvent(EV_USER_SETTLEMENT,order.getOrderId(),null));
        }

        return ES_DONE;
    }
}
