package xd.fw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.bean.OrderSettlement;
import xd.fw.bean.mapper.JknUserMapper;
import xd.fw.service.JknService;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class JknServiceImpl extends HibernateServiceImpl implements JknService {

    @Autowired
    JknUserMapper jknUserMapper;

    @Override
    @Transactional
    public int saveJknUser(JknUser jknUser) {
        JknUser record = get(JknUser.class, jknUser.getUserId());
        if (record != null) {
            //not allowed to change referrer
            if (jknUser.getReferrer() != record.getReferrer()) {
                return 202;
            }
            merge(jknUser);
        } else {
            //default value
            jknUser.setVip(NON_VIP);
            jknUser.setUserLevel(UL_NON);
            jknUser.setAreaLevel(AL_NON);
            if (jknUser.getReferrer() == null) {
                jknUser.setReferrer(DEFAULT_REFERRER);
            } else {
                //check if the referrer exists
                if (get(JknUser.class, jknUser.getReferrer()) == null) {
                    return 204;
                }
            }
            if (jknUser.getReferrer().equals(jknUser.getUserId())) {
                // not allow to refer self
                return 203;
            }
            jknUser.setConsumedCount(0);
            jknUser.setCount(0);
            jknUser.setCountOne(0);
            jknUser.setCountTwo(0);
            jknUser.setCountThree(0);
            save(jknUser);
        }
        //triggerEvent(new JknEvent(EV_USER_UPDATE,jknUser.getUserId(),null));
        return OK;
    }

    @Override
    @Transactional
    public int saveOrder(Order order) {
        if (get(Order.class, order.getOrderId()) != null) {
            //duplicated order
            return 201;
        }
        // 若取现，则必须余额足够
        if (order.getTradeType()== TR_TYPE_MONEY){
            if (order.getTotalFee() > load(JknUser.class, order.getUserId()).getCount()){
                return 202;
            }
        }
        order.setTradeStatus(TR_PAY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getLastDate());
        order.setYear((short) calendar.get(Calendar.YEAR));
        order.setMonth((byte) calendar.get(Calendar.MONTH));
        save(order);

        triggerEvent(new JknEvent(EV_USER_PROCESS_ORDER, order.getOrderId(), null));
        return OK;
    }

    @Override
    @Transactional
    public void triggerEvent(JknEvent event) {
        //event.setEventId(getPrimaryKey(JknEvent.class));
        event.setEventStatus(ES_INI);
        event.setTryCount((byte) 0);
        if (event.getTriggerDate() == null) {
            event.setTriggerDate(new Timestamp(System.currentTimeMillis()));
        }
        save(event);
    }

    @Override
    public List<JknUser> getMemberUser(int start, int limit) {
        return getList("from JknUser where userLevel > " + UL_NON, null, start, limit);
    }

    @Override
    public List<JknEvent> getTriggeringEvent(byte[] eventType, int start, int limit) {
        // the key: keep event in order
        StringBuffer hsql = new StringBuffer(128);
        hsql.append("from JknEvent where eventStatus in (").append(ES_INI).append("" +
                ",").append(ES_FAIL).append(") and tryCount <").append(4).append(" and eventType in(");
        for (Byte type : eventType) {
            hsql.append(type).append(",");
        }
        hsql.deleteCharAt(hsql.length() - 1).append(") and (triggerDate is null or triggerDate < now()) order by eventId");

        return getList(hsql.toString(), null, start, limit);
    }

    @Override
    @Transactional
    public void modifyUserCount(int userId, int count
            , int countOne, int countTwo, int countThree) {
        jknUserMapper.modifyUserCount(userId, count, countOne,countTwo, countThree);
    }

    @Override
    @Transactional
    public void updateUserProsForProcessOrder(JknUser modifyUser) {
        JknUser user = load(JknUser.class, modifyUser.getUserId());

        user.setConsumedCount(user.getConsumedCount() + modifyUser.getConsumedCount());
        user.setCount(user.getCount() + modifyUser.getCount());

        if (user.getUserLevel() < modifyUser.getUserLevel()){
            user.setUserLevel(modifyUser.getUserLevel());
        }

        if (user.getVip() < modifyUser.getVip()){
            user.setVip(modifyUser.getVip());
        }
        update(user);
    }

    @Override
    @Transactional
    public  void upgradeUsers(List<JknEvent> eventList) {
        JknUser user;
        for (JknEvent event : eventList) {
            user = load(JknUser.class, event.getDbKey());
            if (user.getUserLevel() < event.getDbInt()){
                user.setUserLevel((byte) event.getDbInt());
                update(user);
                //trigger notification for mall
                triggerEvent(event);
            }
        }
    }

    @Override
    @Transactional
    public  void saveSettlement(OrderSettlement orderSettlement) {
        save(orderSettlement);

        JknUser user;
        if (orderSettlement.getUserIdOne() != 0){
            user = load(JknUser.class, orderSettlement.getUserIdOne());
            user.setCountOne(user.getCountOne() + orderSettlement.getCountOne());
            update(user);
        }
        if (orderSettlement.getUserIdTwo() != 0){
            user = load(JknUser.class, orderSettlement.getUserIdTwo());
            user.setCountTwo(user.getCountTwo() + orderSettlement.getCountTwo());
            update(user);
        }
        if (orderSettlement.getUserIdThree() != 0){
            user = load(JknUser.class, orderSettlement.getUserIdThree());
            user.setCountThree(user.getCountThree() + orderSettlement.getCountThree());
            update(user);
        }

        JknEvent event = new JknEvent(EV_USER_SETTLEMENT_APPLY
                , orderSettlement.getOrderId(), null);
        event.setTriggerDate(new Timestamp(System.currentTimeMillis() + JKN.settlement_period));
        triggerEvent(event);
    }

    @Override
    @Transactional
    public  byte applySettlement(int dbKey) {
        OrderSettlement orderSettlement = load(OrderSettlement.class, dbKey);

        JknUser userOne = load(JknUser.class, orderSettlement.getUserIdOne());
        JknUser userTwo = load(JknUser.class, orderSettlement.getUserIdTwo());
        JknUser userThree = load(JknUser.class, orderSettlement.getUserIdThree());

        userOne.setCount(orderSettlement.getCountOne() + userOne.getCount());
        userOne.setCountOne(userOne.getCountOne() - orderSettlement.getCountOne());

        userTwo.setCount(orderSettlement.getCountTwo() + userTwo.getCount());
        userTwo.setCountTwo(userTwo.getCountTwo() - orderSettlement.getCountTwo());

        userThree.setCount(orderSettlement.getCountThree() + userThree.getCount());
        userThree.setCountThree(userThree.getCountThree() - orderSettlement.getCountThree());

        orderSettlement.setSettlementStatus(SS_DONE);

        update(orderSettlement);

        update(userOne);
        update(userTwo);
        update(userThree);

        triggerEvent(new JknEvent(EV_USER_NOTIFY_COUNT,orderSettlement.getOrderId(),null));
        return ES_DONE;
    }
}
