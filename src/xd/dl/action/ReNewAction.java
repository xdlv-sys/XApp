package xd.dl.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import xd.dl.job.ParkNative;
import xd.dl.job.ViewCarportRoomInfo;
import xd.fw.FwException;

import java.text.ParseException;

/**
 * Created by xd on 6/21/2017.
 *
 */
@Action("/dtkServer/renew")
public class ReNewAction extends DLBaseAction {
    int carPlateColorType;
    String duoToTime = "";

    RenewFee[] renewFeeList;

    @Action("getRenew")
    public String obtainRenew() throws ParseException {
        if (StringUtils.isBlank(carNumber) || StringUtils.isBlank(parkingNo)){
            throw new FwException("parameter is not invalidate");
        }
        ViewCarportRoomInfo[] carportInfo = ParkNative.getCarportInfo(carNumber, parkNo, carPlateColorType, 1);
        if (carportInfo == null || carportInfo.length < 1){
            state = "1300";
            msg = fail;
        } else {
            int money = 0;
            for (ViewCarportRoomInfo info : carportInfo){
                money += info.fRentMoney * 100;
            }
            billNo = carportInfo[0].sBillNo;
            duoToTime =  carportInfo[0].sEndDate;
            renewFeeList = new RenewFee[]{
                    new RenewFee(1, money)
            };
        }
        return SUCCESS;
    }

    String billNo = "", renewTime = "";
    int renewPeroid, payWay, renewFee, onLinePayAmount, sysDiscount;

    @Action("renewPayNotice")
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

    public String getBillNo() {
        return billNo;
    }
    public String getCarNumber() {
        return carNumber;
    }
    public String getParkingNo(){
        return parkingNo;
    }

    public String getDuoToTime() {
        return duoToTime;
    }

    public  RenewFee[] getRenewFeeList() {
        return renewFeeList;
    }

    public int getCarPlateColorType() {
        return carPlateColorType;
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
