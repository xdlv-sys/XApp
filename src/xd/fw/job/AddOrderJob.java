package xd.fw.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;

@Service
public class AddOrderJob extends EventJob {

    @Override
    protected Byte process(JknEvent event) {
        /**
         * 消费: 升级成会员 VIP
         * 提现: 调整用户余额
         */
        Order order = jknService.get(Order.class, event.getDbKey());
        int totalFee = order.getTotalFee();
        if (totalFee < 0) {
            return ES_FAIL_TF_NEGATIVE;
        }
        int balance = order.getBalanceFee();
        if (balance < 0) {
            return ES_FAIL_BALANCE_NEGATIVE;
        }

        JknUser user = jknService.get(JknUser.class, order.getUserId());

        //若是提现，则修改用户余额无须通知
        if (order.getTradeType().equals(TR_TYPE_MONEY)) {
            user.setCount(user.getCount() - totalFee);
            jknService.updateJknUser(user);
            return ES_DONE;
        }
        //增加消费总额
        user.setConsumedCount(user.getConsumedCount() + totalFee);
        //余额支付
        if (order.getBalanceFee() > 0) {
            user.setCount(user.getCount() - order.getBalanceFee());
        }
        boolean upgrade = false;

        // 升级成正式会员,若消费总额达到59元
        if (user.getUserLevel() < UL_NORMAL && user.getConsumedCount() >= JKN.membership_count) {
            user.setUserLevel(UL_NORMAL);
            upgrade = true;
        }
        //升级成VIP，若消费总额达到590
        if (user.getVip() < VIP && user.getConsumedCount() >= JKN.vip_cost) {
            user.setVip(VIP);
            upgrade = true;
        }

        jknService.update(user);

        if (upgrade){
            jknService.triggerEvent(new JknEvent(EV_USER_UPGRADE,user.getUserId(),null));
        } else {
            //若用户己是会员，则需要更新Map
            if (user.getUserLevel() > UL_NON){
                jknService.triggerEvent(new JknEvent(EV_USER_UPDATE, user.getUserId(),null));
            }
        }

        //三级分销 当前用户是会员
        if (user.getUserLevel() > UL_NON){
            jknService.triggerEvent(new JknEvent(EV_USER_SETTLEMENT,order.getOrderId(),null));
        }

        return ES_DONE;
    }

    @Override
    protected Byte[] processType() {
        return new Byte[]{ EV_ADD_ORDER };
    }
}
