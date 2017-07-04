package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class AutoPay extends SendRequest{

    public String[][] constructParams(TLVMessage request) throws Exception {
        return new String[][]{
                {"orderNo", (String)request.getNextValue(3)},
                {"parkingNo", parkingNo},
                {"carNumber",(String) request.getValue()},
                {"enterTime",(String)request.getNextValue(1)},
                {"outTime", (String)request.getNextValue(2)},
                {"payFee", String.valueOf(100 * (float)request.getNextValue(2))}
        };
    }
    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(getJson(retJson,"state", -1))
                .setNext(getJson(retJson,"msg",""))
                .setNext(request.getValue())
                .setNext(request.getNextValue(2))
                .setNext(request.getNextValue(3))
                .setNext(getJson(retJson,"Member_code",""))
                .setNext(getJson(retJson,"Is_auto_leave", 0));
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/pay/autoPay";
    }
}