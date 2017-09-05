package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class WatchProcess extends SendRequest {
    @Value("${watch_address}")
    String watchAddress;

    public String[][] constructParams(TLVMessage request) throws Exception {
        return new String[][]{
                {"watchId", (String) request.getValue()},
                {"parkId", parkId},
                {"carNumber", (String) request.getNextValue(0)},
                {"startTime", convertDate(request.getNextValue(1))},
                {"outTime", convertDate(request.getNextValue(2))},
                {"price", String.valueOf(request.getNextValue(3))}
        };
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(getJson(retJson,"code", -1)
        ).setNext(getJson(retJson,"errorMsg","")).setNext(request.getNextValue(0)
        ).setNext(request.getNextValue(3));
    }

    @Override
    public String svrAddress() {
        return watchAddress;
    }

    @Override
    protected boolean run() {
        return centerFlag;
    }
}