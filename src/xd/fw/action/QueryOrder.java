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

public class QueryOrder extends BaseAction implements IDongHui {
    @Autowired
    ParkNative parkNative;

    @Autowired
    ParkHandler parkHandler;

    Logger logger = LoggerFactory.getLogger(NotifyOrderAction.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    String parkingno;
    String carnumber;
    int carPlateColorType;
    String timestamp;
    String token;

    int code;//	int	返回编码
    String msg;//	String	备注

    float parkingprice;
    String paystarttime, payendtime,duration,starttime;

    @Value("${order_success}")
    String success;
    @Value("${order_fail}")
    String fail;

    @Override
    public void validate() {
        if (StringUtils.isBlank(carnumber)) {
            throw new IllegalArgumentException("carnumberis empty");
        }
        try {
            carnumber = new String(carnumber.getBytes("iso-8859-1"), "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            if (token == null || !token.equals(md5(carnumber, timestamp, parkingno))) {
                throw new IllegalArgumentException("token is wrong");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("", e);
        }
    }

    @Action("QueryOrder")
    public String queryOrder() {
        logger.info("query order {}", carnumber);
        ParkNative.ParkedInfo parkedInfo = parkNative.getParkedInfo(carPlateColorType == 2 ? 1 : 0, carnumber,15);

        msg = fail;
        code = 201;

        if (parkedInfo != null && parkedInfo.iReturn == 0) {
            code = 200;
            msg = success;

            parkingprice = parkedInfo.fMoney;
            starttime = parkedInfo.sInTime.replaceAll("-", ""
            ).replaceAll(":", "").replaceAll(" ", "");
            paystarttime = starttime;
            payendtime = sdf.format(new Date());
            duration = parkedInfo.iParkedTime / 60 + ":" + parkedInfo.iParkedTime % 60;
            logger.info("price:{},duration:{}",parkingprice,duration);
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

    public float getParkingprice() {
        return parkingprice;
    }

    public String getPaystarttime() {
        return paystarttime;
    }

    public String getPayendtime() {
        return payendtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getDuration() {
        return duration;
    }
}
