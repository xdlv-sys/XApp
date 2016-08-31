package xd.fw.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.FwUtil;
import xd.fw.job.ParkNative;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderAction extends BaseAction {
    Logger logger = LoggerFactory.getLogger(OrderAction.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ParkNative parkNative;

    String parkingno;
    String carnumber;
    int carPlateColorType;
    String timestamp;
    String token;

    int code;//	int	返回编码
    String msg;//	String	备注
    float parkingPrice;//	Float	停车费用
    String starttime;	//timestamp	停车开始时间
    String currenttime;	//timestamp	停车场当前时间
    String parkingTime;	//String

    @Value("${order_success}")
    String success;
    @Value("${order_fail}")
    String fail;

    @Override
    public void validate() {
        if (StringUtils.isBlank(carnumber)){
            addFieldError("carnumber", "is empty");
        }
    }

    @Action("QueryOrder")
    public String queryOrder(){
        logger.info("query order {}", carnumber);
        ParkNative.ParkedInfo parkedInfo = parkNative.getParkedInfo(carPlateColorType == 2 ? 1 : 0, carnumber);

        msg = fail;
        code = 201;

        if (parkedInfo != null && parkedInfo.iReturn == 0){
            code = 200;
            msg = success;
            parkingPrice = parkedInfo.fMoney;
            starttime = parkedInfo.sInTime.replaceAll("-",""
            ).replaceAll(":","").replaceAll(" ","");
            currenttime = sdf.format(new Date());
            parkingTime = parkedInfo.iParkedTime / 60 + ":" +  parkedInfo.iParkedTime % 60;
        }
        return SUCCESS;
    }

    @Action("QueryFee")
    public String queryFee(){
        msg = fail;
        code = 201;
        boolean ret = parkNative.payFee(carnumber, sdf2.format(new Date()),parkingPrice);
        if (ret){
            code = 200;
            msg = success;
        }
        return SUCCESS;
    }

    public void setParkingno(String parkingno) {
        this.parkingno = parkingno;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public void setCarPlateColorType(int carPlateColorType) {
        this.carPlateColorType = carPlateColorType;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public float getParkingPrice() {
        return parkingPrice;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public String getParkingTime() {
        return parkingTime;
    }
}
