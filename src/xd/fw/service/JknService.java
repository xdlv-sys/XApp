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

    void upgradeUsers(List<JknEvent> eventList);

    List<JknUser> getMemberUser(int start, int limit);

    //void saveUserCount(JknUser user);

    void saveSettlement(OrderSettlement orderSettlement);

    List<JknEvent> getTriggeringEvent(byte[] eventType, int start, int limit);

    byte applySettlement(Integer dbKey);

    void modifyUserCount(int userId, int count, int countOne, int countTwo, int countThree);

    void updateUserProsForProcessOrder(JknUser modifyUser);
}
