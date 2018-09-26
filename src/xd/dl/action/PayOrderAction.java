package xd.dl.action;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import xd.dl.job.IDongHui;
import xd.dl.job.ParkNative;
import xd.dl.mina.ParkHandler;
import xd.dl.mina.ParkProxy;
@Action("/dtkServer/payOrder")
public class PayOrderAction extends DLBaseAction implements IDongHui {

    String orderNo = "", paySeq = "", parkingNo = "";
    int carPlateColorType;
    String enterTime = "", payStartTime ="", payEndTime = "";
    int payWay, payFee, payAmount, sysDiscount;

    @Autowired
    ParkHandler parkHandler;

    @Action("payNotice")
    public String payNotice() throws Exception{
        int ret = ParkNative.payParkCarFee(orderNo,0,carNumber
                , convertTime(payEndTime), payFee/100.0f,"", ""
                ,0,paySeq
                , convertTime(enterTime)
                , convertTime(payStartTime)
                , convertTime(payEndTime),payWay,payFee/100.0f,sysDiscount/100.0f);
        if (ret != 0){
            state = "1013";
            msg = fail;
        } else {
            parkHandler.notifyWatchIdPayFee(ParkProxy.PAY_FEE_NOTIFY, carNumber,payFee/100.0f,orderNo,"",0);
        }
        return SUCCESS;
    }

    @Action("accountCheck")
    public String accountCheck() throws Exception{
        return SUCCESS;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setPaySeq(String paySeq) {
        this.paySeq = paySeq;
    }

    public void setParkingNo(String parkingNo) {
        this.parkingNo = parkingNo;
    }

    public void setCarPlateColorType(int carPlateColorType) {
        this.carPlateColorType = carPlateColorType;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    public void setPayStartTime(String payStartTime) {
        this.payStartTime = payStartTime;
    }

    public void setPayEndTime(String payEndTime) {
        this.payEndTime = payEndTime;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public void setPayFee(int payFee) {
        this.payFee = payFee;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public void setSysDiscount(int sysDiscount) {
        this.sysDiscount = sysDiscount;
    }
}
