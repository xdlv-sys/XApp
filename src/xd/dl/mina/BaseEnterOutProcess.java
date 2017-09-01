package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.mina.tlv.TLVMessage;

public class BaseEnterOutProcess extends SendRequest {

    @Value("${enter_address}")
    String enterAddress;

    @Value("${sq_scan:false}")
    protected boolean sq;
    @Value("${sq_scan_address}")
    protected String sqScanAddress;

    @Value("${sq_division_no}")
    protected String sqDivisionNo;
    @Value("${sq_gate_no}")
    protected String sqGateNo;

    private String json;
    protected static String token;

    @Override
    String[][] constructParams(TLVMessage request) throws Exception {
        if (sq){
            JSONObject body = new JSONObject();
            body.put("divisionNo",sqDivisionNo);
            body.put("gateNo",sqGateNo);
            body.put("vehicleNo",request.getValue());
            body.put("accessType", accessType());
            body.put("scanTime",convertDate(request.getNextValue(accessType() == 0 ? 0 : 3)));
            json = body.toString();
            return new String[0][];
        }
        return null;
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(getJson(retJson,"code", -1)
        ).setNext(getJson(retJson,"errorMsg","")).setNext(request.getValue()
        ).setNext(request.getNextValue(3) == null ? 0f : request.getNextValue(3));
    }

    @Override
    protected String json() {
        return json;
    }
    @Override
    public String token() {
        return token;
    }

    @Override
    String svrAddress() {
        if (sq) {
            return sqScanAddress;
        }
        return enterAddress;
    }

    int accessType(){
        return 0;
    }

    public static void main(String[] args){
        JSONObject root = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("divisionNo","divisionNo");
        jsonObj.put("sqGateNo","");
        jsonObj.put("vehicleNo","");
        jsonObj.put("accessType", 1);
        jsonObj.put("scanTime","");
        root.put("body",  jsonObj);
        System.out.println(root.toString());
    }
}
