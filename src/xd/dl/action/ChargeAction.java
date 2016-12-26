package xd.dl.action;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.DlConst;
import xd.dl.bean.ChargePark;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.ViewCarportRoomInfo;
import xd.dl.mina.ParkHandler;
import xd.dl.service.ParkService;
import xd.fw.FwUtil;
import xd.fw.mina.tlv.TLVMessage;
import xd.fw.service.FwService;

import java.util.ArrayList;
import java.util.List;


public class ChargeAction extends ParkBaseAction implements DlConst {
    @Value("${charge_redirect_url}")
    String redirectUrl;

    @Autowired
    ParkHandler parkHandler;
    String carNumber;

    List<ChargePark> parks;

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

            for (int i=0;i<length;i++){

            }

        }
        return SUCCESS;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public List<ChargePark> getParks() {
        return parks;
    }
}
