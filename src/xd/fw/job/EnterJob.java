package xd.fw.job;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.bean.EnterOrOutRecord;
import xd.fw.service.ParkService;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EnterJob extends BaseJob {

    @Autowired
    ParkService parkService;

    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String parkingNo;
    final String CODE = "fsAsf2015";
    String serverHostName = "wap.dh-etc.com";

    protected List<EnterOrOutRecord> getRecords(){
        return parkService.geUploadRecords(0);
    }
    protected String[][] contructParams(String timeStamp,EnterOrOutRecord enterOrOutRecord)throws Exception{
        return new String[][]{
                {"Parkingno", parkingNo},
                {"Carnumber", enterOrOutRecord.getCarNumber()},
                {"Time1", sdf.format(enterOrOutRecord.getEnterTime())},
                {"Timestamp", timeStamp},
                {"Token", md5(enterOrOutRecord.getCarNumber(), timeStamp, parkingNo, CODE)}
        };
    }
    protected String svrAddress(){
        return "http://" + getServerHostName() + "/mobile/index.php?act=user&op=approach";
        //return "http://localhost:8080/accept!accept.cmd";
    }
    protected boolean isSuccess(JSONObject tmp){
        return "true".equals(tmp.getString("state"));
    }

    protected String getTimeStamp(){
        return sdf.format(new Date());
    }

    @Override
    public void doExecute() throws Exception {
        logger.info("start to execute job");
        //get records of car-entering
        List<EnterOrOutRecord> records =getRecords();
        if (records == null || records.size() < 1) {
            return;
        }
        JSONObject tmp;
        String timeStamp;
        int retStatus = 1;
        String[][] params;
        for (EnterOrOutRecord enterOrOutRecord : records) {
            timeStamp = getTimeStamp();
            params = contructParams(timeStamp,enterOrOutRecord);
            logger.info(desc(params));

            tmp = JSONObject.fromObject(post(svrAddress(), params));
            logger.info("result:" + enterOrOutRecord.getOrderNum() + "=" + tmp);
            if (!isSuccess(tmp)){
                retStatus = -1;
            }
            parkService.updateRecordStatus(enterOrOutRecord.getOrderNum(),retStatus,tmp.getString("msg"));
        }
    }

    public static String md5(String... strings) throws Exception {
        StringBuffer buffer = new StringBuffer();
        for (String string : strings) {
            buffer.append(string);
        }
        return FwUtil.md5(buffer.toString());
    }

    public JSONObject post(String address, String[][] params){
        JSONObject jsonObject;
        String json = null;
        try {
            json = HttpClientTpl.post(address, params);
            jsonObject = JSONObject.fromObject(json);
        } catch (Exception e) {
            logger.error("error json:" + json ,e);
            jsonObject = new JSONObject();
            jsonObject.put("state",false);
            jsonObject.put("msg","网络出现问题");
        }
        return jsonObject;
    }

    public void setParkingNo(String parkingNo) {
        this.parkingNo = parkingNo;
    }

    public ParkService getParkService() {
        return parkService;
    }

    protected String desc(String[][] arrays){
        StringBuffer buffer = new StringBuffer();
        for (String[] strs : arrays){
            buffer.append(strs[0]).append("=").append(strs[1]).append("\n");
        }
        return buffer.toString();
    }

    public String getServerHostName() {
        return serverHostName;
    }

    public void setServerHostName(String serverHostName) {
        this.serverHostName = serverHostName;
    }

    public static void main(String[] args) throws Exception {

    }
}