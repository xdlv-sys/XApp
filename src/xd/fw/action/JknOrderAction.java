package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.Order;
import xd.fw.service.JknService;

import java.util.List;

/**
 * Created by xd on 2016/7/31.
 */
public class JknOrderAction extends BaseAction {
    @Autowired
    JknService jknService;

    Order order;
    List<Order> orders;
    public String obtainOrders(){
        total = jknService.getAllCount(Order.class);
        orders = jknService.getList(Order.class,null, start, limit);
        return SUCCESS;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
