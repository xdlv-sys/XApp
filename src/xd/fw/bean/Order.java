package xd.fw.bean;

import java.sql.Timestamp;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "t_jkn_order")
@org.hibernate.annotations.Entity(
        dynamicUpdate = true
)
public class Order {
    @Id
    private int orderId;
    private int userId;
    private short year;
    private byte month;
    private byte payType;
    private byte tradeType;
    private int totalFee;
    private int balanceFee;
    private int storeUserId;
    private byte tradeStatus;
    private Timestamp lastDate;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public byte getMonth() {
        return month;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public byte getPayType() {
        return payType;
    }

    public void setPayType(byte payType) {
        this.payType = payType;
    }

    public byte getTradeType() {
        return tradeType;
    }

    public void setTradeType(byte tradeType) {
        this.tradeType = tradeType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getBalanceFee() {
        return balanceFee;
    }

    public void setBalanceFee(int balanceFee) {
        this.balanceFee = balanceFee;
    }

    public void setBalanceFee(String balanceFee) {
        if (StringUtils.isNotBlank(balanceFee)){
            try{
                this.balanceFee = Integer.parseInt(balanceFee);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
    }

    public int getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(int storeUserId) {
        this.storeUserId = storeUserId;
    }

    public byte getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(byte tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Timestamp getLastDate() {
        return lastDate;
    }

    public void setLastDate(Timestamp lastDate) {
        this.lastDate = lastDate;
    }

    public void setLastUpdateS(String s) throws ParseException {
        this.lastDate = new Timestamp(new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s).getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != order.orderId) return false;
        if (userId != order.userId) return false;
        if (year != order.year) return false;
        if (month != order.month) return false;
        if (payType != order.payType) return false;
        if (tradeType != order.tradeType) return false;
        if (totalFee != order.totalFee) return false;
        if (balanceFee != order.balanceFee) return false;
        if (tradeStatus != order.tradeStatus) return false;
        if (lastDate != null ? !lastDate.equals(order.lastDate) : order.lastDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + userId;
        result = 31 * result + (int) year;
        result = 31 * result + (int) month;
        result = 31 * result + (int) payType;
        result = 31 * result + (int) tradeType;
        result = 31 * result + totalFee;
        result = 31 * result + balanceFee;
        result = 31 * result + (int) tradeStatus;
        result = 31 * result + (lastDate != null ? lastDate.hashCode() : 0);
        return result;
    }
}
