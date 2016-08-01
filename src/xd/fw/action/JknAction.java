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
        if (jknUser.getUserId() == null || jknUser.getUserName() == null){
            code = 201;
            return SUCCESS;
        }
        jknService.saveJknUser(jknUser);
        return SUCCESS;
    }

    @Action("addTrade")
    public String addTrade(){
        code = jknService.saveOrder(order);
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
