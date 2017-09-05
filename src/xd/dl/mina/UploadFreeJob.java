package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

/**
 * Created by xd on 7/4/2017.
 *
 */
@Service
public class UploadFreeJob extends SendRequest{

    @Override
    String[][] constructParams(TLVMessage request) throws Exception {
        return new String[][]{
                {"parkingNo", parkingNo},
                {"freeSlotNum",String.valueOf(request.getNextValue(0))}
        };
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        int retInt = Integer.parseInt(String.valueOf(getJson(retJson,"state", -1)));
        return ret.setNext(retInt).setNext(
                retInt == 200 ? request.getValue() : getJson(retJson,"msg","error"));
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/askPark/uploadFreeSlotNum";
    }

    @Override
    protected boolean run() {
        return dongHuiFlag;
    }
}
