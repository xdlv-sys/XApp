package xd.dl.bean;

import xd.fw.service.IConst;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "t_charge")
public class Charge {
    @Id
    private String outTradeNo;
    private String tradeNo;
    private String parkId;
    private Float totalFee;

    private String carNumber;
    private String roomNumber;
    private String carPorts;
    private Byte months;
    private String userName;

    private Short payStatus = IConst.STATUS_INI;

    private Short notifyStatus = IConst.STATUS_INI;
    private Short payFlag;
    private Timestamp timeStamp;

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

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCarPorts() {
        return carPorts;
    }

    public void setCarPorts(String carPorts) {
        this.carPorts = carPorts;
    }

    public Byte getMonths() {
        return months;
    }

    public void setMonths(Byte months) {
        this.months = months;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Charge payOrder = (Charge) o;

        if (outTradeNo != null ? !outTradeNo.equals(payOrder.outTradeNo) : payOrder.outTradeNo != null) return false;
        if (tradeNo != null ? !tradeNo.equals(payOrder.tradeNo) : payOrder.tradeNo != null) return false;
        if (parkId != null ? !parkId.equals(payOrder.parkId) : payOrder.parkId != null) return false;
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
        result = 31 * result + (totalFee != null ? totalFee.hashCode() : 0);
        result = 31 * result + (payStatus != null ? payStatus.hashCode() : 0);
        result = 31 * result + (notifyStatus != null ? notifyStatus.hashCode() : 0);
        result = 31 * result + (payFlag != null ? payFlag.hashCode() : 0);
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        return result;
    }
}
