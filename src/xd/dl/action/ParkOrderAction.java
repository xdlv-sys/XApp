package xd.dl.action;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xd.dl.job.IDongHui;
import xd.dl.job.ParkNative;
import xd.dl.job.ParkedCarInfo;
@Action("/dtkServer/parkOrder")
public class ParkOrderAction extends DLBaseAction implements IDongHui {
    String orderNo;
    @Action("getParkOrder")
    public String getParkOrder(){
        logger.info("start to ParkNative.getParkedCarInfo");
        ParkedCarInfo carInfo = ParkNative.getParkedCarInfo(orderNo, 0, carNo, 0, 0, 0, "", "");
        if (carInfo == null){
            state = "1012";
            msg = fail;
        } else {
            put("orderNo", orderNo);
            put("payFee", carInfo.fMoney * 100);
            put("payStartTime", carInfo.sStartTime);
            put("payEndTime", carInfo.sEndTime);
        }
        return SUCCESS;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
