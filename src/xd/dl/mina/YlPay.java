package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

import static xd.fw.mina.tlv.IMinaConst.ID_KEY;

@Service
public class YlPay extends BaseYlRequest{

    @Override
    protected String json(TLVMessage request, IoSession session) {
        // orderId -> carNumber -> price -> payCode
        JSONObject json = new JSONObject();
        json.put("merchantCode", merchantCode);
        json.put("terminalCode", getTidBySession(session));
        json.put("transactionCurrencyCode", "156");
        json.put("merchantOrderId", request.getValue());
        json.put("merchantRemark", request.getNextValue(0));
        json.put("transactionAmount", ((float)request.getNextValue(1)) * 100);
        json.put("payCode", request.getNextValue(2));
        json.put("payMode", "CODE_SCAN");

        return json.toString();
    }

    @Override
    protected String posType() {
        return "pay";
    }
}
