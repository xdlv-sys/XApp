package xd.fw.service;

import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;

import java.util.List;

/**
 * Created by xd on 2016/7/30.
 */
public interface JknService extends BaseService {

    int saveJknUser(JknUser jknUser);

    int saveOrder(Order order);

    void triggerEvent(JknEvent event);

    void upgradeJknUser(List<JknEvent> eventList);
}
