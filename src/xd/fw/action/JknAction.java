package xd.fw.action;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.service.JknService;

public class JknAction extends BaseAction{
    @Autowired
    JknService jknService;

    JknUser jknUser;
    Order order;

    int code = 200;

    @Action("syncUser")
    public String syncUser(){
        if (jknUser.getUserId() == 0 || jknUser.getUserName() == null){
            code = 201;
            return SUCCESS;
        }
        code = jknService.saveJknUser(jknUser);
        jknUser = null;
        return SUCCESS;
    }

    @Action("addTrade")
    public String addTrade(){
        int totalFee = order.getTotalFee();
        if (totalFee < 0) {
            code = 301;
            return SUCCESS;
        }
        int balance = order.getBalanceFee();
        if (balance < 0) {
            code = 302;
            return SUCCESS;
        }
        code = jknService.saveOrder(order);
        order = null;
        return SUCCESS;
    }

    public JknUser getJknUser() {
        return jknUser;
    }

    public void setJknUser(JknUser jknUser) {
        this.jknUser = jknUser;
    }

    public int getCode() {
        return code;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
