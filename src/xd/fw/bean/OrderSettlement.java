package xd.fw.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "t_jkn_order_settlement")
public class OrderSettlement {
    @Id
    private int orderId;
    private int userId;
    private int userIdOne;
    private int countOne;
    private int userIdTwo;
    private int countTwo;
    private int userIdThree;
    private int countThree;
    private byte settlementStatus;
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

    public int getUserIdOne() {
        return userIdOne;
    }

    public void setUserIdOne(int userIdOne) {
        this.userIdOne = userIdOne;
    }

    public int getCountOne() {
        return countOne;
    }

    public void setCountOne(int countOne) {
        this.countOne = countOne;
    }

    public int getUserIdTwo() {
        return userIdTwo;
    }

    public void setUserIdTwo(int userIdTwo) {
        this.userIdTwo = userIdTwo;
    }

    public int getCountTwo() {
        return countTwo;
    }

    public void setCountTwo(int countTwo) {
        this.countTwo = countTwo;
    }

    public int getUserIdThree() {
        return userIdThree;
    }

    public void setUserIdThree(int userIdThree) {
        this.userIdThree = userIdThree;
    }

    public int getCountThree() {
        return countThree;
    }

    public void setCountThree(int countThree) {
        this.countThree = countThree;
    }

    public byte getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(byte settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public Timestamp getLastDate() {
        return lastDate;
    }

    public void setLastDate(Timestamp lastDate) {
        this.lastDate = lastDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderSettlement that = (OrderSettlement) o;

        if (orderId != that.orderId) return false;
        if (userId != that.userId) return false;
        if (userIdOne != that.userIdOne) return false;
        if (countOne != that.countOne) return false;
        if (userIdTwo != that.userIdTwo) return false;
        if (countTwo != that.countTwo) return false;
        if (userIdThree != that.userIdThree) return false;
        if (countThree != that.countThree) return false;
        if (settlementStatus != that.settlementStatus) return false;
        if (lastDate != null ? !lastDate.equals(that.lastDate) : that.lastDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + userId;
        result = 31 * result + userIdOne;
        result = 31 * result + countOne;
        result = 31 * result + userIdTwo;
        result = 31 * result + countTwo;
        result = 31 * result + userIdThree;
        result = 31 * result + countThree;
        result = 31 * result + (int) settlementStatus;
        result = 31 * result + (lastDate != null ? lastDate.hashCode() : 0);
        return result;
    }
}
