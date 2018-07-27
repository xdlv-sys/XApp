package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarOutProcess extends SendRequest {
    @Value("${out_address}")
    String outAddress;

    public String[][] constructParams(TLVMessage request) {
        int itemCount = request.getNextInt(5);
        List<String[]> items = new ArrayList<>();
        int itemStart = 6;
        for (int i = 0; i< itemCount; i++ ) {
            items.add(new String[] {"items[" + i + "].orderId", request.getNextString(itemStart ++)});
            items.add(new String[] {"items[" + i + "].startTime", convertDate(request.getNextString(itemStart ++))});
            items.add(new String[] {"items[" + i + "].endTime", convertDate(request.getNextString(itemStart ++))});
            items.add(new String[] {"items[" + i + "].payType", request.getNextString(itemStart ++)});
            items.add(new String[] {"items[" + i + "].settleFee", request.getNextString(itemStart ++)});
            items.add(new String[] {"items[" + i + "].fee", request.getNextString(itemStart ++)});
            items.add(new String[] {"items[" + i + "].cashOff", request.getNextString(itemStart ++)});
            items.add(new String[] {"items[" + i + "].sysOff", request.getNextString(itemStart ++)});
        }
        items.add(new String[] {"parkId", parkId});
        items.add(new String[] {"carNumber", (String)request.getValue()});
        items.add(new String[] {"enterTime", convertDate(request.getNextString(0))});
        items.add(new String[] {"outTime", convertDate(request.getNextString(1))});
        items.add(new String[] {"deviceId", request.getNextString(2)});
        items.add(new String[] {"orderId", request.getNextString(3)});
        items.add(new String[]  {"operator", request.getNextString(4)});
        return items.toArray(new String[0][]);
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
