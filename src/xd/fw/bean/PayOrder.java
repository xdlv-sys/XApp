package xd.fw.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "t_pay_order")
public class PayOrder {
    @Id
    private String outTradeNo;
    private String tradeNo;
    private String parkId;
    private String carNumber;
    private Float totalFee;
    private Short payStatus = Const.STATUS_INITIAL;

    private Short notifyStatus;
    private Short payFlag;
    private String watchId;
    private byte carType;
    private Timestamp timeStamp;

    public PayOrder(){}

    public PayOrder(String outTradeNo, String parkId, String carNumber, Float totalFee, Short payStatus
            , Short payFlag, String watchId, byte carType) {
        this.outTradeNo = outTradeNo;
        this.parkId = parkId;
        this.carNumber = carNumber;
        this.totalFee = totalFee;
        this.payStatus = payStatus;
        this.payFlag = payFlag;
        this.watchId = watchId;
        this.carType = carType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Float getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Float totalFee) {
        this.totalFee = totalFee;
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public Short getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(Short notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public Short getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(Short payFlag) {
        this.payFlag = payFlag;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getWatchId() {
        return watchId;
    }

    public void setWatchId(String watchId) {
        this.watchId = watchId;
    }

    public byte getCarType() {
        return carType;
    }

    public void setCarType(byte carType) {
        this.carType = carType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayOrder payOrder = (PayOrder) o;

        if (outTradeNo != null ? !outTradeNo.equals(payOrder.outTradeNo) : payOrder.outTradeNo != null) return false;
        if (tradeNo != null ? !tradeNo.equals(payOrder.tradeNo) : payOrder.tradeNo != null) return false;
        if (parkId != null ? !parkId.equals(payOrder.parkId) : payOrder.parkId != null) return false;
        if (carNumber != null ? !carNumber.equals(payOrder.carNumber) : payOrder.carNumber != null) return false;
        if (totalFee != null ? !totalFee.equals(payOrder.totalFee) : payOrder.totalFee != null) return false;
        if (payStatus != null ? !payStatus.equals(payOrder.payStatus) : payOrder.payStatus != null) return false;
        if (notifyStatus != null ? !notifyStatus.equals(payOrder.notifyStatus) : payOrder.notifyStatus != null)
            return false;
        if (payFlag != null ? !payFlag.equals(payOrder.payFlag) : payOrder.payFlag != null) return false;
        if (timeStamp != null ? !timeStamp.equals(payOrder.timeStamp) : payOrder.timeStamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = outTradeNo != null ? outTradeNo.hashCode() : 0;
        result = 31 * result + (tradeNo != null ? tradeNo.hashCode() : 0);
        result = 31 * result + (parkId != null ? parkId.hashCode() : 0);
        result = 31 * result + (carNumber != null ? carNumber.hashCode() : 0);
        result = 31 * result + (totalFee != null ? totalFee.hashCode() : 0);
        result = 31 * result + (payStatus != null ? payStatus.hashCode() : 0);
        result = 31 * result + (notifyStatus != null ? notifyStatus.hashCode() : 0);
        result = 31 * result + (payFlag != null ? payFlag.hashCode() : 0);
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        return result;
    }
}
