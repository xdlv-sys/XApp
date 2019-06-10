package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.job.IDongHui;
import xd.fw.MD5;
import xd.fw.mina.tlv.TLVMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

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

    @Value("${cmb_flag:false}")
    boolean cmbFlag;
    @Value("${cmb_url}")
    String cmbUrl;

    @Value("${cmb_app_id}")
    String cmbAppId;
    @Value("${cmb_app_appsecret}")
    String cmbAppSecret;

    abstract String[][] constructParams(TLVMessage request) throws Exception;

    abstract String svrAddress();

    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session){
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
    protected String json(TLVMessage request, IoSession session){
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

    String[][] cmbSign(String[][] params) {
        Arrays.sort(params, Comparator.comparing(t -> t[0]));
        StringBuilder sb = new StringBuilder();
        Arrays.stream(params).forEach(p -> {
            if (StringUtils.isNotBlank(p[1])) {
                sb.append(p[0]).append("=").append(p[1]).append("&");
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(cmbAppSecret);

        Arrays.stream(params).forEach(p -> {
            if (p[0].equals("sign")) {
                p[1] = MD5.MD5Encode(sb.toString());
            }
        });

        /*JSONObject ret = new JSONObject();
        Arrays.stream(params).forEach(p -> {
            if (p[0].equals("sign")) {
                p[1] = MD5.MD5Encode(sb.toString());
            }
            ret.put(p[0], p[1]);
        });

        return ret.toString();*/
        return params;
    }
}
