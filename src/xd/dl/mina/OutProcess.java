package xd.dl.mina;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class OutProcess extends SendRequest{

    public String[][] constructParams(TLVMessage request) throws Exception {
        int start = 4;
        int payListTime = (int)request.getNextValue(start);
        JSONArray payList = new JSONArray();

        for (int i =0;i<payListTime;i++){
            JSONObject pay = new JSONObject();
            pay.put("paySeq", request.getNextValue(++start));
            pay.put("payStartTime", request.getNextValue(++start));
            pay.put("payEndTime", request.getNextValue(++start));
            pay.put("payWay", request.getNextValue(++start));
            pay.put("settlementFee", 100 * (int)request.getNextValue(++start));
            pay.put("payAmount", 100 * (int)request.getNextValue(++start));
            payList.add(pay);
        }
        return new String[][]{
                {"parkNo", parkingNo},
                {"orderNo", (String)request.getNextValue(3)},
                {"carNumber", (String)request.getValue()},
                {"enterTime", (String)request.getNextValue(0)},
                {"outTime", (String)request.getNextValue(1)},
                {"device_no",(String)request.getNextValue(2)},
                {"payList", payList.toString()}
        };
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(getJson(retJson,"stauts", -1))
                .setNext(getJson(retJson,"msg",""))
                .setNext(request.getValue())
                .setNext(request.getNextValue(0))
                .setNext(request.getNextValue(3));
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/order/completeUpload";
    }
}