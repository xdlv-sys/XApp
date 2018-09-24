package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class PayYl extends BaseYlRequest{

    @Override
    protected String json(TLVMessage request) {
        // orderId -> carNumber -> price
        JSONObject json = new JSONObject();
        json.put("merchantCode", merchantCode);
        json.put("terminalCode", terminalCode);
        json.put("transactionCurrencyCode", "156");
        json.put("merchantOrderId", request.getValue());
        json.put("merchantRemark", request.getNextString(0));
        json.put("transactionAmount", request.getNextValue(1));
        json.put("payMode", "CODE_SCAN");
        json.put("payCode", "");

        return json.toString();
    }

    @Override
    protected String posType() {
        return "pay";
    }

    public static String sha1(String str){
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(StandardCharsets.UTF_8));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
