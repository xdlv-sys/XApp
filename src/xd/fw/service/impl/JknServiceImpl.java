package xd.fw.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Value("${settlement_period}")
    long settlementPeriod;

    @Override
    @Transactional
    public int saveJknUser(JknUser jknUser) {
        JknUser record = load(JknUser.class,jknUser.getUserId());
        if (record != null){
            //not allowed to change referrer
            if (jknUser.getReferrer() != record.getReferrer()){
                return 202;
            }
            merge(jknUser);
        } else {
            //default value
            jknUser.setVip(NON_VIP);
            jknUser.setUserLevel(UL_NON);
            jknUser.setAreaLevel(AL_NON);
            if (jknUser.getReferrer() == null){
                jknUser.setReferrer(DEFAULT_REFERRER);
            } else {
                //check if the referrer exists
                if (get(JknUser.class,jknUser.getReferrer()) == null){
                    return 204;
                }
            }
            if (jknUser.getReferrer().equals(jknUser.getUserId())){
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
        if (get(Order.class, order.getOrderId()) != null){
            //duplicated order
            return 201;
        }
        order.setTradeStatus(TR_PAY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getLastDate());
        order.setYear((short)calendar.get(Calendar.YEAR));
        order.setMonth((byte)calendar.get(Calendar.MONTH));
        save(order);

        triggerEvent(new JknEvent(EV_ADD_ORDER,order.getOrderId(),null));
        return OK;
    }

    @Override
    public void triggerEvent(JknEvent event) {
        event.setEventStatus(EV_INI);
        save(event);
    }

    @Override
    @Transactional
    public void upgradeJknUser(List<JknEvent> eventList) {
        JknUser user;
        for (JknEvent event: eventList){
            user = load(JknUser.class, event.getDbKey());
            user.setUserLevel((byte)event.getDbInt().intValue());
            update(user);
            //trigger notification for mall
            triggerEvent(event);
        }
    }

    @Override
    public List<JknUser> getMemberUser(int start, int limit) {
        return getList("from JknUser where userLevel > " + UL_NON, null, start,limit);
    }

    @Override
    @Transactional
    public void saveUserCount(JknUser user) {
        JknUser record = load(JknUser.class,user.getUserId());
        record.setCount(user.getCount());
        update(record);
    }

    @Override
    @Transactional
    public void saveSettelment(OrderSettlement orderSettlement, JknUser user, List<JknUser> impactedUsers) {
        save(orderSettlement);
        update(user);

        for (JknUser u : impactedUsers){
            update(u);
            JknEvent event = new JknEvent(EV_USER_SETTLEMENT, u.getUserId(), null);
            event.setTriggerDate(new Timestamp(System.currentTimeMillis() + settlementPeriod));
            triggerEvent(event);
        }
    }

    @Override
    public List<JknEvent> getTriggeringEvent(Byte[] eventType, int start, int limit) {
        StringBuffer hsql = new StringBuffer(128);
        hsql.append("from JknEvent where eventType in(");
        for (Byte type: eventType){
            hsql.append(type).append(",");
        }
        hsql.deleteCharAt(hsql.length() -1).append(") and (triggerDate is null or triggerDate < now())");

        return getList(hsql.toString(),null,start, limit);
    }
}
