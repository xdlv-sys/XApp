package xd.dl.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import xd.dl.DlConst;
import xd.dl.bean.GroupItem;
import xd.dl.bean.ParkGroup;
import xd.dl.bean.ParkInfo;
import xd.dl.mina.ParkHandler;
import xd.fw.bean.User;

import java.util.List;

public class WhiteAction extends ParkBaseAction implements DlConst {
    @Autowired
    ParkHandler parkHandler;

    List<GroupItem> whites;
    GroupItem white;
    ParkGroup parkGroup;
    List<ParkGroup> groups;

    public String obtainGroups() throws Exception{
        User user = currentUser();
        if ( StringUtils.isNotBlank(user.getAddition())){
            if (parkGroup == null){
                parkGroup = new ParkGroup();
                parkGroup.setParkId(user.getAddition());
            }
        }
        total = parkService.getAllCount(ParkGroup.class, parkGroup);
        groups = parkService.getList(ParkGroup.class, parkGroup, null, start, limit);
        return SUCCESS;
    }

    public String obtainWhites() throws Exception{
        User user = currentUser();
        if (white == null){
            white = new GroupItem();
        }

        if ( StringUtils.isNotBlank(user.getAddition())){
            //set first group to retrieve white lists
            ParkGroup parkGroup = new ParkGroup();
            parkGroup.setParkId(user.getAddition());
            List<ParkGroup> parkGroupList = parkService.getList(ParkGroup.class, parkGroup, null, -1, -1);
            if (parkGroupList != null && parkGroupList.size() > 0){
                white.setGroupId(parkGroupList.get(0).getId());
            }
        }
        total = parkService.getAllCount(GroupItem.class, white);
        whites = parkService.getList(GroupItem.class, white, null, start, limit);
        return SUCCESS;
    }

    public void setWhite(GroupItem white) {
        this.white = white;
    }

    public List<GroupItem> getWhites() {
        return whites;
    }

    public void setParkGroup(ParkGroup parkGroup) {
        this.parkGroup = parkGroup;
    }

    public List<ParkGroup> getGroups() {
        return groups;
    }
}
