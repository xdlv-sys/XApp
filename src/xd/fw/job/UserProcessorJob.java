package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.service.IConst;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserProcessorJob extends EventJob {
    private static Map<Integer, UserDesc> userMap = new HashMap<>();

    private static UserHandler[] userHandlers = new UserHandler[50];

    @Autowired
    UserSettlementApply userSettlementApply;
    @Autowired
    UserSettlement userSettlement;
    @Autowired
    UserUpgrade userUpgrade;
    @Autowired
    UserUpdate userUpdate;
    @Autowired
    UserProcessOrder userProcessOrder;

    @Override
    protected byte process(JknEvent event) {
        return userHandlers[event.getEventType()].process(event,userMap);
    }

    @Override
    protected byte[] processType() {
        return new byte[]{EV_USER_PROCESS_ORDER, EV_USER_UPGRADE, EV_USER_SETTLEMENT
                , EV_USER_SETTLEMENT_APPLY};
    }

    @PostConstruct
    public void init(){
        userHandlers[EV_USER_PROCESS_ORDER] = userProcessOrder;
        //userHandlers[EV_USER_UPDATE] = userUpdate;
        userHandlers[EV_USER_UPGRADE] = userUpgrade;
        userHandlers[EV_USER_SETTLEMENT] = userSettlement;
        userHandlers[EV_USER_SETTLEMENT_APPLY] = userSettlementApply;

        buildUserTree();
    }

    private void buildUserTree() {
        int start = 0;
        List<JknUser> users;
        UserDesc self, parent;

        while ((users = jknService.getMemberUser(start, start += 100)).size() > 0) {
            for (JknUser user : users) {
                self = userMap.get(user.getUserId());
                if (self == null) {
                    self = new UserDesc(user.getUserId());
                    userMap.put(user.getUserId(), self);
                } else {
                    self.userId = user.getUserId();
                }
                if (user.getReferrer() != null) {
                    parent = userMap.get(user.getReferrer());
                    if (parent == null) {
                        parent = new UserDesc(INVALIDATE_INT);
                        userMap.put(user.getReferrer(), parent);
                    }
                    self.parent = parent;
                    parent.children.add(self);
                }
            }
        }
    }
}
