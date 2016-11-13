package xd.dl.bean;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "t_dl_order")
@DynamicUpdate
public class DlOrder {
    @Id
    private String outTradeNo;
    private String tradeNo;
    private String userNo;
    private double totalFee;
    private short payStatus;
    private short notifyStatus;
    private short payFlag;
    private String codeUrl;
    private Timestamp timeStamp;

    public DlOrder(){}

    public DlOrder(String outTradeNo, String tradeNo,String userNo, double totalFee, short payFlag, String codeUrl) {
        this();
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.userNo = userNo;
        this.totalFee = totalFee;
        this.payFlag = payFlag;
        this.codeUrl = codeUrl;
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(short payStatus) {
        this.payStatus = payStatus;
    }

    public short getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(short notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public short getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(short payFlag) {
        this.payFlag = payFlag;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DlOrder dlOrder = (DlOrder) o;

        if (Double.compare(dlOrder.totalFee, totalFee) != 0) return false;
        if (payStatus != dlOrder.payStatus) return false;
        if (notifyStatus != dlOrder.notifyStatus) return false;
        if (payFlag != dlOrder.payFlag) return false;
        if (outTradeNo != null ? !outTradeNo.equals(dlOrder.outTradeNo) : dlOrder.outTradeNo != null) return false;
        if (userNo != null ? !userNo.equals(dlOrder.userNo) : dlOrder.userNo != null) return false;
        if (timeStamp != null ? !timeStamp.equals(dlOrder.timeStamp) : dlOrder.timeStamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = outTradeNo != null ? outTradeNo.hashCode() : 0;
        result = 31 * result + (userNo != null ? userNo.hashCode() : 0);
        temp = Double.doubleToLongBits(totalFee);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) payStatus;
        result = 31 * result + (int) notifyStatus;
        result = 31 * result + (int) payFlag;
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        return result;
    }
}
