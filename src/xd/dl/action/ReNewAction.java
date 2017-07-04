package xd.dl.action;

import org.apache.struts2.convention.annotation.Action;
import xd.dl.job.ParkNative;
import xd.dl.job.ViewCarportRoomInfo;

import java.util.Arrays;

/**
 * Created by xd on 6/21/2017.
 *
 */
public class ReNewAction extends ParkOrderBaseAction {
    String carNumber;
    int carPlateColorType;

    @Action("/renew/getRenew")
    public String getRenew(){
        ViewCarportRoomInfo[] carportInfo = ParkNative.getCarportInfo(carNumber, parkNo, carPlateColorType, 1);
        if (carportInfo == null || carportInfo.length < 1){
            state = "1300";
            msg = fail;
        } else {
            int money = 0;
            for (ViewCarportRoomInfo info : carportInfo){
                money += info.fRentMoney * 100;
            }
            put("billNo",carportInfo[0].sBillNo);
            put("carNumber", carNumber);
            put("carPlateColorType", carPlateColorType);
            put("parkingNo", parkNo);
            put("duoToTime", carportInfo[0].sEndDate);
            put("renewFeeList", String.format("[{\"renewPeriod\":1,\"renewFee\":%d}]",money));
        }
        return SUCCESS;
    }

    String billNo, renewTime;
    int renewPeroid, payWay, renewFee, onLinePayAmount, sysDiscount;


    @Action("/renew/renewPayNotice")
    public String renewPayNotice(){
        int ret = ParkNative.payCarportRent(carNumber,null
                ,null,null,renewTime
                ,billNo,renewPeroid,renewFee/100f
                ,true,carPlateColorType
                ,payWay,onLinePayAmount/100f,sysDiscount/100f,1);
        if (ret != 0){
            state = "2000";
        }
        return SUCCESS;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setCarPlateColorType(int carPlateColorType) {
        this.carPlateColorType = carPlateColorType;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public void setRenewTime(String renewTime) {
        this.renewTime = renewTime;
    }

    public void setRenewPeroid(int renewPeroid) {
        this.renewPeroid = renewPeroid;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public void setRenewFee(int renewFee) {
        this.renewFee = renewFee;
    }

    public void setOnLinePayAmount(int onLinePayAmount) {
        this.onLinePayAmount = onLinePayAmount;
    }

    public void setSysDiscount(int sysDiscount) {
        this.sysDiscount = sysDiscount;
    }
}
