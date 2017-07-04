package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.job.IDongHui;
import xd.fw.mina.tlv.TLVMessage;

abstract class SendRequest implements IDongHui{

    @Value("${park_no}")
    String parkingNo;

    @Value("${unique_key}")
    String uniqueKey;
    @Value("${dh_host}")
    String dhHost;

    abstract String[][] constructParams(TLVMessage request) throws Exception;

    abstract String svrAddress();

    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson){
        return ret;
    }
    Object getJson(JSONObject jsonObject, String key, Object dValue){
        return jsonObject.has(key) ? jsonObject.get(key) : dValue;
    }
}