package xd.fw.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_jkn_account_item")
public class AccountItem {
    @Id
    private Integer itemId;
    private Integer orderId;
    private Integer userOne;
    private Integer userTwo;
    private Integer userThree;
    private Byte itemStatus;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserOne() {
        return userOne;
    }

    public void setUserOne(Integer userOne) {
        this.userOne = userOne;
    }

    public Integer getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(Integer userTwo) {
        this.userTwo = userTwo;
    }

    public Integer getUserThree() {
        return userThree;
    }

    public void setUserThree(Integer userThree) {
        this.userThree = userThree;
    }

    public Byte getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(Byte itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountItem that = (AccountItem) o;

        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (userOne != null ? !userOne.equals(that.userOne) : that.userOne != null) return false;
        if (userTwo != null ? !userTwo.equals(that.userTwo) : that.userTwo != null) return false;
        if (userThree != null ? !userThree.equals(that.userThree) : that.userThree != null) return false;
        if (itemStatus != null ? !itemStatus.equals(that.itemStatus) : that.itemStatus != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (userOne != null ? userOne.hashCode() : 0);
        result = 31 * result + (userTwo != null ? userTwo.hashCode() : 0);
        result = 31 * result + (userThree != null ? userThree.hashCode() : 0);
        result = 31 * result + (itemStatus != null ? itemStatus.hashCode() : 0);
        return result;
    }
}
