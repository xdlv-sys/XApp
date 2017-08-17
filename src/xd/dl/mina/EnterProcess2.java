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
public class EnterProcess2 extends BaseEnterOutProcess {

    @Override
    public String[][] constructParams(TLVMessage request) throws Exception {
        String[][] superParams = super.constructParams(request);
        if (superParams != null){
            return superParams;
        }

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
    int accessType() {
        return 0;
    }
}
