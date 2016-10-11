package xd.fw.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate4.HibernateTemplate;
import xd.fw.FwUtil;
import xd.fw.JKN;
import xd.fw.bean.*;
import xd.fw.bean.mapper.JknUserMapper;
import xd.fw.service.ConstructHql;
import xd.fw.service.JknService;
import xd.fw.service.SessionProcessor;
import xd.fw.service.SetParameters;

import java.util.List;
import java.util.Map;

public class JknAction extends BaseAction {
    @Value("${md5_key}")
    String md5Key;
    @Autowired
    JknService jknService;
    @Autowired
    JknUserMapper jknUserMapper;

    JknUser jknUser;
    Order order;
    String sign;

    int code = 200;

    UserCount userCount;

    @Override
    public void validate() {
        Map params = ServletActionContext.getRequest().getParameterMap();
        if (!FwUtil.verify(params, md5Key)) {
            addFieldError("sign", "sign error");
            ServletActionContext.getRequest().setAttribute("code", 101);
        }
        if (JKN.appUpdating){
            addFieldError("update", "the system is updating");
            ServletActionContext.getRequest().setAttribute("code", 102);
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

    @Action("approveStore")
    public String approveStore() {
        code = jknService.runInSession(new SessionProcessor<Integer>() {
            @Override
            public Integer process(HibernateTemplate htpl) {
                JknUser record = htpl.load(JknUser.class, jknUser.getUserId());
                record.setStoreKeeper(jknUser.getStoreKeeper());
                htpl.update(record);
                jknService.triggerEvent(
                        new JknEvent(EV_USER_UPGRADE,record.getUserId(),null));
                return 200;
            }
        });
        return SUCCESS;
    }

    Integer withdrawCount;
    @Action("withdrawCount")
    public String withdrawCount(){
        jknUser = jknService.get(JknUser.class, jknUser.getUserId());
        if (jknUser == null){
            code = 201;
        } else {
            withdrawCount = (int)((jknUser.getAllCount() - jknUser.getWithdrawCount()) * JKN.withdraw_percent);
        }
        jknUser = null;
        return SUCCESS;
    }

    List<OrderSettlement> orderSettlements;

    public List<OrderSettlement> getOrderSettlements() {
        return orderSettlements;
    }

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

    @Action("userGeneration")
    public String userGeneration() {
        if (jknUser.getUserId() == 0) {
            code = 201;
            return SUCCESS;
        }
        sons = querySons(jknUser.getUserId(), start, limit);
        return SUCCESS;
    }

    public UserCount getUserCount() {
        return userCount;
    }

    @Action("userGenerationCount")
    public String userGenerationCount() {
        if (jknUser.getUserId() == 0) {
            code = 201;
            return SUCCESS;
        }

        jknUser = jknService.get(JknUser.class, jknUser.getUserId());
        if (jknUser == null){
            code = 202;
            return SUCCESS;
        }
        userCount = new UserCount();
        userCount.setGenerationOne(querySonsCount(jknUser));

        FwUtil.safeEach(querySons(jknUser.getUserId(), 0, userCount.getGenerationOne()), new FwUtil.SafeEachProcess<JknUser>() {
            @Override
            public void process(JknUser son) {
                userCount.setGenerationTwo(userCount.getGenerationTwo() + querySonsCount(son));
                FwUtil.safeEach(querySons(son.getUserId(), 0, -1), new FwUtil.SafeEachProcess<JknUser>() {
                    @Override
                    public void process(JknUser grandSon) {
                        userCount.setGenerationThree(userCount.getGenerationThree() + querySonsCount(grandSon));
                    }
                });
            }
        });

        Integer sumOne = jknUserMapper.sumFeeFromGeneration(1, jknUser.getUserId());

        Integer sumTwo = jknUserMapper.sumFeeFromGeneration(2, jknUser.getUserId());;

        Integer sumThree = jknUserMapper.sumFeeFromGeneration(3, jknUser.getUserId());;;

        userCount.setSumOne(sumOne == null ? 0 : sumOne);
        userCount.setSumTwo(sumTwo == null ? 0 : sumTwo);
        userCount.setSumThree(sumThree == null ? 0 : sumThree);

        return SUCCESS;
    }

    private int querySonsCount(JknUser user){
        return jknService.getAllCount(user, new SetParameters() {
            @Override
            public void process(Query query) {
                query.setInteger("userId", user.getUserId());
            }
        }, new ConstructHql<JknUser>() {
            @Override
            public String process(JknUser user) {
                return "from JknUser where referrer=:userId ";
            }
        });
    }

    List<JknUser> querySons(Integer userId, int start, int limit){
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

    public Integer getWithdrawCount() {
        return withdrawCount;
    }
}
