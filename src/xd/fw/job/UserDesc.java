package xd.fw.job;

import xd.fw.bean.JknUser;
import xd.fw.service.JknService;

import java.util.LinkedList;
import java.util.List;

class UserDesc {

    static UserDesc create(JknUser user){
        UserDesc userDesc = new UserDesc(user.getUserId());
        userDesc.fill(user);
        return userDesc;
    }
    void fill(JknUser user){
        this.userId = user.getUserId();
        this.userLevel = user.getUserLevel();
        this.referrer = user.getReferrer();
    }

    UserDesc (int userId){
        this.userId = userId;
    }

    int userId;
    byte userLevel;
    Integer referrer;

    UserDesc parent;
    List<UserDesc> children = new LinkedList<>();
    void addChild(UserDesc child){
        for (UserDesc c : children){
            if (c.userId == child.userId){
                return;
            }
        }
        children.add(child);
    }

    int childCount(){
        int count = 0;
        for (UserDesc userDesc : children){
            if (userDesc.userLevel > JknService.UL_NON){
                count++;
            }
        }
        return count;
    }

    int allChildCount() {
        int count = childCount();
        for (UserDesc son : children) { // son
            count += son.childCount();
            for (UserDesc gradeSon : son.children) { //gradeSon
                count += gradeSon.childCount();
                for (UserDesc gradeGradeSon : gradeSon.children) { // gradeGradeSon
                    count += gradeGradeSon.childCount();
                }
            }
        }
        return count;
    }
}