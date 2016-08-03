package xd.fw.service;

import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.bean.OrderSettlement;

import java.util.List;

/**
 * Created by xd on 2016/7/30.
 */
public interface JknService extends BaseService {

    int saveJknUser(JknUser jknUser);

    int saveOrder(Order order);

    void triggerEvent(JknEvent event);

    void upgradeJknUser(List<JknEvent> eventList);

    List<JknUser> getMemberUser(int start, int limit);

    //void saveUserCount(JknUser user);

    void saveSettlement(OrderSettlement orderSettlement, List<JknUser> impactedUsers);

    List<JknEvent> getTriggeringEvent(Byte[] eventType, int start, int limit);

    Byte applySettlement(Integer dbKey);

    void updateJknUser(JknUser user);
}
