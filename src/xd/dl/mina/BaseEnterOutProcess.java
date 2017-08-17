package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.mina.tlv.TLVMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BaseEnterOutProcess extends SendRequest {

    @Value("${enter_address}")
    String enterAddress;
    @Value("${park_id}")
    protected String parkId;


    @Value("${sq_scan:false}")
    protected boolean sq;
    @Value("${sq_scan_address}")
    protected String sqScanAddress;

    @Value("${sq_division_no}")
    protected String sqDivisionNo;
    @Value("${sq_gate_no}")
    protected String sqGateNo;

    private String json;

    @Override
    String[][] constructParams(TLVMessage request) throws Exception {
        if (sq){
            JSONObject root = new JSONObject();
            JSONObject body = new JSONObject();
            body.put("divisionNo",sqDivisionNo);
            body.put("sqGateNo",sqGateNo);
            body.put("vehicleNo",request.getValue());
            body.put("accessType", accessType());
            body.put("scanTime",transferDate((String) request.getNextValue(0)));
            root.put("body", body);
            json = root.toString();
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
    String svrAddress() {
        if (sq) {
            return sqScanAddress;
        }
        return enterAddress;
    }

    int accessType(){
        return 0;
    }

    protected String transferDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new SimpleDateFormat("yyyyMMddHHmmss").parse(date));
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
