package xd.dl.mina;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class CarOutProcess extends SendRequest {
    @Value("${out_address}")
    String outAddress;

    public String[][] constructParams(TLVMessage request) {
        return null;
    }

    @Override
    protected String json(TLVMessage request, IoSession session) {
        //>苏A12388->20180911085135->20180911091302->0001->N1502000000101180911091300897253->TEST1->1->->20180911085135->20180911091302->1->0.1->0.1->0.0->0.0
        int itemCount = request.getNextInt(5);
        JSONObject ret = new JSONObject();

        JSONArray itemArray = new JSONArray();
        int itemStart = 6;
        for (int i = 0; i< itemCount; i++ ) {
            JSONObject item = new JSONObject();
            _putJson(item,new String[] {"orderId", request.getNextString(itemStart ++)});
            _putJson(item,new String[] {"startTime", convertDate(request.getNextString(itemStart ++))});
            _putJson(item,new String[] {"endTime", convertDate(request.getNextString(itemStart ++))});
            _putJson(item,new String[] {"payType", request.getNextString(itemStart ++)});
            _putJson(item,new String[] {"settleFee", request.getNextString(itemStart ++)});
            _putJson(item,new String[] {"fee", request.getNextString(itemStart ++)});
            _putJson(item,new String[] {"cashOff", request.getNextString(itemStart ++)});
            _putJson(item,new String[] {"sysOff", request.getNextString(itemStart ++)});
            itemArray.add(item);
        }
        ret.put("items", itemArray);

        _putJson(ret,new String[] {"parkId", parkId});
        _putJson(ret,new String[] {"carNumber", (String)request.getValue()});
        _putJson(ret,new String[] {"enterTime", convertDate(request.getNextString(0))});
        _putJson(ret,new String[] {"outTime", convertDate(request.getNextString(1))});
        _putJson(ret,new String[] {"deviceId", request.getNextString(2)});
        _putJson(ret,new String[] {"orderId", request.getNextString(3)});
        _putJson(ret,new String[]  {"operator", request.getNextString(4)});
        return ret.toString();
    }

    void _putJson(JSONObject json, String[] v) {
        json.put(v[0], v[1]);
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(getJson(retJson,"code", -1)).setNext(
                getJson(retJson,"errorMsg","")).setNext(request.getValue())
                .setNext(request.getNextValue(0)).setNext(request.getNextString(3));
    }

    @Override
    public String svrAddress() {
        return outAddress;
    }

    @Override
    protected boolean run() {
        return centerFlag;
    }
}
