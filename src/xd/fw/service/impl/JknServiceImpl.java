package xd.fw.service.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.FwUtil;
import xd.fw.JKN;
import xd.fw.bean.*;
import xd.fw.bean.mapper.JknUserMapper;
import xd.fw.service.JknService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public void userWithdrawCount(int userId, int totalCount) {
        //用户提现：若totalCount>0 ，则为提现，否则撤销提现
        jknUserMapper.userWithdrawCount(userId, totalCount);
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

        //如果是vip，返现20%
        if (jknUser.getVip() == VIP){
            int disCount = order.getTotalFee() * (100 - JKN.vip_discount) /100;
            jknUser.addCount(disCount);
        }
        update(jknUser);
        return new byte[]{upgrade ? TRUE : FALSE, jknUser.getUserLevel()};
    }

    @Override
    @Transactional
    public void upgradeUserLevel(int userId, byte userLevel, byte areaLevel) {
        JknUser user = load(JknUser.class, userId);
        if (user.getUserLevel() < userLevel) {
            user.setUserLevel(userLevel);
        }
        if (user.getAreaLevel() < areaLevel) {
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
        if (orderSettlement.getStoreUserId() > 0){
            user = load(JknUser.class, orderSettlement.getStoreUserId());
            user.setStoreCount(user.getStoreCount() + orderSettlement.getStoreCount());
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
            if (userOne.getCountOne() >= orderSettlement.getCountOne()) {
                userOne.addCount(orderSettlement.getCountOne());
                userOne.setCountOne(userOne.getCountOne() - orderSettlement.getCountOne());
                update(userOne);
            }
        }

        if (orderSettlement.getUserIdTwo() != 0) {
            JknUser userTwo = load(JknUser.class, orderSettlement.getUserIdTwo());
            if (userTwo.getCountTwo() >= orderSettlement.getCountTwo()) {
                userTwo.addCount(orderSettlement.getCountTwo());
                userTwo.setCountTwo(userTwo.getCountTwo() - orderSettlement.getCountTwo());
                update(userTwo);
            }
        }

        if (orderSettlement.getUserIdThree() != 0) {
            JknUser userThree = load(JknUser.class, orderSettlement.getUserIdThree());
            if (userThree.getCountThree() >= orderSettlement.getCountThree()) {
                userThree.addCount(orderSettlement.getCountThree());
                userThree.setCountThree(userThree.getCountThree() - orderSettlement.getCountThree());
                update(userThree);
            }
        }

        if (orderSettlement.getStoreUserId() > 0) {
            JknUser userStore = load(JknUser.class, orderSettlement.getStoreUserId());
            if (userStore.getStoreCount() >= orderSettlement.getStoreCount()) {
                userStore.addCount(orderSettlement.getStoreCount());
                userStore.setStoreCount(userStore.getStoreCount() - orderSettlement.getStoreCount());
                update(userStore);
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
        FwUtil.safeEach(jknEvents, (event) -> {
            if (event.getEventStatus() == ES_INI) {
                exists[0] = true;
            }
        });
        if (exists[0]) {
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

    private String constructJknUserSql(int userId, String userName, String referrerName, boolean count) {
        StringBuilder sql = new StringBuilder(count ? "select count(*) " : "select a.* "
        ).append("from t_jkn_user a ");
        if (StringUtils.isNotBlank(referrerName)) {
            sql.append("join t_jkn_user b on a.referrer=b.user_id where b.user_name like '%"
            ).append(referrerName).append("%'");
        } else {
            sql.append("where 1=1");
        }
        if (userId > 0) {
            sql.append(" and a.user_id=").append(userId);
        }
        if (StringUtils.isNotBlank(userName)) {
            sql.append(" and a.user_name like '%").append(userName).append("%'");
        }
        return sql.toString();
    }

    public List<JknUser> getJknUsers(int userId, String userName, String referrerName, int start, int limit) {
        return htpl.execute((HibernateCallback<List<JknUser>>) session -> {
            String sql = constructJknUserSql(userId, userName, referrerName, false);
            SQLQuery query = session.createSQLQuery(sql).addEntity(JknUser.class);
            return query.setFirstResult(start).setMaxResults(limit).list();
        });
    }

    public int getJknUserCount(int userId, String userName, String referrerName) {
        return htpl.execute(session -> {
            String sql = constructJknUserSql(userId, userName, referrerName, true);
            SQLQuery query = session.createSQLQuery(sql);
            return ((BigInteger) query.list().get(0)).intValue();
        });
    }

    public List<Order> getJknOrders(int orderId, int userId, short tradeType, int start, int limit) {
        return htpl.execute((HibernateCallback<List<Order>>) session -> session.createQuery(constructOrderSql(orderId, userId, tradeType, false)
        ).setFirstResult(start).setMaxResults(limit).list());
    }

    public int getJknOrderCount(int orderId, int userId, short tradeType) {
        return htpl.execute(session -> {
            String sql = constructOrderSql(orderId, userId, tradeType, true);
            return ((Long) session.createQuery(sql).list().get(0)).intValue();
        });
    }

    private String constructOrderSql(int orderId, int userId, short tradeType, boolean count) {
        StringBuilder sql = new StringBuilder(count ? "select count(*) " : "").append("from Order where 1=1");
        if (orderId > 0) {
            sql.append(" and orderId=").append(orderId);
        }
        if (userId > 0) {
            sql.append(" and userId=").append(userId);
        }
        if (tradeType > -1) {
            sql.append(" and tradeType=").append(tradeType);
        }
        return sql.toString();
    }

    @Override
    public void updateApproveStore(JknStoreApprove storeApprove) {
        JknStoreApprove record = load(JknStoreApprove.class, storeApprove.getApproveId());
        record.setApproveStatus(storeApprove.getApproveStatus());
        record.setApproveDate(new Timestamp(System.currentTimeMillis()));
        update(record);

        if (storeApprove.getApproveStatus() == STATUS_DONE){
            //notify user
            JknUser jknUser = load(JknUser.class, record.getUserId());
            jknUser.setStoreKeeper(record.getApproveType());
            update(jknUser);
            //
        }
    }

    @Override
    public Map<Short, Long> sumFee() {
        Map<Short, Long> retMap = new HashMap<>();
        List lists = (List) htpl.execute(session -> {
            Query query = session.createSQLQuery("select b.year,sum(b.total_fee) " +
                    "from (select * from t_jkn_order where trade_type=0) " +
                    "b GROUP BY b.year");
            return query.list();
        });
        FwUtil.safeEach(lists, l->{
            Object[] tmp = (Object[]) l;
            retMap.put((Short)tmp[0], ((BigDecimal)tmp[1]).longValue());
        });
        return retMap;
    }
}
