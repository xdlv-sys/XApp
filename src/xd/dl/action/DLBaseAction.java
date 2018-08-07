package xd.dl.action;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.action.BaseAction;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by xd on 6/21/2017.
 * base class for pay order action
 */
public class DLBaseAction extends BaseAction{
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${success}")
    String success;
    @Value("${fail}")
    String fail;
    @Value("${park_no}")
    String parkNo;

    @Value("${dh_flag}")
    boolean dhFlag = false;
    @Value("${db_load}")
    boolean dbLoad = false;

    String state, msg, carNumber = "", carNo = "", parkingNo = "";

    JSONObject result;
    @PostConstruct
    public void init(){
        state = "200";
        msg = success;
    }

    @Override
    public void validate() {
        carNo = convertUtf8(carNo);
        carNumber = convertUtf8(carNumber);
        if (!dhFlag) {
            throw new RuntimeException("dh flag is false");
        }
        if (!dbLoad) {
            throw new RuntimeException("db not load");
        }
    }

    static String convertUtf8(String str){
        return _convertCharset(str, "iso-8859-1", "utf8");
    }
    static String _convertCharset(String str, String from, String to){
        if (StringUtils.isBlank(str)){
            return str;
        }
        try {
            return new String(str.getBytes(from), to);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public void setParkNo(String parkNo) {
        this.parkNo = parkNo;
    }

    public void setParkingNo(String parkingNo) {
        this.parkingNo = parkingNo;
    }

    public String getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject getResult() {
        return result;
    }

    void put(Object k, Object v){
        if (result == null){
            result = new JSONObject();
        }
        result.put(k, v);
    }

    String convertTime(String s) throws ParseException {
        return sdf.format(sdf2.parse(s));
    }
    String convertTime2(String s) throws ParseException {
        return sdf2.format(sdf.parse(s));
    }
}
