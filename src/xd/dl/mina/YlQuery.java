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
        JSONObject json = new JSONObject();
        json.put("merchantCode", merchantCode);
        json.put("terminalCode", getTidBySession(session));
        json.put("merchantOrderId", request.getValue());

        return json.toString();
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return super.constructMessage(ret, request, retJson)
                .setNext(retJson.getString("queryResCode"))
                .setNext(retJson.getString("queryResDesc"));
    }

    @Override
    protected String posType() {
        return "pay";
    }
}
