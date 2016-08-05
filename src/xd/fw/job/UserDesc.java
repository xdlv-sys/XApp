package xd.fw.job;

import xd.fw.bean.JknUser;

import java.util.LinkedList;
import java.util.List;

class UserDesc {
    UserDesc(int userId) {
        this.userId = userId;
    }

    int userId;
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