package xd.dl.scheduler;

import org.springframework.context.ApplicationEvent;
<<<<<<< ad674155dd3266a10bf86d9535f55c7cf32768c3:src/xd/fw/scheduler/RefundEvent.java
=======
import xd.dl.bean.PayOrder;
>>>>>>> v1.0:src/xd/dl/scheduler/RefundEvent.java

import xd.fw.service.IConst;

public class RefundEvent extends ApplicationEvent implements IConst {

    String id = "001";

    byte payType;

    String outTradeNo;
    float totalFee;

    String appId;
    String mchId;
    String wxKey;

    String rsaKey;

    public static RefundEvent wxRefund(String outTradeNo, float totalFee
            , String appId, String mchId, String wxKey) {
        RefundEvent event = new RefundEvent(outTradeNo);
        event.payType = PAY_WX;
        event.outTradeNo = outTradeNo;
        event.totalFee = totalFee;
        event.appId = appId;
        event.mchId = mchId;
        event.wxKey = wxKey;
        return event;
    }

    public static RefundEvent aliRefund(String outTradeNo, float totalFee
            , String appId, String rsaKey) {
        RefundEvent event = new RefundEvent(outTradeNo);
        event.payType = PAY_ALI;
        event.outTradeNo = outTradeNo;
        event.totalFee = totalFee;
        event.appId = appId;
        event.rsaKey = rsaKey;
        return event;
    }

    public RefundEvent(String outTradeNo) {
        super(outTradeNo);
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public float getTotalFee() {
        return totalFee;
    }

    public String getAppId() {
        return appId;
    }

    public String getMchId() {
        return mchId;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte getPayType() {
        return payType;
    }

    public String getWxKey() {
        return wxKey;
    }
}
