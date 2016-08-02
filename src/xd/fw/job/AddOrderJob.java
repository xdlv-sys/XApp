package xd.fw.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;

@Service
public class AddOrderJob extends EventJob {
    @Value("${membership_count}")
    int membershipCount;

    @Value("${vip_cost}")
    int vipCost;

    @Override
    protected Byte process(JknEvent eventType) {
        /**
         * 消费: 升级成会员 VIP
         * 提现: 调整用户余额
         */
        Order order = jknService.get(Order.class, eventType.getDbKey());
        int totalFee = order.getTotalFee();
        if (totalFee < 0) {
            return EV_FAIL_TF_NEGATIVE;
        }
        int balance = order.getBalanceFee();
        if (balance < 0) {
            return EV_FAIL_BALANCE_NEGATIVE;
        }

        JknUser user = jknService.get(JknUser.class, order.getUserId());

        user.setConsumedCount(user.getConsumedCount() + totalFee);

        //若是提现，则修改用户余额无须通知
        if (order.getTradeStatus().equals(TR_TYPE_MONEY)) {
            user.setCount(user.getCount() - totalFee);
            jknService.update(user);
            jknService.triggerEvent(new JknEvent(EV_USER_UPDATE,user.getUserId(),null));
            return EV_DONE;
        }
        //余额支付
        if (order.getBalanceFee() > 0) {
            user.setCount(user.getCount() - order.getBalanceFee());
        }
        boolean upgrade = false;

        // 升级成正式会员,若消费总额达到59元
        if (user.getUserLevel() < UL_NORMAL && user.getConsumedCount() >= membershipCount) {
            user.setUserLevel(UL_NORMAL);
            upgrade = true;
        }
        //升级成VIP，若消费总额达到590
        if (user.getVip() < VIP && user.getConsumedCount() >= vipCost) {
            user.setVip(VIP);
            upgrade = true;
        }

        if (upgrade){
            jknService.triggerEvent(new JknEvent(EV_USER_UPGRADE,user.getUserId(),null));
            jknService.update(user);
        }

        //三级分销 当前用户是会员
        if (user.getUserLevel() > UL_NON){
            jknService.triggerEvent(new JknEvent(EV_USER_SETTLEMENT,order.getOrderId(),null));
        }

        return EV_DONE;
    }

    @Override
    protected Byte[] processType() {
        return new Byte[]{ EV_ADD_ORDER };
    }
}
