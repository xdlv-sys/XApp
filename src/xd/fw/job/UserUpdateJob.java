package xd.fw.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;

import javax.annotation.PostConstruct;
import java.util.*;
@Service
public class UserUpdateJob extends EventJob {
    static Map<Integer, UserDesc> userMap = new HashMap<>();

    @Value("${gold_ucn}")
    int goldUcn = 1;
    @Value("${gold_acn}")
    int goldAcn = 0;

    @Value("${white_ucn}")
    int whiteUcn = 10;
    @Value("${white_acn}")
    int whiteAcn = 0;

    @Value("${diamond_ucn}")
    int diamondUcn = 10;
    @Value("${diamond_acn}")
    int diamondAcn = 30;

    @Override
    protected JknEvent processType() {
        return USER_UPDATE;
    }

    @Override
    public byte process(JknEvent event) {
        List<JknEvent> eventList = new ArrayList<>();

        JknUser self = jknService.get(JknUser.class, event.getDbKey());
        UserDesc user = userMap.get(event.getDbKey());
        if (user != null) {
            // user update since user desc already exists
            user.user = self;
            return EV_DONE;
        }
        // new User
        user = new UserDesc(self);
        UserDesc parent = userMap.get(self.getReferrer());
        user.parent = parent;
        user.parent.children.add(user);

        userMap.put(self.getUserId(), user);

        //upgrade user level from parent
        JknEvent upgradeEvent;
        while (parent != null) {
            upgradeEvent = upgrade(user.parent);
            if (upgradeEvent != null) {
                eventList.add(upgradeEvent);
            }
            parent = parent.parent;
        }
        jknService.upgradeJknUser(eventList);
        return EV_DONE;
    }

    private JknEvent upgrade(UserDesc user) {
        Byte userLevel = user.user.getUserLevel();
        int childrenCount = user.children.size();
        int allChildrenCount = user.allChildCount();

        byte shouldLevel = UL_NON;
        if (childrenCount >= goldUcn && allChildrenCount >= goldAcn) {
            shouldLevel = UL_GOLD;
        }
        if (childrenCount >= whiteUcn && allChildrenCount >= whiteAcn) {
            shouldLevel = UL_WHITE;
        }
        if (childrenCount >= diamondUcn && allChildrenCount >= diamondAcn) {
            shouldLevel = UL_DIAMOND;
        }
        // TODO　are level implementation

        if (userLevel < shouldLevel) {
            //upgrade user level and the record in db will be upgraded later
            user.user.setUserLevel(shouldLevel);
            return new JknEvent(EV_USER_UPGRADE, user.user.getUserId(), shouldLevel);
        }
        return null;
    }

    @PostConstruct
    public void buildUserTree() {
        int total = jknService.getAllCount(JknUser.class);
        int start = 0;
        List<JknUser> users;
        UserDesc self, parent;
        while (start < total) {
            users = jknService.getList(JknUser.class, null, start, start += 100);
            for (JknUser user : users) {
                self = userMap.get(user.getUserId());
                if (self == null) {
                    self = new UserDesc(user);
                    userMap.put(user.getUserId(), self);
                } else {
                    self.user = user;
                }
                if (user.getReferrer() != null) {
                    parent = userMap.get(user.getReferrer());
                    if (parent == null) {
                        parent = new UserDesc(null);
                        userMap.put(user.getReferrer(), parent);
                    }
                    self.parent = parent;
                    parent.children.add(self);
                }
            }
        }
    }

    static class UserDesc {
        UserDesc(JknUser user) {
            this.user = user;
        }

        JknUser user;
        UserDesc parent;
        List<UserDesc> children = new LinkedList<>();

        int allChildCount() {
            int count = children.size();
            for (UserDesc son : children) { // son
                count += son.children.size();
                for (UserDesc gradeSon : son.children) { //gradeSon
                    count += gradeSon.children.size();
                    for (UserDesc gradeGradeSon : gradeSon.children) { // gradeGradeSon
                        count += gradeGradeSon.children.size();
                    }
                }
            }
            return count;
        }
    }

}
