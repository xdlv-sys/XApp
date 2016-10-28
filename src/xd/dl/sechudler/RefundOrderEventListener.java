package xd.dl.sechudler;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.XmlUtils;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import xd.dl.bean.DlOrder;
import xd.dl.service.DlService;
import xd.fw.WxUtil;
import xd.fw.scheduler.RefundListener;
import xd.fw.service.IConst;

import java.util.ArrayList;
import java.util.List;

@Service
//@Async
public class RefundOrderEventListener extends RefundListener {
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
}
