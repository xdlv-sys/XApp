package xd.fw.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    @Value("${gold_ucn}")
    int goldUcn = 1;
    @Value("${gold_acn}")
    int goldAcn = 0;

    @Value("${white_ucn}")
    int whiteUcn = 10;
    @Value("${white_acn}")
    int whiteAcn = 0;

    @Value("${diamond_ucn}")
    int diamondUcn = 10;
    @Value("${diamond_acn}")
    int diamondAcn = 30;

    @Value("${settlement_one}")
    float settlementOne;
    @Value("${settlement_two}")
    float settlementTwo;
    @Value("${settlement_three}")
    float settlementThree;

    @Override
    protected Byte[] processType() {
        return new Byte[]{EV_USER_UPDATE, EV_USER_UPGRADE,EV_USER_SETTLEMENT };
    }
    private Byte processUpdate(JknEvent event){
        JknUser self = jknService.get(JknUser.class, event.getDbKey());
        UserDesc user = userMap.get(event.getDbKey());
        user.user = self;
        return EV_DONE;
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

            //upgrade father ...
            List<JknEvent> eventList = new ArrayList<>();
            upgrade(user.parent, eventList);
            upgrade(user.parent.parent,eventList);
            upgrade(user.parent.parent.parent,eventList);

            // update user level and trigger notify event
            jknService.upgradeJknUser(eventList);
        } else {
            //user is membership already.
            user.user = self;
        }
        return EV_DONE;
    }
    private Byte processSettlement(JknEvent event) {
        Order order = jknService.get(Order.class, event.getDbKey());
        UserDesc user = userMap.get(order.getUserId());

        int totalFee = order.getTotalFee();

        List<JknUser> impactedUsers = new ArrayList<>(3);

        OrderSettlement orderSettlement = new OrderSettlement();
        orderSettlement.setOrderId(order.getOrderId());
        orderSettlement.setUserId(user.user.getUserId());

        int fee;
        UserDesc target = user.parent;
        if (target != null) {
            fee = (int) (totalFee * settlementOne);
            target.user.setCountOne(target.user.getCountOne() + fee);
            orderSettlement.setCountOne(target.user.getUserId());
            orderSettlement.setCountOne(fee);
            orderSettlement.setUserIdOne(target.user.getUserId());
            impactedUsers.add(target.user);
            target = target.parent;
            if (target != null) {
                fee = (int) (totalFee * settlementTwo);
                target.user.setCountTwo(target.user.getCountTwo() + fee);
                orderSettlement.setCountTwo(target.user.getUserId());
                orderSettlement.setCountTwo(fee);
                orderSettlement.setUserIdTwo(target.user.getUserId());
                impactedUsers.add(target.user);
                target = target.parent;
                if (target != null) {
                    fee = (int) (totalFee * settlementThree);
                    target.user.setCountThree(target.user.getCountThree() + fee);
                    orderSettlement.setCountThree(target.user.getUserId());
                    orderSettlement.setCountThree(fee);
                    orderSettlement.setUserIdThree(target.user.getUserId());
                    impactedUsers.add(target.user);
                }
            }
        }
        jknService.saveSettlement(orderSettlement, impactedUsers);
        return EV_DONE;
    }

    @Override
    public Byte process(JknEvent eventType) {

        // sync user info between map and db including ccout, count_one ...
        if (eventType.getEventType() == EV_USER_UPDATE.byteValue()){
            return processUpdate(eventType);
        }
        //user upgrade
        if (eventType.getEventType() == EV_USER_UPGRADE.byteValue()){
            return processUpgrade(eventType);
        }

        if (eventType.getEventType() == EV_USER_SETTLEMENT.byteValue()){
            return processSettlement(eventType);
        }

        return EV_FAIL;
    }

    private void upgrade(UserDesc user, List<JknEvent> eventList) {
        Byte userLevel = user.user.getUserLevel();
        int childrenCount = user.children.size();
        int allChildrenCount = user.allChildCount();

        byte shouldLevel = UL_NON;
        if (childrenCount >= goldUcn && allChildrenCount >= goldAcn) {
            shouldLevel = UL_GOLD;
        }
        if (childrenCount >= whiteUcn && allChildrenCount >= whiteAcn) {
            shouldLevel = UL_WHITE;
        }
        if (childrenCount >= diamondUcn && allChildrenCount >= diamondAcn) {
            shouldLevel = UL_DIAMOND;
        }
        // TODO　are level implementation

        if (userLevel < shouldLevel) {
            //upgrade user level and the record in db will be upgraded later
            user.user.setUserLevel(shouldLevel);
            eventList.add(new JknEvent(EV_USER_NOTIFY
                    , user.user.getUserId(), shouldLevel));
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
