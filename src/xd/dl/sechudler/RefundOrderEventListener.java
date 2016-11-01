package xd.dl.sechudler;

import com.alipay.api.AlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.dl.bean.DlOrder;
import xd.dl.service.DlService;
import xd.fw.AliClient;
import xd.fw.scheduler.RefundEvent;
import xd.fw.scheduler.RefundListener;

@Service
//@Async
public class RefundOrderEventListener extends RefundListener {
    @Autowired
    AliClient aliClient;
    @Autowired
    DlService dlService;
    @Override
    protected void processRefundStatus(String outTradeNo, boolean success) {
        dlService.runInSession(htpl -> {
            DlOrder dlOrder = htpl.load(DlOrder.class, outTradeNo);
            byte payStatus = success ? ORDER_STATUS_REFUND_DONE : ORDER_STATUS_REFUND_FAIL;
            dlOrder.setPayStatus(payStatus);
            htpl.update(dlOrder);
            return null;
        });
    }

    @Override
    protected AlipayClient alipayClient(RefundEvent event) {
        return aliClient.getAlipayClient();
    }
}
