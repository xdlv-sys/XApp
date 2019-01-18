package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class YlQuery extends BaseYlRequest{

    @Override
    protected String json(TLVMessage request, IoSession session) {
        // orderId
        return _json(merchantCode, getTidBySession(session), String.valueOf(request.getValue()));
    }

    public String _json(String merchantCode, String terminalCode, String merchantOrderId) {
        JSONObject json = new JSONObject();
        json.put("merchantCode", merchantCode);
        json.put("terminalCode", terminalCode);
        json.put("merchantOrderId", merchantOrderId);

        return json.toString();
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
        return super.constructMessage(ret, request, retJson, session)
                .setNext(retJson.getString("queryResCode"))
                .setNext(retJson.getString("queryResDesc"));
    }

    @Override
    protected String posType() {
        return "query";
    }
}
