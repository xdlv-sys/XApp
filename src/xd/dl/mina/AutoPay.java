package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class AutoPay extends SendRequest{

    public String[][] constructParams(TLVMessage request) throws Exception {
        //8->苏A12388->0.1->20170705142329->20170705144442->N1502000000103170705144439085615
        return new String[][]{
                {"orderNo", (String)request.getNextValue(3)},
                {"parkingNo", parkingNo},
                {"carNumber",(String) request.getValue()},
                {"enterTime",convertDate(request.getNextValue(1))},
                {"outTime", convertDate(request.getNextValue(2))},
                {"payFee", String.valueOf(100 * (float)request.getNextValue(0))}
        };
    }
    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
        return super.constructMessage(ret,request,retJson, session)
                .setNext(getJson(retJson,"msg",""))
                .setNext(request.getValue())
                .setNext(request.getNextValue(0))
                .setNext(request.getNextValue(3))
                .setNext(getJson(retJson,"Member_code",""))
                .setNext(getJson(retJson,"Is_auto_leave", 0));
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/pay/autoPay";
    }
}