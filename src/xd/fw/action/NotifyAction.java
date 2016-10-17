package xd.fw.action;

import com.alipay.api.internal.util.XmlUtils;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xd.fw.AliPayUtil;
import xd.fw.WxUtil;
import xd.fw.bean.Const;
import xd.fw.bean.ParkInfo;
import xd.fw.bean.PayOrder;
import xd.fw.service.ParkService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by exiglvv on 7/5/2016.
 */
public class NotifyAction extends ParkBaseAction implements Const{
    @Autowired
    ParkService parkService;

    enum PS_STATUS{PS_UPDATED , PS_ALREADY_HANDLED, PS_VERIFY_FAILED ,PS_INVALIDATE};
    String out_trade_no,trade_no,trade_status,total_fee,seller_id;

    @Action("wxNotify")
    public String wxNotify() throws Exception {
        Element rootElement = XmlUtils.getRootElementFromStream(ServletActionContext.getRequest().getInputStream());
        NodeList childNodes = rootElement.getChildNodes();
        Node node;
        Map<String, String> params = new HashMap<>(childNodes.getLength());
        for (int i =0 ;i < childNodes.getLength();i++){
            node = childNodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE){
                continue;
            }
            params.put(node.getNodeName(), XmlUtils.getElementValue(rootElement,node.getNodeName()));
        }

        String out_trade_no = params.get("out_trade_no");
        PayOrder payOrder = payService.get(PayOrder.class,out_trade_no);
        ParkInfo parkInfo = payService.get(ParkInfo.class, payOrder.getParkId());
        boolean verification = WxUtil.verify(params, parkInfo.getWxKey());
        if (verification){
            boolean success = SUCCESS_FLAG.equals(params.get("return_code"));
            payService.updateInitialPayOrderStatus(out_trade_no
                    ,params.get("transaction_id"), success ? Const.STATUS_SUCCESS : Const.STATUS_FAIL);
        } else {
            logger.warn("wx verification failed:" + params);
        }
        returnXml("<xml>" +
                "  <return_code><![CDATA[" + (verification ? SUCCESS_FLAG : "FAIL") +"]]></return_code>" +
                "  <return_msg><![CDATA[OK]]></return_msg>" +
                "</xml>");

        return XML;
    }

    @Action("aliNotify")
    public String aliNotify() throws Exception {
        PS_STATUS status = processOrder();
        returnXml(status == PS_STATUS.PS_VERIFY_FAILED ? "fail" : "success");
        return XML;
    }
    @Action("aliReturn")
    public String aliReturn() throws Exception {
        processOrder();
        PayOrder order = parkService.get(PayOrder.class,out_trade_no);
        setRequestAttribute(RET_KEY, JSONObject.fromObject(order));
        return PAY;
    }

    private PS_STATUS processOrder() throws Exception {
        PayOrder order = parkService.get(PayOrder.class,out_trade_no);
        if (order == null){
            return PS_STATUS.PS_INVALIDATE;
        }
        if (order.getPayStatus() != Const.STATUS_INITIAL){
            log.info("the order is handled already.");
            return PS_STATUS.PS_ALREADY_HANDLED;
        }
        ParkInfo parkInfo = parkService.get(ParkInfo.class, order.getParkId());
        if (AliPayUtil.verify(ServletActionContext.getRequest().getParameterMap()
                , parkInfo.getPartnerId())){
            if (order.getTotalFee() != Float.parseFloat(total_fee)
                    || !seller_id.equals(parkInfo.getPartnerId())){
                return PS_STATUS.PS_INVALIDATE;
            }
            boolean tradeSuccess = trade_status.equals("TRADE_SUCCESS");

            boolean updated = payService.updateInitialPayOrderStatus(out_trade_no,trade_no
                    ,tradeSuccess? Const.STATUS_SUCCESS : Const.STATUS_FAIL);
            if (!updated){
                return PS_STATUS.PS_ALREADY_HANDLED;
            }
        } else {
            return PS_STATUS.PS_VERIFY_FAILED;
        }
        return PS_STATUS.PS_UPDATED;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }
}
