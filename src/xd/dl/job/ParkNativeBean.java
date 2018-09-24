package xd.dl.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class ParkNativeBean {
    @Value("${proxy-test:false}")
    boolean proxyTest;
    @Value("${proxy-test-money:0.01f}")
    float testMoney;

    Logger logger = LoggerFactory.getLogger(ParkNativeBean.class);

    DevParkNative devParkNative = new DevParkNative();
    @PostConstruct
    void init() {
        logger.info("proxy test: {}", proxyTest);
        if (!proxyTest) {
            System.loadLibrary("ParkingMeter");
        }
    }

    public ParkedCarInfo getParkedCarInfo(String pOrderNo, int iCarType, String pLicense, int iFreeTime, int iMatchMethod, int iCarOrder,
                                          String pSearchID, String pSearchInTime) {
        return !proxyTest ? ParkNative.getParkedCarInfo(pOrderNo, iCarType
                , pLicense, iFreeTime, iMatchMethod, iCarOrder, pSearchID, pSearchInTime) :
                devParkNative.getParkedCarInfo(pOrderNo, iCarType
                        , pLicense, iFreeTime, iMatchMethod, iCarOrder, pSearchID, pSearchInTime);
    }

    public int payParkCarFee(String pOrderNo, int iCarType, String pCarLicese, String pPayTime, float fMoney, String pSearchID,
                      String pSearchInTime, int iPayMethod, String pPaySeq, String pInTime, String pStartTime, String pEndTime,
                      int iPayWay, float fPayFee, float fDiscount) {
        return !proxyTest ? ParkNative.payParkCarFee(pOrderNo, iCarType
                , pCarLicese, pPayTime, fMoney, pSearchID, pSearchInTime
                , iPayMethod, pPaySeq, pInTime, pStartTime, pEndTime, iPayWay, fPayFee, fDiscount) :
                devParkNative.payParkCarFee(pOrderNo, iCarType
                        , pCarLicese, pPayTime, fMoney, pSearchID, pSearchInTime
                        , iPayMethod, pPaySeq, pInTime, pStartTime, pEndTime, iPayWay, fPayFee, fDiscount);
    }


    class DevParkNative {
        public ParkedCarInfo getParkedCarInfo(String pOrderNo, int iCarType, String pLicense
                , int iFreeTime, int iMatchMethod, int iCarOrder, String pSearchID, String pSearchInTime) {
            ParkedCarInfo parkedCarInfo = new ParkedCarInfo();
            parkedCarInfo.fMoney = testMoney;
            parkedCarInfo.iParkedTime = 15;
            parkedCarInfo.iReturn = 0;
            parkedCarInfo.sCarLicense = "苏A12345";
            parkedCarInfo.sEndTime = FwUtil.sdf.format(new Date());
            parkedCarInfo.sStartTime = FwUtil.sdf.format(new Date(System.currentTimeMillis() - 3600 * 1000));
            parkedCarInfo.sInTime = FwUtil.sdf.format(new Date(System.currentTimeMillis() - 3600 * 1000));
            parkedCarInfo.sID = "123";
            parkedCarInfo.sInPic = "1.jpg";
            return parkedCarInfo;
        }

        int payParkCarFee(String pOrderNo, int iCarType, String pCarLicese, String pPayTime, float fMoney, String pSearchID,
                          String pSearchInTime, int iPayMethod, String pPaySeq, String pInTime, String pStartTime, String pEndTime,
                          int iPayWay, float fPayFee, float fDiscount) {
            return 0;
        }
    }
}
