package xd.dl.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.dl.bean.DlOrder;
import xd.fw.action.BaseAction;
import xd.dl.service.DlService;

/**
 * Created by exiglvv on 10/25/2016.
 */
public class OrderAction extends BaseAction {
    @Autowired
    DlService dlService;

    DlOrder dlOrder;
    boolean query;

    String donecode;

    public String queryOrderStatus(){
        dlOrder = dlService.get(DlOrder.class, donecode);
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

    public void setDonecode(String donecode) {
        this.donecode = donecode;
    }
}
