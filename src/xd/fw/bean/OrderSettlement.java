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
    private Integer userIdOne;
    private Integer countOne;
    private Integer userIdTwo;
    private Integer countTwo;
    private Integer userIdThree;
    private Integer countThree;
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

    public Integer getUserIdOne() {
        return userIdOne;
    }

    public void setUserIdOne(Integer userIdOne) {
        this.userIdOne = userIdOne;
    }

    public Integer getCountOne() {
        return countOne;
    }

    public void setCountOne(Integer countOne) {
        this.countOne = countOne;
    }

    public Integer getUserIdTwo() {
        return userIdTwo;
    }

    public void setUserIdTwo(Integer userIdTwo) {
        this.userIdTwo = userIdTwo;
    }

    public Integer getCountTwo() {
        return countTwo;
    }

    public void setCountTwo(Integer countTwo) {
        this.countTwo = countTwo;
    }

    public Integer getUserIdThree() {
        return userIdThree;
    }

    public void setUserIdThree(Integer userIdThree) {
        this.userIdThree = userIdThree;
    }

    public Integer getCountThree() {
        return countThree;
    }

    public void setCountThree(Integer countThree) {
        this.countThree = countThree;
    }

    public Timestamp getLastDate() {
        return lastDate;
    }

    public void setLastDate(Timestamp lastDate) {
        this.lastDate = lastDate;
    }

    public void setSettlementStatus(byte settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public byte getSettlementStatus() {
        return settlementStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderSettlement that = (OrderSettlement) o;

        if (orderId != that.orderId) return false;
        if (userId != that.userId) return false;
        if (userIdOne != null ? !userIdOne.equals(that.userIdOne) : that.userIdOne != null) return false;
        if (countOne != null ? !countOne.equals(that.countOne) : that.countOne != null) return false;
        if (userIdTwo != null ? !userIdTwo.equals(that.userIdTwo) : that.userIdTwo != null) return false;
        if (countTwo != null ? !countTwo.equals(that.countTwo) : that.countTwo != null) return false;
        if (userIdThree != null ? !userIdThree.equals(that.userIdThree) : that.userIdThree != null) return false;
        if (countThree != null ? !countThree.equals(that.countThree) : that.countThree != null) return false;
        if (lastDate != null ? !lastDate.equals(that.lastDate) : that.lastDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + userId;
        result = 31 * result + (userIdOne != null ? userIdOne.hashCode() : 0);
        result = 31 * result + (countOne != null ? countOne.hashCode() : 0);
        result = 31 * result + (userIdTwo != null ? userIdTwo.hashCode() : 0);
        result = 31 * result + (countTwo != null ? countTwo.hashCode() : 0);
        result = 31 * result + (userIdThree != null ? userIdThree.hashCode() : 0);
        result = 31 * result + (countThree != null ? countThree.hashCode() : 0);
        result = 31 * result + (lastDate != null ? lastDate.hashCode() : 0);
        return result;
    }
}
