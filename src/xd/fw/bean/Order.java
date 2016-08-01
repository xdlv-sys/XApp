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
    private Integer totalFee;
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

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != null ? !orderId.equals(order.orderId) : order.orderId != null) return false;
        if (userId != null ? !userId.equals(order.userId) : order.userId != null) return false;
        if (year != null ? !year.equals(order.year) : order.year != null) return false;
        if (month != null ? !month.equals(order.month) : order.month != null) return false;
        if (payType != null ? !payType.equals(order.payType) : order.payType != null) return false;
        if (totalFee != null ? !totalFee.equals(order.totalFee) : order.totalFee != null) return false;
        if (tradeStatus != null ? !tradeStatus.equals(order.tradeStatus) : order.tradeStatus != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + (payType != null ? payType.hashCode() : 0);
        result = 31 * result + (totalFee != null ? totalFee.hashCode() : 0);
        result = 31 * result + (tradeStatus != null ? tradeStatus.hashCode() : 0);
        return result;
    }
}
