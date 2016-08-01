package xd.fw.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.service.JknService;

import java.util.Calendar;

@Service
public class JknServiceImpl extends HibernateServiceImpl implements JknService {
    @Override
    @Transactional
    public void saveJknUser(JknUser jknUser) {
        JknUser record = get(JknUser.class,jknUser.getUserId());
        if (record != null){
            merge(jknUser);
        } else {
            //default value
            jknUser.setVip(NON_VIP);
            jknUser.setUserLevel(UL_NON);
            jknUser.setAreaLevel(AL_NON);
            jknUser.setCount(0);
            jknUser.setCountOne(0);
            jknUser.setCountTwo(0);
            jknUser.setCountThree(0);
            save(jknUser);
        }
        triggerEvent(new JknEvent(EV_USER_UPDATE,jknUser.getUserId(),null));
    }

    @Override
    @Transactional
    public int saveOrder(Order order) {
        if (get(Order.class, order.getOrderId()) != null){
            return 201;
        }
        order.setTradeStatus(TR_PAY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getLastDate());
        order.setYear((short)calendar.get(Calendar.YEAR));
        order.setMonth((byte)calendar.get(Calendar.MONTH));
        save(order);

        triggerEvent(new JknEvent(EV_ADD_ORDER,order.getOrderId(),null));
        return 200;
    }

    @Override
    public void triggerEvent(JknEvent event) {
        event.setEventStatus(EV_INI);
        save(event);
    }
}
