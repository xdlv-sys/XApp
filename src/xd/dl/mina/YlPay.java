package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.HttpClientTpl;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class YlPay extends BaseYlRequest{

    @Value("${yl_scan_wait_times:5}")
    int ylScanWaitTimes;

    @Autowired
    YlQuery ylQuery;

    @Override
    protected String json(TLVMessage request, IoSession session) {
        // orderId -> carNumber -> price -> payCode
        int random = (int)(999 * Math.random());
        request.append(String.valueOf(random));

        JSONObject json = new JSONObject();
        json.put("merchantCode", merchantCode);
        json.put("terminalCode", getTidBySession(session));
        json.put("transactionCurrencyCode", "156");
        json.put("merchantOrderId", String.valueOf(request.getValue()) + random);
        json.put("merchantRemark", request.getNextValue(0));
        json.put("transactionAmount", ((float)request.getNextValue(1)) * 100);
        json.put("payCode", request.getNextValue(2));
        json.put("payMode", "CODE_SCAN");

        return json.toString();
    }

    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
        // 5 seconds to wait the order
        JSONObject httpRetJson = null;
        String ylOrderId = String.valueOf(request.getValue()) + request.getTail().getValue();
        boolean success = false;
        for (int i = 0; i< ylScanWaitTimes; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger().error("yl scan query wait error", e);
            }
            // query yl scan result _json(merchantCode, getTidBySession(session), String.valueOf(request.getValue()));
            String json = ylQuery._json(merchantCode, getTidBySession(session), ylOrderId);
            try {
                httpRetJson = JSONObject.fromObject(HttpClientTpl.postJson(ylQuery.svrAddress(), json, heads()));
                if (YL_SCAN_SUCCESS.equals(httpRetJson.getString("errCode"))
                        && "0".equals(httpRetJson.getString("queryResCode"))) {
                    success = true;
                    break;
                }
            } catch (Exception e) {
                logger().error("yl scan query result error", e);
            }
        }


        return ret.setNext(success ? 0 : 1)
                .setNext(httpRetJson == null ? "FAIL" : httpRetJson.getString("errInfo"))
                // orderId carNumber price
                .setNext(request.getValue())
                .setNext(request.getNextValue(0))
                .setNext(request.getNextValue(1))
                .setNext(ylOrderId);
    }

    @Override
    protected String posType() {
        return "pay";
    }
}
