package xd.dl.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.dl.DlConst;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.PayOrder;
import xd.dl.service.PayService;
import xd.fw.scheduler.RefundEvent;

public class RefundAction extends ParkBaseAction implements DlConst {
    String outTradeNo;
    @Autowired
    PayService payService;

    public String refund() {
        PayOrder order = payService.get(PayOrder.class, outTradeNo);
        // pay successfully and fail to notify
        assert order != null &&
                order.getPayStatus() == STATUS_DONE && order.getNotifyStatus() == STATUS_FAIL;
        ParkInfo parkInfo = payService.get(ParkInfo.class, order.getParkId());
        RefundEvent event;
        if (order.getPayFlag() == PAY_WX){
            event = RefundEvent.wxRefund(order.getOutTradeNo(),order.getTotalFee()
                    ,parkInfo.getAppId(),parkInfo.getMchId(), parkInfo.getWxKey());
        } else {
            event = RefundEvent.aliRefund(order.getOutTradeNo()
                    ,order.getTotalFee(),parkInfo.getAliAppId(),parkInfo.getAliShaRsaKey());
        }
        applicationContext.publishEvent(event);
        return SUCCESS;
    }
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }
}
