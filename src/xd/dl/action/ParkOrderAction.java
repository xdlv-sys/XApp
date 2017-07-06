package xd.dl.action;

import org.apache.struts2.convention.annotation.Action;
import xd.dl.job.IDongHui;
import xd.dl.job.ParkNative;
import xd.dl.job.ParkedCarInfo;
@Action("/dtkServer/parkOrder")
public class ParkOrderAction extends ParkOrderBaseAction implements IDongHui {
    //Logger logger = LoggerFactory.getLogger(ParkOrderAction.class);
    String orderNo, carNo;
    @Action("getParkOrder")
    public String getParkOrder(){
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

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
