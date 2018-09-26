package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.mina.tlv.TLVMessage;

import static xd.fw.mina.tlv.IMinaConst.ID_KEY;

public abstract class BaseYlRequest extends SendRequest {

    @Value("${pos_url}")
    String posUrl;

    @Value("${app_id}")
    String appId;
    @Value("${app_key}")
    String appKey;
    @Value("${merchant_code}")
    String merchantCode;

    @Autowired
    YlPosToken ylPosToken;

    @Override
    String[][] constructParams(TLVMessage request) {
        return null;
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(retJson.getString("errCode")).setNext(retJson.getString("errInfo"));
    }

    @Override
    String svrAddress() {
        return posUrl + "/" + posType();
    }

    @Override
    protected String[][] heads() {
        return new String[][] { {"Authorization", "OPEN-ACCESS-TOKEN AccessToken=\""
                + ylPosToken.getAccessToken() + "\""} };
    }

    protected abstract String posType();

    @Override
    protected boolean run() {
        return true;
    }

    protected final String getTidBySession(IoSession session) {
        return ylPosToken.getTidBySessionId((String)session.getAttribute(ID_KEY));
    }
}
