package xd.dl.mina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.mina.tlv.TLVMessage;

public abstract class BaseYlRequest extends SendRequest {

    @Value("${pos_url}")
    String posUrl;

    @Value("${app_id}")
    String appId;
    @Value("${app_key}")
    String appKey;
    @Value("${merchant_code}")
    String merchantCode;
    @Value("${terminal_code}")
    String terminalCode;

    @Autowired
    YlPosToken ylPosToken;

    @Override
    String[][] constructParams(TLVMessage request) throws Exception {
        return null;
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
}
