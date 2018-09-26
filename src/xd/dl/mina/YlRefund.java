package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import xd.fw.WxUtil;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class YlRefund extends BaseYlRequest{

    @Override
    protected String json(TLVMessage request, IoSession session) {
        // orderId -> price
        JSONObject json = new JSONObject();
        json.put("merchantCode", merchantCode);
        json.put("terminalCode", getTidBySession(session));
        json.put("merchantOrderId", request.getValue());
        json.put("refundRequestId", request.getValue() + WxUtil.getRandomStringByLength(12));
        json.put("transactionAmount", ((float)request.getNextValue(1)) * 100);

        return json.toString();
    }

    @Override
    protected String posType() {
        return "refund";
    }
}
