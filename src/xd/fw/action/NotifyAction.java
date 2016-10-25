package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.bean.DlOrder;
import xd.fw.service.DlService;

/**
 * Created by xd Lv on 10/25/2016.
 */
public class NotifyAction extends PayNotifyBaseAction{
    @Value("{app_id}")
    String appId;
    @Value("{mch_id}")
    String mchId;
    @Value("${wx_key}")
    String wxKey;

    @Autowired
    DlService dlService;

    @Override
    protected String getWxKey(String out_trade_no) {
        return wxKey;
    }

    @Override
    protected void processOrder(String out_trade_no, String transaction_id, boolean success) {
        DlOrder dlOrder = dlService.get(DlOrder.class, out_trade_no);
        dlOrder.setPayStatus(success ? STATUS_DONE : STATUS_FAIL);
        // TODO notify app
        dlService.save(dlOrder);
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
