package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.DlOrder;
import xd.fw.service.DlService;

/**
 * Created by exiglvv on 10/25/2016.
 */
public class OrderAction extends BaseAction {
    @Autowired
    DlService dlService;
    DlOrder dlOrder;
    boolean query;

    public String queryOrderStatus(){
        dlOrder = dlService.get(DlOrder.class, dlOrder.getOutTradeNo());
        return SUCCESS;
    }

    public void setDlOrder(DlOrder dlOrder) {
        this.dlOrder = dlOrder;
    }

    public DlOrder getDlOrder() {
        return dlOrder;
    }

    public void setQuery(boolean query) {
        this.query = query;
    }
}
