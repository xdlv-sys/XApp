package xd.fw.job;

import xd.fw.JKN;
import xd.fw.bean.JknUser;
import xd.fw.service.JknService;

import java.util.LinkedList;
import java.util.List;

class UserDesc {
    UserDesc(int userId) {
        this.userId = userId;
    }

    int userId;
    UserDesc parent;
    private List<UserDesc> children = new LinkedList<>();
    void addChild(UserDesc child){
        children.add(child);
    }

    int childCount(JknService service){
        int count = 0;
        for (UserDesc userDesc : children){
            if (service.get(JknUser.class,userDesc.userId).getUserLevel() > JknService.UL_NON){
                count++;
            }
        }
        return count;
    }

    int allChildCount(JknService service) {
        int count = childCount(service);
        for (UserDesc son : children) { // son
            count += son.childCount(service);
            for (UserDesc gradeSon : son.children) { //gradeSon
                count += gradeSon.childCount(service);
                for (UserDesc gradeGradeSon : gradeSon.children) { // gradeGradeSon
                    count += gradeGradeSon.childCount(service);
                }
            }
        }
        return count;
    }
}