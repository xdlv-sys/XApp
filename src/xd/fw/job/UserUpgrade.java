package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.service.JknService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserUpgrade implements UserHandler {
    @Autowired
    JknService jknService;

    @Override
    public byte process(JknEvent event, Map<Integer, UserDesc> userMap) {
        JknUser self = jknService.get(JknUser.class, event.getDbKey());
        UserDesc user = userMap.get(event.getDbKey());

        if (user == null) {
            //user upgrade from customer to membership
            user = new UserDesc(self.getUserId(),self.getUserLevel());
            userMap.put(self.getUserId(), user);
            user.parent = userMap.get(self.getReferrer());
        } else {
            //use level improved
            user.userId = self.getUserId();
        }
        //notify user self
        jknService.triggerEvent(new JknEvent(EV_USER_NOTIFY, user.userId, null));

        //make sure three up generation exists
        //upgrade father ...
        List<JknEvent> eventList = new ArrayList<>();
        UserDesc parent = checkParent(user, userMap);
        if (parent != null){
            upgrade(parent, eventList);
            parent = checkParent(parent, userMap);
            if (parent != null){
                upgrade(parent,eventList);
                parent = checkParent(parent,userMap);
                if (parent != null){
                    upgrade(parent,eventList);
                }
            }
        }

        // update user level and trigger notify event, no need to trigger update event, since user in map already been
        // updated in above function upgrade
        jknService.upgradeUsers(eventList);

        return ES_DONE;
    }

    UserDesc checkParent(UserDesc user, Map<Integer, UserDesc> userMap){
        if (user.parent == null){
            //load from db
            JknUser self = jknService.get(JknUser.class,user.userId);
            if (self.getReferrer() != null){
                user.parent = new UserDesc(self.getReferrer());
                userMap.put(self.getReferrer(), user.parent);
                user.parent.addChild(user);
            } else {
                return null;
            }
        }
        return user.parent;
    }

    private void upgrade(UserDesc user, List<JknEvent> eventList) {

        int childrenCount = user.childCount(jknService);
        int allChildrenCount = user.allChildCount(jknService);

        byte shouldLevel = UL_NON;
        if (childrenCount >= JKN.gold_ucn && allChildrenCount >= JKN.gold_acn) {
            shouldLevel = UL_GOLD;
        }
        if (childrenCount >= JKN.white_ucn && allChildrenCount >= JKN.white_acn) {
            shouldLevel = UL_WHITE;
        }
        if (childrenCount >= JKN.diamond_ucn && allChildrenCount >= JKN.diamond_acn) {
            shouldLevel = UL_DIAMOND;
        }
        // TODO　are level implementation
        JknUser dbUser = jknService.get(JknUser.class, user.userId);
        if (dbUser.getUserLevel() < shouldLevel) {
            //upgrade user level and the record in db will be upgraded later
            eventList.add(new JknEvent(EV_USER_NOTIFY, user.userId, shouldLevel));
        }
    }
}
