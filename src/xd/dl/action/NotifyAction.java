package xd.dl.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.DlConst;
import xd.dl.bean.DlOrder;
import xd.fw.action.PayNotifyBaseAction;
import xd.dl.service.DlService;
import xd.fw.bean.Event;
import xd.fw.service.FwService;

/**
 * Created by xd Lv on 10/25/2016.
 */
public class NotifyAction extends PayNotifyBaseAction implements DlConst{
    @Value("{app_id}")
    String appId;
    @Value("{mch_id}")
    String mchId;
    @Value("${wx_key}")
    String wxKey;

    @Autowired
    DlService dlService;

    @Autowired
    FwService fwService;

    @Override
    protected String getWxKey(String out_trade_no) {
        return wxKey;
    }

    @Override
    protected void processOrder(String out_trade_no, String transaction_id, boolean success) {
        DlOrder dlOrder = dlService.get(DlOrder.class, out_trade_no);
        dlOrder.setPayStatus(success ? STATUS_DONE : STATUS_FAIL);

        fwService.triggerEvent(new Event(NOTIFY_APP,0,dlOrder.getOutTradeNo()));
        dlService.saveOrUpdate(dlOrder);
    }

    @Override
    protected String getPid(String out_trade_no) {
        return null;
    }

    @Override
    protected String aliReturnHook(String out_trade_no) {
        return null;
    }
}
