package xd.fw.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.FwUtil;
import xd.fw.bean.JknUser;
import xd.fw.bean.Order;
import xd.fw.bean.OrderSettlement;
import xd.fw.service.JknService;
import xd.fw.service.SetParameters;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JknAction extends BaseAction {
    @Value("${md5_key}")
    String md5Key;
    @Autowired
    JknService jknService;

    JknUser jknUser;
    Order order;
    String sign;

    int code = 200;

    @Override
    public void validate() {
        Map params = ServletActionContext.getRequest().getParameterMap();
        if (!FwUtil.verify(params, md5Key)) {
            addFieldError("sign", "sign error");
            ServletActionContext.getRequest().setAttribute("code", 101);
        }
    }

    @Action("syncUser")
    public String syncUser() {
        if (jknUser.getUserId() == 0 || jknUser.getUserName() == null) {
            code = 201;
            return SUCCESS;
        }
        code = jknService.saveJknUser(jknUser);
        jknUser = null;
        return SUCCESS;
    }

    @Action("addTrade")
    public String addTrade() {
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

    List<OrderSettlement> orderSettlements;
    @Action("settlementDetail")
    public String settlementDetail(){
        orderSettlements = jknService.getLists("from OrderSettlement " +
                "where userIdOne=:userId or userIdTwo=:userId or userIdThree=:userId", new SetParameters() {
            @Override
            public void process(Query query) {
                query.setInteger("userId", jknUser.getUserId());
                query.setFirstResult(start);
                query.setMaxResults(limit);
            }
        });
        return SUCCESS;
    }

    List<JknUser> sons, grandSons, grandGrandSons;

    public List<JknUser> getSons() {
        return sons;
    }

    public List<JknUser> getGrandSons() {
        return grandSons;
    }

    public List<JknUser> getGrandGrandSons() {
        return grandGrandSons;
    }

    @Action("userDetail")
    public String userDetail() {
        if (jknUser.getUserId() == 0) {
            code = 201;
            return SUCCESS;
        }
        sons = querySons(jknUser.getUserId());

        /*grandSons = new LinkedList<>();
        FwUtil.safeEach(sons, new FwUtil.SafeEachProcess<JknUser>() {
            @Override
            public void process(JknUser user) {
                grandSons.addAll(querySons(user.getUserId()));
            }
        });

        grandGrandSons = new LinkedList<>();
        FwUtil.safeEach(grandSons, new FwUtil.SafeEachProcess<JknUser>() {
            @Override
            public void process(JknUser user) {
                grandGrandSons.addAll(querySons(user.getUserId()));
            }
        });*/

        return SUCCESS;
    }

    List<JknUser> querySons(Integer userId){
        return jknService.getLists("from JknUser where referrer=:userId", new SetParameters() {
            @Override
            public void process(Query query) {
                query.setInteger("userId", userId);
                query.setFirstResult(start);
                query.setMaxResults(limit);
            }
        });
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

    public void setSign(String sign) {
        this.sign = sign;
    }
}
