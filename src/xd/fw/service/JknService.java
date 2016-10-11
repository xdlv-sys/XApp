package xd.fw.service;

import xd.fw.bean.*;

import java.util.List;

/**
 * Created by xd on 2016/7/30.
 */
public interface JknService extends BaseService {

    int saveJknUser(JknUser jknUser);

    int saveOrder(Order order);

    void triggerEvent(JknEvent event);

    void upgradeUserLevel(int userId, byte userLevel, byte areaLevel);

    List<JknUser> getMemberUser(int start, int limit);

    //void saveUserCount(JknUser user);

    void saveSettlement(OrderSettlement orderSettlement);

    List<JknEvent> getTriggeringEvent(byte[] eventType, int start, int limit);

    byte applySettlement(int dbKey);

    void userWithdrawCount(int userId, int count);

    byte[] updateUserProsForProcessOrder(Order order);

    void triggerSmsEvent(byte eventType, boolean serious);

    void deleteEarlierEvent();

    List<JknUser> getJknUsers(int userId,String userName, String referrerName, int start, int limit);

    int getJknUserCount(int userId,String userName, String referrerName);

    List<Order> getJknOrders(int orderId, int userId, short tradeType, int start, int limit);

    int getJknOrderCount(int orderId, int userId, short tradeType);

    void updateApproveStore(JknStoreApprove storeApprove);

}
