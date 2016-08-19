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
            user = UserDesc.create(self);
            userMap.put(self.getUserId(), user);
            user.parent = userMap.get(self.getReferrer());
            if (user.parent != null){
                user.parent.addChild(user);
            }
        } else {
            //use level improved
            user.fill(self);
        }
        //notify user self
        jknService.triggerEvent(new JknEvent(EV_USER_NOTIFY, user.userId, null));

        //make sure three up generation exists
        //upgrade father ...
        List<JknEvent> eventList = new ArrayList<>();

        UserDesc parent = checkParent(user, userMap);
        if (parent != null){
            upgrade(parent, eventList,userMap);
            parent = checkParent(parent, userMap);
            if (parent != null){
                upgrade(parent,eventList,userMap);
                parent = checkParent(parent,userMap);
                if (parent != null){
                    upgrade(parent,eventList,userMap);
                }
            }
        }

        // update user level and trigger notify event
        jknService.upgradeUsers(eventList);

        return ES_DONE;
    }

    UserDesc checkParent(UserDesc user, Map<Integer, UserDesc> userMap){
        if (user.referrer == null){
            return null;
        }

        if (user.parent == null){
            //try to load in user map
            user.parent = userMap.get(user.referrer);
        }

        if (user.parent == null || user.parent.userId == INVALIDATE_INT){
            JknUser parent = jknService.get(JknUser.class, user.referrer);
            if (user.parent == null){
                user.parent = UserDesc.create(parent);
                userMap.put(user.parent.userId, user.parent);
            } else {
                user.parent.fill(parent);
            }
        }

        user.parent.addChild(user);

        return user.parent;
    }

    private void upgrade(UserDesc user, List<JknEvent> eventList, Map<Integer, UserDesc> userMap) {
        if (user.userLevel < UL_NORMAL){
            return;
        }

        int childrenCount = user.childCount();
        int allChildrenCount = user.allChildCount();

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
            userMap.get(user.userId).userLevel = (byte)shouldLevel;
        }
    }
}
