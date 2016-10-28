package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
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

        int totalFee = order.getSettlementFee() == -1
                ? order.getTotalFee() : order.getSettlementFee();

        OrderSettlement orderSettlement = new OrderSettlement();
        orderSettlement.setOrderId(order.getOrderId());
        orderSettlement.setUserId(order.getUserId());
        orderSettlement.setSettlementStatus(SS_INI);

        //店铺走货
        UserDesc storeUserDesc;
        if (order.getStoreUserId() > 0
                && (storeUserDesc = userMap.get(order.getStoreUserId())) != null){
            orderSettlement.setStoreUserId(order.getStoreUserId());
            float radio = 0f;
            if (storeUserDesc.storeKeeper == STORE_KEEPER_FIRST){
                radio = JKN.store_order_first_settlement;
            }
            if (storeUserDesc.storeKeeper == STORE_KEEPER_EXTEND){
                radio = JKN.store_order_extend_settlement;
            }
            orderSettlement.setStoreCount((int) (totalFee * radio));
        }

        int fee;
        UserDesc target = user.parent;
        if (target != null) {
            if (target.userLevel >= UL_GOLD){
                fee = fee(target,JKN.settlement_one, totalFee);
                orderSettlement.setCountOne(fee);
                orderSettlement.setUserIdOne(target.userId);
            }

            target = target.parent;
            if (target != null) {
                if (target.userLevel >= UL_WHITE){
                    fee = fee(target,JKN.settlement_two, totalFee);
                    orderSettlement.setCountTwo(fee);
                    orderSettlement.setUserIdTwo(target.userId);
                }

                target = target.parent;
                if (target != null) {
                    if (target.userLevel >= UL_DIAMOND){
                        fee = fee(target,JKN.settlement_three, totalFee);
                        orderSettlement.setCountThree(fee);
                        orderSettlement.setUserIdThree(target.userId);
                    }
                }
            }
        }

        jknService.saveSettlement(orderSettlement);
        return ES_DONE;
    }

    private int fee(UserDesc target, float base, int totalFee){
        if (target.areaLevel == AL_REGION){
            base += JKN.region_settlement;
        }
        if (target.areaLevel == AL_CITY){
            base += JKN.city_settlement;
        }
        if (target.storeKeeper == STORE_KEEPER_FIRST){
            base += JKN.store_keeper_first_settlement;
        }
        if (target.storeKeeper == STORE_KEEPER_EXTEND){
            base += JKN.store_keeper_extend_settlement;
        }
        return (int) (base * totalFee);
    }
}
