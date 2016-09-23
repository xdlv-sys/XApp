package xd.fw.job;

import xd.fw.bean.JknUser;
import xd.fw.service.IConst;
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
        this.consumedCount = user.getConsumedCount();
        this.areaLevel = user.getAreaLevel();
        this.storeKeeper = user.getStoreKeeper();
    }

    UserDesc (int userId){
        this.userId = userId;
    }

    int userId;
    byte userLevel;
    byte areaLevel;
    byte storeKeeper;
    Integer referrer;
    private int consumedCount;

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
        int[] count = new int[] { 0 } ;
        iterateGenerations((userDesc -> count[0]++));
        return count[0];
    }

    long allConsumed(){
        long[] allConsumed = new long[]{ 0 } ;
        iterateGenerations((userDesc -> allConsumed[0] += userDesc.consumedCount));
        return allConsumed[0];
    }

    int allRegionCount(){
        int[] cityCount = new int[] {0};
        iterateGenerations((userDesc -> {
            if (userDesc.areaLevel == IConst.AL_REGION){
                cityCount[0] ++;
            }
        } ));
        return cityCount[0];
    }

    private void iterateGenerations(IterateGeneration it){
        for (UserDesc son : children) { // son
            it.process(son);
            for (UserDesc gradeSon : son.children) { //gradeSon
                it.process(gradeSon);
                // gradeGradeSon
                gradeSon.children.forEach(it::process);
            }
        }
    }

    interface IterateGeneration{
        void process(UserDesc userDesc);
    }
}