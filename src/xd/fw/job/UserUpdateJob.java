package xd.fw.job;

import xd.fw.FwUtil;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserUpdateJob extends EventJob {
    static Map<Integer, UserDesc> userMap = new HashMap<>();
    @Override
    public void doExecute() throws Exception {
        List<JknEvent> events = getEvent(USER_UPDATE);
        if (events == null || events.size() < 1){
            return;
        }
        FwUtil.safeEach(events,(event -> {

        }));
    }

    @PostConstruct
    public void buildUserTree(){
        int total = jknService.getAllCount(JknUser.class);
        int start = 0;
        List<JknUser> users;
        UserDesc self, parent;
        while (start < total){
            users = jknService.getList(JknUser.class,null,start, start + 100);
            for (JknUser user : users){
                self = new UserDesc(user);
                userMap.put(user.getUserId(), self);
                if (user.getReferrer() != null){
                    parent = userMap.get(user.getReferrer());
                    if (parent == null){
                        //userMap.put
                    }
                }
            }
        }

    }

    static class UserDesc{
        UserDesc(JknUser user){
            this.user = user;
        }
        JknUser user;
        UserDesc parent;
        List<UserDesc> children = new LinkedList<>();
    }

}
