package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class ChargeStandard extends SendRequest{

    public String[][] constructParams(TLVMessage request) throws Exception {
        return new String[][]{
                {"parkingNo", parkingNo},
                {"parkFeeInfo",(String) request.getValue()}
        };
    }
    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(getJson(retJson,"state", -1))
                .setNext(getJson(retJson,"msg",""));
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/pay/autoPay";
    }
}