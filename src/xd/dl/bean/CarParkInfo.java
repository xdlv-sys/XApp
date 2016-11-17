package xd.dl.bean;

import java.util.Base64;

public class CarParkInfo {
    String carNumber;
    float price;
    String startTime;
    int consumedTime;
    String parkId;

    byte[] carImageData;
    byte carOrder;

    boolean aliPay = false;
    boolean wxPay = false;

    public CarParkInfo(){}

    public CarParkInfo(String carNumber, float price, String startTime
            ,int consumedTime, String parkId, byte[] carImageRawData, byte carOrder) {
        this();
        this.carNumber = carNumber;
        this.price = price;
        this.startTime = startTime;
        this.parkId = parkId;
        this.consumedTime = consumedTime;
        this.carOrder = carOrder;

        this.carImageData = carImageRawData;
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
}
