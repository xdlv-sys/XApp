package xd.fw.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "t_jkn_account_item")
public class AccountItem {
    @Id
    private int itemId;
    private int orderId;
    private int userOne;
    private int userTwo;
    private int userThree;
    private byte itemStatus;
    private Timestamp lastUpdate;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserOne() {
        return userOne;
    }

    public void setUserOne(int userOne) {
        this.userOne = userOne;
    }

    public int getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(int userTwo) {
        this.userTwo = userTwo;
    }

    public int getUserThree() {
        return userThree;
    }

    public void setUserThree(int userThree) {
        this.userThree = userThree;
    }

    public byte getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(byte itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountItem that = (AccountItem) o;

        if (itemId != that.itemId) return false;
        if (orderId != that.orderId) return false;
        if (userOne != that.userOne) return false;
        if (userTwo != that.userTwo) return false;
        if (userThree != that.userThree) return false;
        if (itemStatus != that.itemStatus) return false;
        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId;
        result = 31 * result + orderId;
        result = 31 * result + userOne;
        result = 31 * result + userTwo;
        result = 31 * result + userThree;
        result = 31 * result + (int) itemStatus;
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        return result;
    }
}
