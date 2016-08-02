package xd.fw.bean;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "t_jkn_order")
public class Order {
    @Id
    private Integer orderId;
    private Integer userId;
    private Short year;
    private Byte month;
    private Byte payType;
    private Byte tradeType;
    private int totalFee;
    private int balanceFee;
    private Byte tradeStatus;


    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDate;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Byte getMonth() {
        return month;
    }

    public void setMonth(Byte month) {
        this.month = month;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public Byte getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Byte tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastUpdateS(String s) throws ParseException {
        this.lastDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
    }

    public Byte getTradeType() {
        return tradeType;
    }

    public void setTradeType(Byte tradeType) {
        this.tradeType = tradeType;
    }

    public int getBalanceFee() {
        return balanceFee;
    }

    public void setBalanceFee(int balanceFee) {
        this.balanceFee = balanceFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (totalFee != order.totalFee) return false;
        if (balanceFee != order.balanceFee) return false;
        if (orderId != null ? !orderId.equals(order.orderId) : order.orderId != null) return false;
        if (userId != null ? !userId.equals(order.userId) : order.userId != null) return false;
        if (year != null ? !year.equals(order.year) : order.year != null) return false;
        if (month != null ? !month.equals(order.month) : order.month != null) return false;
        if (payType != null ? !payType.equals(order.payType) : order.payType != null) return false;
        if (tradeType != null ? !tradeType.equals(order.tradeType) : order.tradeType != null) return false;
        if (tradeStatus != null ? !tradeStatus.equals(order.tradeStatus) : order.tradeStatus != null) return false;
        return lastDate != null ? lastDate.equals(order.lastDate) : order.lastDate == null;

    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + (payType != null ? payType.hashCode() : 0);
        result = 31 * result + (tradeType != null ? tradeType.hashCode() : 0);
        result = 31 * result + totalFee;
        result = 31 * result + balanceFee;
        result = 31 * result + (tradeStatus != null ? tradeStatus.hashCode() : 0);
        result = 31 * result + (lastDate != null ? lastDate.hashCode() : 0);
        return result;
    }
}
