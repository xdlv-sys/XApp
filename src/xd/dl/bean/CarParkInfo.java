package xd.dl.bean;

public class CarParkInfo {
    String carNumber;
    float price;
    String startTime;
    int consumedTime;
    String parkId;
    String dbId;

    byte[] carImageData;
    byte carOrder;

    boolean aliPay = false;
    boolean wxPay = false;
    boolean hasPic = false;

    public CarParkInfo(){}

    public CarParkInfo(String carNumber, float price, String startTime
            ,int consumedTime, String parkId, byte[] carImageRawData
            , byte carOrder, String dbId) {
        this();
        this.carNumber = carNumber;
        this.price = price;
        this.startTime = startTime;
        this.parkId = parkId;
        this.consumedTime = consumedTime;
        this.carOrder = carOrder;

        this.carImageData = carImageRawData;
        this.hasPic = this.carImageData != null;
        this.dbId = dbId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setConsumedTime(int consumedTime) {
        this.consumedTime = consumedTime;
    }

    public int getConsumedTime() {
        return consumedTime;
    }

    public boolean isAliPay() {
        return aliPay;
    }

    public void setAliPay(boolean aliPay) {
        this.aliPay = aliPay;
    }

    public boolean isWxPay() {
        return wxPay;
    }

    public void setWxPay(boolean wxPay) {
        this.wxPay = wxPay;
    }

    public byte[] carImageData() {
        return carImageData;
    }

    public byte getCarOrder() {
        return carOrder;
    }

    public boolean isHasPic() {
        return hasPic;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }
}
