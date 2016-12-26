package xd.dl.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by exiglvv on 12/26/2016.
 */
public class ChargePark {

    List<ViewCarportRoomInfo> slots;
    ParkInfo parkInfo;

    public ChargePark(ParkInfo parkInfo){
        this.parkInfo = parkInfo;
    }

    public void putSlot(ViewCarportRoomInfo slot){
        if (slots == null){
            slots = new ArrayList<>();
        }
        slots.add(slot);
    }

    public List<ViewCarportRoomInfo> getSlots() {
        return slots;
    }

    public void setSlots(List<ViewCarportRoomInfo> slots) {
        this.slots = slots;
    }

    public ParkInfo getParkInfo() {
        return parkInfo;
    }

    public void setParkInfo(ParkInfo parkInfo) {
        this.parkInfo = parkInfo;
    }
}
