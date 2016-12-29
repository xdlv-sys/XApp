package xd.dl.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.DlConst;
import xd.dl.bean.*;
import xd.dl.mina.ParkHandler;
import xd.fw.mina.tlv.TLVMessage;

import java.util.ArrayList;
import java.util.List;

@Results({
        @Result(name = "index", location = "../../wwt/charge.jsp")
})
public class ChargeAction extends ParkBaseAction implements DlConst {
    @Autowired
    ParkHandler parkHandler;
    String carNumber;

    List<ChargePark> parks;

    List<Charge> charges;
    Charge charge;

    public String obtainCharges() throws Exception{
        total = parkService.getAllCount(Charge.class, charge);
        charges = parkService.getList(Charge.class, charge, "timeStamp asc", start, limit);
        return SUCCESS;
    }

    public String execute() throws Exception {
        return INDEX;
    }

    public String query() throws Exception{
        //send to all proxy to obtain the car info
        List<TLVMessage> messages = parkHandler.queryCarInfoFromAllProxy(carNumber);
        parks = new ArrayList<>(messages.size());
        String parkId;
        int length;
        ChargePark park;
        ParkInfo parkInfo;
        for (TLVMessage msg : messages){
            parkId = (String)msg.getValue();
            length = (Integer)msg.getNextValue(0);
            if (length < 1){
                continue;
            }
            parkInfo = parkService.get(ParkInfo.class, parkId);
            park = new ChargePark(parkInfo);
            // for the security
            parkInfo.setAliShaRsaKey(null);
            parkInfo.setMchId(null);
            parkInfo.setSecret(null);
            parkInfo.setWxKey(null);

            parks.add(park);

            int index = 1;
            for (int i=0;i<length;i++){
                ViewCarportRoomInfo slot = new ViewCarportRoomInfo();
                park.putSlot(slot);
                slot.sCarportNum = (String)msg.getNextValue(index++);		//车位号
                slot.sRoomNum = (String)msg.getNextValue(index++);			//组号
                slot.sName= (String)msg.getNextValue(index++);				//姓名
                slot.sAddress= (String)msg.getNextValue(index++);			//地址
                slot.sPhoneNumber= (String)msg.getNextValue(index++);		//电话
                slot.sPosition= (String)msg.getNextValue(index++);			//位置
                slot.sStartDate= (String)msg.getNextValue(index++);			//有效起始日期
                slot.sEndDate= (String)msg.getNextValue(index++);			//有效结束日期
                slot.fDeposit= (Float)msg.getNextValue(index++);			//押金
                slot.bTemporary= (Boolean)msg.getNextValue(index++);			//临时
                slot.sRemark= (String)msg.getNextValue(index++);			//备注
                slot.sRentName= (String)msg.getNextValue(index++);		//包月类型
                slot.fRentMoney= (Float)msg.getNextValue(index++);		//包月金额
                slot.sParkName= (String)msg.getNextValue(index++);
            }
        }
        return SUCCESS;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public List<ChargePark> getParks() {
        return parks;
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }
}
