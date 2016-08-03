package xd.fw.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.bean.OrderSettlement;
import xd.fw.service.JknService;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class JknServiceImpl extends HibernateServiceImpl implements JknService {

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
        order.setTradeStatus(TR_PAY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getLastDate());
        order.setYear((short) calendar.get(Calendar.YEAR));
        order.setMonth((byte) calendar.get(Calendar.MONTH));
        save(order);

        triggerEvent(new JknEvent(EV_ADD_ORDER, order.getOrderId(), null));
        return OK;
    }

    @Override
    @Transactional
    public void triggerEvent(JknEvent event) {
        event.setEventId(getPrimaryKey(JknEvent.class));
        event.setEventStatus(ES_INI);
        event.setTryCount((byte) 0);
        if (event.getTriggerDate() == null) {
            event.setTriggerDate(new Timestamp(System.currentTimeMillis()));
        }
        save(event);
    }

    @Override
    @Transactional
    public void upgradeJknUser(List<JknEvent> eventList) {
        JknUser user;
        for (JknEvent event : eventList) {
            user = load(JknUser.class, event.getDbKey());
            user.setUserLevel((byte) event.getDbInt().intValue());
            update(user);
            //trigger notification for mall
            triggerEvent(event);
        }
    }

    @Override
    @Transactional
    public void updateJknUser(JknUser user) {
        update(user);
        triggerEvent(new JknEvent(EV_USER_UPDATE,user.getUserId(),null));
    }

    @Override
    public List<JknUser> getMemberUser(int start, int limit) {
        return getList("from JknUser where userLevel > " + UL_NON, null, start, limit);
    }

    @Override
    @Transactional
    public void saveSettlement(OrderSettlement orderSettlement, List<JknUser> impactedUsers) {
        save(orderSettlement);
        impactedUsers.forEach(this::update);

        JknEvent event = new JknEvent(EV_USER_SETTLEMENT_APPLY
                , orderSettlement.getOrderId(), null);
        event.setTriggerDate(new Timestamp(System.currentTimeMillis() + JKN.settlement_period));
        triggerEvent(event);
    }

    @Override
    public List<JknEvent> getTriggeringEvent(Byte[] eventType, int start, int limit) {
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
    public Byte applySettlement(Integer dbKey) {
        OrderSettlement orderSettlement = load(OrderSettlement.class, dbKey);

        JknUser userOne = load(JknUser.class, orderSettlement.getUserIdOne());
        JknUser userTwo = load(JknUser.class, orderSettlement.getUserIdTwo());
        JknUser userThree = load(JknUser.class, orderSettlement.getUserIdThree());

        userOne.addCount(orderSettlement.getCountOne());
        userOne.setCountOne(userOne.getCountOne() - orderSettlement.getCountOne());

        userTwo.addCount(orderSettlement.getCountTwo());
        userTwo.setCountTwo(userTwo.getCountTwo() - orderSettlement.getCountTwo());

        userThree.addCount(orderSettlement.getCountThree());
        userThree.setCountThree(userThree.getCountThree() - orderSettlement.getCountThree());

        orderSettlement.setSettlementStatus(SS_DONE);

        update(orderSettlement);

        updateJknUser(userOne);
        updateJknUser(userTwo);
        updateJknUser(userThree);

        triggerEvent(new JknEvent(EV_USER_NOTIFY_COUNT,orderSettlement.getOrderId(),null));
        return ES_DONE;
    }
}
