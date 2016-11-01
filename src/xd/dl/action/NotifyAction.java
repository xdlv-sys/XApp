package xd.dl.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.DlConst;
import xd.dl.bean.DlOrder;
import xd.dl.service.DlService;
import xd.fw.AliClient;
import xd.fw.action.PayNotifyBaseAction;
import xd.fw.bean.Event;
import xd.fw.service.FwService;

/**
 * Created by xd Lv on 10/25/2016.
 */
public class NotifyAction extends PayNotifyBaseAction implements DlConst {
    @Value("${wx_key}")
    String wxKey;
    @Value("${partner_id}")
    String pid;

    @Autowired
    AliClient aliClient;

    @Autowired
    DlService dlService;

    @Autowired
    FwService fwService;

    @Override
    protected String wxKey(String out_trade_no) {
        return wxKey;
    }

    @Override
    protected boolean processOrder(String out_trade_no, String transaction_id, boolean success) {
        String outTradeNo = dlService.runInSession((htpl -> {
            DlOrder dlOrder = htpl.get(DlOrder.class, out_trade_no);
            if (dlOrder.getPayStatus() != STATUS_INI) {
                log.info("order already be processed:{}", dlOrder.getPayStatus());
                return null;
            }
            dlOrder.setPayStatus(success ? STATUS_DONE : STATUS_FAIL);
            htpl.update(dlOrder);

            return dlOrder.getOutTradeNo();
        }));
        //notify app
        fwService.triggerEvent(new Event(NOTIFY_APP, 0, outTradeNo));
        return true;
    }

    @Override
    protected String pid(String out_trade_no) {
        return pid;
    }

    @Override
    protected String aliPublicKey() {
        return aliClient.getAliPublicKey();
    }

    @Override
    protected String aliReturnHook(String out_trade_no) {
        return null;
    }
}
