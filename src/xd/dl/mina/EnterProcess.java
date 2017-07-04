package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class EnterProcess extends SendRequest{

    @Override
    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String)request.getValue();
        String timeStamp = (String)request.getNextValue(0);
        int color = (int)request.getNextValue(1);
        String deviceNo = (String)request.getNextValue(2);
        String orderNo = (String)request.getNextValue(3);

        return new String[][]{
                {"parkingNo", parkingNo},
                {"orderNo", orderNo},
                {"carNumber", carNumber},
                {"carPlateColorType",String.valueOf(color)},
                {"enterTime", timeStamp},
                {"deviceCode",deviceNo}
        };
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(getJson(retJson,"stauts", -1))
                .setNext(getJson(retJson,"msg",""))
                .setNext(request.getValue())
                .setNext(((float)getJson(retJson,"memberBalance",-1000000.11))/100f)
                .setNext(request.getNextValue(3))
                .setNext(getJson(retJson,"Member_code",""))
                .setNext(getJson(retJson,"Is_auto_leave", 0));
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/order/add";
    }
}
