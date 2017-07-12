package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 上报云平台，用于推送消息
 */
@Service
public class EnterProcess2 extends SendRequest {
    @Value("${enter_address}")
    String enterAddress;
    @Value("${park_id}")
    protected String parkId;

    @Override
    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String) request.getValue();
        String enterTime = (String) request.getNextValue(0);
        return new String[][]{
                {"parkId", parkId},
                {"carNumber", carNumber},
                //2017-05-22 164708
                {"enterTime", transferDate(enterTime)}
        };
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson) {
        return ret.setNext(getJson(retJson,"code", -1)
        ).setNext(getJson(retJson,"errorMsg","")).setNext(request.getValue()
        ).setNext(request.getNextValue(3) == null ? 0f : request.getNextValue(3));
    }

    @Override
    public String svrAddress() {
        return enterAddress;
    }

    protected String transferDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new SimpleDateFormat("yyyyMMddHHmmss").parse(date));
    }
}
