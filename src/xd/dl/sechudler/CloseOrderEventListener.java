package xd.dl.sechudler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.dl.bean.DlOrder;
import xd.dl.service.DlService;
import xd.fw.scheduler.CloseOrderListener;

@Service
public class CloseOrderEventListener extends CloseOrderListener {
    @Autowired
    DlService dlService;
    @Override
    protected void processCloseStatus(String outTradeNo, boolean success) {
        dlService.runInSession(htpl -> {
            DlOrder dlOrder = htpl.load(DlOrder.class, outTradeNo);
            dlOrder.setPayStatus(success ? ORDER_STATUS_CLOSE_DONE : ORDER_STATUS_CLOSE_FAIL);
            htpl.update(dlOrder);
            return null;
        });
    }
}
