package xd.fw.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.bean.OrderSettlement;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserUpdateJob extends EventJob {
    static Map<Integer, UserDesc> userMap = new HashMap<>();

    @Override
    protected Byte[] processType() {
        return new Byte[]{EV_USER_UPDATE, EV_USER_UPGRADE,EV_USER_SETTLEMENT };
    }

    private Byte processUpdate(JknEvent event){
        JknUser self = jknService.get(JknUser.class, event.getDbKey());
        UserDesc user = userMap.get(event.getDbKey());
        user.user = self;
        return ES_DONE;
    }

    private Byte processUpgrade(JknEvent event){
        JknUser self = jknService.get(JknUser.class, event.getDbKey());
        UserDesc user = userMap.get(event.getDbKey());

        if (user == null){
            //user upgrade from customer to membership
            user = new UserDesc(self);
            user.parent = userMap.get(self.getReferrer());
            user.parent.children.add(user);
            userMap.put(self.getUserId(), user);
        } else {
            //user is membership already, never happen since update event has been executed
            user.user = self;
        }

        //upgrade father ...
        List<JknEvent> eventList = new ArrayList<>();
        upgrade(user.parent, eventList);
        upgrade(user.parent.parent,eventList);
        upgrade(user.parent.parent.parent,eventList);

        // update user level and trigger notify event, no need to trigger update event, since user in map already been
        // updated in above function upgrade
        jknService.upgradeJknUser(eventList);

        return ES_DONE;
    }
    private Byte processSettlement(JknEvent event) {
        Order order = jknService.get(Order.class, event.getDbKey());
        UserDesc user = userMap.get(order.getUserId());

        int totalFee = order.getTotalFee();

        List<JknUser> impactedUsers = new ArrayList<>(3);

        OrderSettlement orderSettlement = new OrderSettlement();
        orderSettlement.setOrderId(order.getOrderId());
        orderSettlement.setUserId(user.user.getUserId());
        orderSettlement.setSettlementStatus(SS_INI);

        int fee;
        UserDesc target = user.parent;
        if (target != null) {
            fee = (int) (totalFee * JKN.settlement_one);
            target.user.setCountOne(target.user.getCountOne() + fee);
            orderSettlement.setCountOne(target.user.getUserId());
            orderSettlement.setCountOne(fee);
            orderSettlement.setUserIdOne(target.user.getUserId());
            impactedUsers.add(target.user);
            target = target.parent;
            if (target != null) {
                fee = (int) (totalFee * JKN.settlement_two);
                target.user.setCountTwo(target.user.getCountTwo() + fee);
                orderSettlement.setCountTwo(target.user.getUserId());
                orderSettlement.setCountTwo(fee);
                orderSettlement.setUserIdTwo(target.user.getUserId());
                impactedUsers.add(target.user);
                target = target.parent;
                if (target != null) {
                    fee = (int) (totalFee * JKN.settlement_three);
                    target.user.setCountThree(target.user.getCountThree() + fee);
                    orderSettlement.setCountThree(target.user.getUserId());
                    orderSettlement.setCountThree(fee);
                    orderSettlement.setUserIdThree(target.user.getUserId());
                    impactedUsers.add(target.user);
                }
            }
        }
        // no need to trigger update event
        jknService.saveSettlement(orderSettlement, impactedUsers);
        return ES_DONE;
    }

    @Override
    public Byte process(JknEvent event) {

        // sync user info between map and db including ccout, count_one ...
        if (event.getEventType() == EV_USER_UPDATE.byteValue()){
            return processUpdate(event);
        }
        //user upgrade
        if (event.getEventType() == EV_USER_UPGRADE.byteValue()){
            return processUpgrade(event);
        }

        if (event.getEventType() == EV_USER_SETTLEMENT.byteValue()){
            return processSettlement(event);
        }

        return ES_FAIL;
    }

    private void upgrade(UserDesc user, List<JknEvent> eventList) {
        Byte userLevel = user.user.getUserLevel();
        int childrenCount = user.children.size();
        int allChildrenCount = user.allChildCount();

        byte shouldLevel = UL_NON;
        if (childrenCount >= JKN.gold_ucn && allChildrenCount >= JKN.gold_acn) {
            shouldLevel = UL_GOLD;
        }
        if (childrenCount >= JKN.white_ucn && allChildrenCount >= JKN.white_acn) {
            shouldLevel = UL_WHITE;
        }
        if (childrenCount >= JKN.diamond_ucn && allChildrenCount >= JKN.diamond_acn) {
            shouldLevel = UL_DIAMOND;
        }
        // TODO　are level implementation

        if (userLevel < shouldLevel) {
            //upgrade user level and the record in db will be upgraded later
            user.user.setUserLevel(shouldLevel);
            eventList.add(new JknEvent(EV_USER_NOTIFY  , user.user.getUserId(), shouldLevel));
        }
    }

    @PostConstruct
    public void buildUserTree() {
        int start = 0;
        List<JknUser> users;
        UserDesc self, parent;

        while ((users = jknService.getMemberUser(start, start += 100)).size() > 0) {
            for (JknUser user : users) {
                self = userMap.get(user.getUserId());
                if (self == null) {
                    self = new UserDesc(user);
                    userMap.put(user.getUserId(), self);
                } else {
                    self.user = user;
                }
                if (user.getReferrer() != null) {
                    parent = userMap.get(user.getReferrer());
                    if (parent == null) {
                        parent = new UserDesc(null);
                        userMap.put(user.getReferrer(), parent);
                    }
                    self.parent = parent;
                    parent.children.add(self);
                }
            }
        }
    }

    /*static UserDesc getUserDesc(Integer userId){
        synchronized (userMap){
            return userMap.get(userId);
        }
    }
    static UserDesc setUserDesc(Integer userId, UserDesc userDesc){
        synchronized (userMap){
            return userMap.put(userId, userDesc);
        }
    }*/
}
