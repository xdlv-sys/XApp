package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.HttpClientTpl;
import xd.fw.mina.tlv.TLVMessage;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class WatchProcess extends SendRequest {
    @Value("${watch_address}")
    String watchAddress;

    public String[][] constructParams(TLVMessage request) throws Exception{
        if (cmbFlag) {
            // 招行进场接口
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String[][] params = {
                    {"appid", cmbAppId},
                    {"park_code", parkId},
                    {"in_code", "1"},
                    {"vpl_number", (String)request.getValue()},
                    {"record_id", "CMB" + request.getNextString(1)}, // startTime as order Id
                    {"in_time", sdf.parse(request.getNextString(1)).getTime() / 1000 + ""},
                    {"car_type", "1"},
                    {"plate_color", "0"},
                    {"sign", ""}
            };

            String cmbRet = HttpClientTpl.post(cmbUrl + "Indata", cmbSign(params));
            logger().info("cmb enter return: {}", cmbRet);
        }

        return new String[][]{
                {"watchId", (String) request.getValue()},
                {"parkId", parkId},
                {"carNumber", (String) request.getNextValue(0)},
                {"startTime", convertDate(request.getNextValue(1))},
                {"outTime", convertDate(request.getNextValue(2))},
                {"price", String.valueOf(request.getNextValue(3))}
        };
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
        return ret.setNext(getJson(retJson,"code", -1)
        ).setNext(getJson(retJson,"errorMsg","")).setNext(request.getNextValue(0)
        ).setNext(request.getNextValue(3));
    }

    @Override
    public String svrAddress() {
        return watchAddress;
    }

    @Override
    protected boolean run() {
        return centerFlag;
    }
}
