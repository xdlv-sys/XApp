package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.bean.OrderSettlement;
import xd.fw.service.JknService;

import java.util.Map;

@Service
public class UserSettlement implements UserHandler {
    @Autowired
    JknService jknService;

    @Override
    public byte process(JknEvent event, Map<Integer, UserDesc> userMap) {
        Order order = jknService.get(Order.class, event.getDbKey());
        UserDesc user = userMap.get(order.getUserId());

        int totalFee = order.getTotalFee();

        OrderSettlement orderSettlement = new OrderSettlement();
        orderSettlement.setOrderId(order.getOrderId());
        orderSettlement.setUserId(order.getUserId());
        orderSettlement.setSettlementStatus(SS_INI);

        int fee;
        UserDesc target = user.parent;
        JknUser targetUser;
        if (target != null) {
            if (jknService.get(JknUser.class, target.userId).getUserLevel() >= UL_GOLD){
                fee = (int) (totalFee * JKN.settlement_one);
                orderSettlement.setCountOne(fee);
                orderSettlement.setUserIdOne(target.userId);
            }

            target = target.parent;
            if (target != null) {
                if (jknService.get(JknUser.class, target.userId).getUserLevel() >= UL_WHITE){
                    fee = (int) (totalFee * JKN.settlement_two);
                    orderSettlement.setCountTwo(fee);
                    orderSettlement.setUserIdTwo(target.userId);
                }

                target = target.parent;
                if (target != null) {
                    if (jknService.get(JknUser.class, target.userId).getUserLevel() >= UL_DIAMOND){
                        fee = (int) (totalFee * JKN.settlement_three);
                        orderSettlement.setCountThree(fee);
                        orderSettlement.setUserIdThree(target.userId);
                    }
                }
            }
        }

        jknService.saveSettlement(orderSettlement);
        return ES_DONE;
    }
}
