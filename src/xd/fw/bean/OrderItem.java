package xd.fw.bean;

/**
 * Created by xd on 2016/8/7.
 */
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int goodId;

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (orderItemId != orderItem.orderItemId) return false;
        if (orderId != orderItem.orderId) return false;
        if (goodId != orderItem.goodId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderItemId;
        result = 31 * result + orderId;
        result = 31 * result + goodId;
        return result;
    }
}
