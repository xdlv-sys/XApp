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
            user = new UserDesc(self.getUserId());
            user.parent = userMap.get(self.getReferrer());
            user.parent.children.add(user);
            userMap.put(self.getUserId(), user);
        } else {
            user.userId = self.getUserId();
            //other properties..
        }

        //upgrade father ...
        List<JknEvent> eventList = new ArrayList<>();
        upgrade(user.parent, eventList);
        upgrade(user.parent.parent, eventList);
        upgrade(user.parent.parent.parent, eventList);

        // update user level and trigger notify event, no need to trigger update event, since user in map already been
        // updated in above function upgrade
        jknService.upgradeUsers(eventList);
        //notify user self
        jknService.triggerEvent(new JknEvent(EV_USER_NOTIFY, user.userId, null));

        return ES_DONE;
    }

    private void upgrade(UserDesc user, List<JknEvent> eventList) {

        int childrenCount = user.children.size();
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
        }
    }
}
