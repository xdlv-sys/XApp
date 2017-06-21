package xd.dl.action;

import org.apache.struts2.convention.annotation.Action;
import xd.dl.job.IDongHui;
import xd.dl.job.ParkNative;

import java.text.SimpleDateFormat;

public class PayOrderAction extends ParkOrderBaseAction implements IDongHui {

    String orderNo, paySeq, parkingNo, carNumber;
    int carPlateColorType;
    String enterTime, payStartTime, payEndTime;
    int payWay, payFee, payAmount, sysDiscount;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String state, msg;
    @Action("/payOrder/payNotice")
    public String getParkOrder() throws Exception{
        int ret = ParkNative.payParkCarFee(orderNo,0,carNumber,payStartTime, payFee/100.0f,null, null,1,paySeq
                ,sdf.format(sdf2.parse(enterTime))
                ,sdf.format(sdf2.parse(payStartTime))
                ,sdf.format(sdf2.parse(payEndTime)),payWay,payFee/100.0f,sysDiscount/100.0f);
        if (ret != 0){
            state = "1013";
            msg = fail;
        }
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

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
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
