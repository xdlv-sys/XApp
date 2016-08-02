package xd.fw.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected Byte[] processType() {
        return new Byte[]{EV_USER_UPDATE, EV_USER_UPGRADE,EV_USER_SETTLEMENT };
    }

    @Override
    public Byte process(JknEvent eventType) {
        JknUser self = jknService.get(JknUser.class, eventType.getDbKey());
        UserDesc user = userMap.get(eventType.getDbKey());

        // sync user info between map and db
        // user properties (telephone, email account etc)update since user desc already exists
        user.user = self;

        if (eventType.getEventType() == EV_USER_UPDATE.byteValue()){
            // just return for update already be done
            return EV_DONE;
        }
        //user upgrade
        if (eventType.getEventType() == EV_USER_UPGRADE){

        }

        List<JknEvent> eventList = new ArrayList<>();

        JknUser self = jknService.get(JknUser.class, eventType.getDbKey());
        UserDesc user = userMap.get(eventType.getDbKey());
        if (user != null) {
            // user properties (telephone, email etc)update since user desc already exists
            user.user = self;
            return EV_DONE;
        }
        // new User
        user = new UserDesc(self);
        UserDesc parent = userMap.get(self.getReferrer());
        if (parent == null) {
            //should never happen
            return EV_FAIL;
        }
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
        int start = 0;
        List<JknUser> users;
        UserDesc self, parent;

        while ((users = jknService.getMemberUser(start, start += 100)).size() > 0) {
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

    /*static UserDesc getUserDesc(Integer userId){
        synchronized (userMap){
            return userMap.get(userId);
        }
    }
    static UserDesc setUserDesc(Integer userId, UserDesc userDesc){
        synchronized (userMap){
            return userMap.put(userId, userDesc);
        }
    }*/
}
