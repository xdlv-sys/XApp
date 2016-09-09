package xd.fw.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.job.IDongHui;
import xd.fw.job.ParkNative;
import xd.fw.mina.ParkHandler;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotifyOrderAction extends BaseAction implements IDongHui{
    Logger logger = LoggerFactory.getLogger(NotifyOrderAction.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ParkNative parkNative;

    @Autowired
    ParkHandler parkHandler;

    //
    String parkingno;
    float parkingPrice;
    String carnumber;
    int carPlateColorType;
    String timestamp;
    String token;
    String currenttime;

    int code;//	int	返回编码
    String msg;//	String	备注

    @Value("${order_success}")
    String success;
    @Value("${order_fail}")
    String fail;

    @Override
    public void validate() {
        if (StringUtils.isBlank(carnumber)){
            throw new IllegalArgumentException("carnumberis empty");
        }
        try {
            carnumber = new String(carnumber.getBytes("iso-8859-1"), "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            if (token == null || !token.equals(md5(carnumber, timestamp,parkingno))){
                throw new IllegalArgumentException("token is wrong");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("",e);
        }

    }

    @Action("QueryFee")
    public String queryFee(){
        msg = fail;
        code = 201;
        boolean ret = parkNative.payFee(carnumber, sdf2.format(new Date()),parkingPrice);
        if (ret){
            code = 200;
            msg = success;

            //notify all watch house
            parkHandler.notifyWatchIdPayFee(carnumber, parkingPrice);
        }
        return SUCCESS;
    }

    public void setParkingno(String parkingno) {
        this.parkingno = parkingno;
    }

    public void setParkingPrice(float parkingPrice) {
        this.parkingPrice = parkingPrice;
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

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }
}
