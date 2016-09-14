package xd.fw.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.FwUtil;
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

    Logger logger = LoggerFactory.getLogger(getClass());

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
        if (order.getTradeType() == TR_TYPE_MONEY) {
            if (order.getTotalFee() > load(JknUser.class, order.getUserId()).getCount()) {
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
        return getList("from JknUser where userLevel > " + UL_NON + " order by userId", null, start, limit);
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
        jknUserMapper.modifyUserCount(userId, count, countOne, countTwo, countThree);
    }

    @Override
    @Transactional
    public byte[] updateUserProsForProcessOrder(Order order) {
        JknUser jknUser = load(JknUser.class, order.getUserId());

        //增加消费总额
        jknUser.setConsumedCount(jknUser.getConsumedCount() + order.getTotalFee());
        //余额支付
        jknUser.setCount(jknUser.getCount() - order.getBalanceFee());
        boolean upgrade = false;

        // 升级成正式会员,若消费总额达到59元
        if (jknUser.getUserLevel() < UL_NORMAL && jknUser.getConsumedCount() >= JKN.membership_count) {
            jknUser.setUserLevel(UL_NORMAL);
            upgrade = true;
        }
        //升级成VIP，若消费总额达到590
        if (jknUser.getVip() < VIP && jknUser.getConsumedCount() >= JKN.vip_cost) {
            jknUser.setVip(VIP);
            upgrade = true;
        }

        update(jknUser);
        return new byte[]{upgrade ? TRUE : FALSE, jknUser.getUserLevel()};
    }

    @Override
    @Transactional
    public void upgradeUserLevel(int userId, byte userLevel, byte areaLevel) {
        JknUser user = load(JknUser.class, userId);
        if (user.getUserLevel() < userLevel){
            user.setUserLevel(userLevel);
        }
        if (user.getAreaLevel() < areaLevel){
            user.setAreaLevel(areaLevel);
        }
        update(user);

        triggerEvent(new JknEvent(EV_USER_NOTIFY, userId, 0));
    }

    @Override
    @Transactional
    public void saveSettlement(OrderSettlement orderSettlement) {
        save(orderSettlement);

        JknUser user;
        if (orderSettlement.getUserIdOne() != 0) {
            user = load(JknUser.class, orderSettlement.getUserIdOne());
            user.setCountOne(user.getCountOne() + orderSettlement.getCountOne());
            update(user);
        }
        if (orderSettlement.getUserIdTwo() != 0) {
            user = load(JknUser.class, orderSettlement.getUserIdTwo());
            user.setCountTwo(user.getCountTwo() + orderSettlement.getCountTwo());
            update(user);
        }
        if (orderSettlement.getUserIdThree() != 0) {
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
    public byte applySettlement(int dbKey) {
        OrderSettlement orderSettlement = load(OrderSettlement.class, dbKey);

        if (orderSettlement.getUserIdOne() != 0) {
            JknUser userOne = load(JknUser.class, orderSettlement.getUserIdOne());
            if (userOne.getCountOne() >= orderSettlement.getCountOne()){
                userOne.setCount(orderSettlement.getCountOne() + userOne.getCount());
                userOne.setCountOne(userOne.getCountOne() - orderSettlement.getCountOne());
                update(userOne);
            }
        }

        if (orderSettlement.getUserIdTwo() != 0) {
            JknUser userTwo = load(JknUser.class, orderSettlement.getUserIdTwo());
            if (userTwo.getCountTwo() >= orderSettlement.getCountTwo()){
                userTwo.setCount(orderSettlement.getCountTwo() + userTwo.getCount());
                userTwo.setCountTwo(userTwo.getCountTwo() - orderSettlement.getCountTwo());
                update(userTwo);
            }
        }

        if (orderSettlement.getUserIdThree() != 0) {
            JknUser userThree = load(JknUser.class, orderSettlement.getUserIdThree());
            if (userThree.getCountThree() >= orderSettlement.getCountThree()){
                userThree.setCount(orderSettlement.getCountThree() + userThree.getCount());
                userThree.setCountThree(userThree.getCountThree() - orderSettlement.getCountThree());
                update(userThree);
            }
        }
        orderSettlement.setSettlementStatus(SS_DONE);
        update(orderSettlement);

        triggerEvent(new JknEvent(EV_USER_NOTIFY_COUNT, orderSettlement.getOrderId(), null));
        return ES_DONE;
    }

    @Override
    @Transactional
    public void triggerSmsEvent(byte eventType, boolean serious) {
        List<JknEvent> jknEvents = getLists("from JknEvent where to_days(triggerDate)=" +
                        "to_days(curdate()) and eventType=:eventType"
                , (query -> query.setInteger("eventType", EV_SMS_SEND)));

        if (jknEvents != null && jknEvents.size() > 5) {
            logger.warn("too much sms event, just ignore this:" + eventType);
            return;
        }

        boolean[] exists = new boolean[]{false};
        FwUtil.safeEach(jknEvents,(event)->{
            if (event.getEventStatus() == ES_INI){
                exists[0] = true;
            }
        });
        if (exists[0]){
            logger.info("there are still sms event not send, ignore this");
            return;
        }
        JknEvent event = new JknEvent(EV_SMS_SEND, eventType, serious ? SERIOUS : NORMAL);
        //trigger sms event 10 mins later
        event.setTriggerDate(new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000));
        triggerEvent(event);
    }

    @Override
    @Transactional
    public void deleteEarlierEvent() {
        int count = jknUserMapper.deleteEarlierEvent();
        logger.info("delete {} rows in earlier events", count);
    }
}
