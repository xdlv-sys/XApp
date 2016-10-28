package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.Order;
import xd.fw.bean.OrderSettlement;
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
    List<OrderSettlement> orderSettlements;
    public String obtainOrders(){

        int orderId = order == null ? 0 : order.getOrderId();
        int userId = order == null ? 0 : order.getUserId();
        short tradeType = order == null ? -1 : order.getTradeType();

        total = jknService.getJknOrderCount(orderId, userId,tradeType);
        orders = jknService.getJknOrders(orderId, userId,tradeType, start, limit);
        return SUCCESS;
    }

    public String obtainOrderSettlements(){
        total = jknService.getAllCount(OrderSettlement.class);
        orderSettlements = jknService.getList(OrderSettlement.class,null, start, limit);
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

    public List<OrderSettlement> getOrderSettlements() {
        return orderSettlements;
    }

    public void setOrderSettlements(List<OrderSettlement> orderSettlements) {
        this.orderSettlements = orderSettlements;
    }
}