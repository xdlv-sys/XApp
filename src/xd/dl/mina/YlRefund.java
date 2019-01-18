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
        json.put("merchantOrderId", request.getNextValue(2));
        json.put("refundRequestId", request.getNextValue(2) + WxUtil.getRandomStringByLength(12));
        json.put("transactionAmount", ((float)request.getNextValue(1)) * 100);

        return json.toString();
    }

    @Override
    protected String posType() {
        return "refund";
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
        return ret.setNext(YL_SCAN_SUCCESS.equals(retJson.getString("errCode")) ? 0 : 1)
                .setNext(retJson.getString("errInfo")).setNext(request);
    }
}
