package xd.dl.mina;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.job.IDongHui;
import xd.fw.mina.tlv.TLVMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;

abstract class SendRequest implements IDongHui{

    @Value("${park_no}")
    String parkingNo;
    @Value("${park_id}")
    protected String parkId;

    @Value("${unique_key}")
    String uniqueKey;
    @Value("${dh_host}")
    String dhHost;
    @Value("${dh_flag}")
    boolean dongHuiFlag;
    @Value("${center_flg}")
    boolean centerFlag;

    abstract String[][] constructParams(TLVMessage request) throws Exception;

    abstract String svrAddress();

    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson){
        return ret.setNext(Integer.parseInt(String.valueOf(getJson(retJson,"state", -1))));
    }
    Object getJson(JSONObject jsonObject, String key, Object dValue){
        return jsonObject.has(key) ? jsonObject.get(key) : dValue;
    }
    static String convertDate(Object str){
        if (str == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return sdf.format(sdf2.parse((String)str));
        } catch (ParseException e) {
            //logger().error("",e);
            return null;
        }
    }
    protected String json(TLVMessage request){
        return null;
    }
    protected String token(){
        return null;
    }

    protected String[][] heads() {
        return null;
    }

    protected boolean run() {
        return dongHuiFlag;
    }
}