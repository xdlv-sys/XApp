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
        int totalFee = order.getTotalFee();
        JknUser user = jknService.get(JknUser.class, order.getUserId());

        //若是提现，则修改用户余额无须通知
        if (order.getTradeType() == TR_TYPE_MONEY) {
            jknService.modifyUserCount(user.getUserId(), totalFee * -1, 0, 0, 0);
            return ES_DONE;
        }

        JknUser modifyUser = new JknUser();
        modifyUser.setUserId(user.getUserId());

        //增加消费总额
        modifyUser.setConsumedCount(totalFee);
        //余额支付
        modifyUser.setCount(order.getBalanceFee() * -1);
        boolean upgrade = false;
        int allConsumedCount = user.getConsumedCount() + totalFee;

        // 升级成正式会员,若消费总额达到59元
        if (user.getUserLevel() < UL_NORMAL && allConsumedCount >= JKN.membership_count) {
            modifyUser.setUserLevel(UL_NORMAL);
            upgrade = true;
        }
        //升级成VIP，若消费总额达到590
        if (user.getVip() < VIP && allConsumedCount >= JKN.vip_cost) {
            modifyUser.setVip(VIP);
            upgrade = true;
        }

        jknService.updateUserProsForProcessOrder(modifyUser);

        if (upgrade){
            jknService.triggerEvent(new JknEvent(EV_USER_UPGRADE,user.getUserId(),null));
        }

        //三级分销 当前用户是会员
        if (upgrade || user.getUserLevel() > UL_NON){
            jknService.triggerEvent(new JknEvent(EV_USER_SETTLEMENT,order.getOrderId(),null));
        }

        return ES_DONE;
    }
}
