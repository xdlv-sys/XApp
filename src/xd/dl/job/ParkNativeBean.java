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
    
    public ParkedCarInfoJy getParkedCarInfoJy(String pOrderNo, int iCarType, String pLicense, int iFreeTime, int iMatchMethod, int iCarOrder,
    		String pSearchID, String pSearchInTime) {
    	return !proxyTest ? ParkNative.getParkedCarInfoJy(pOrderNo, iCarType
    			, pLicense, iFreeTime, iMatchMethod, iCarOrder, pSearchID, pSearchInTime) :
    				devParkNative.getParkedCarInfoJy(pOrderNo, iCarType
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
    
    public int payParkCarFeeJy(int iPayMethod, int iPayWay, String pLicense, String sShopNo, String pPaySeq, 
			int iDayTime, int iNightTime, float fDayMny, float fNightMny, String sOrderTime, String pPayTime) {
    	return !proxyTest ? ParkNative.payParkCarFeeJy("", 0, pLicense, pPayTime, 0, "", "", iPayMethod, pPaySeq, "", "", "", iPayWay, 0, 0, sShopNo, iDayTime, iNightTime, fDayMny, fNightMny, sOrderTime) :
    				devParkNative.payParkCarFeeJy("", 0, pLicense, pPayTime, 0, "", "", iPayMethod, pPaySeq, "", "", "", iPayWay, 0, 0, sShopNo, iDayTime, iNightTime, fDayMny, fNightMny, sOrderTime);
    }

    /**
     * 2018-09-13 新增会员优惠支付的记录
     * @author jinyi
     *
     */
    public ParkedCarInfo dispatchShopCoupon(String sOrderNo, String sCarLicense, String sCouID, String sCouName, int iCouType, 
			int iCouTime, float fCouMny,String sStartDate, String sEndDate,String sCarportID, String sInID,
			String sCardNo, int iCardType, int iDisType, int iUseType) {
		return !proxyTest ? ParkNative.dispatchShopCoupon(sOrderNo, sCarLicense, sCouID, sCouName, iCouType, iCouTime, fCouMny, sStartDate, sEndDate, sCarportID, sInID, sCardNo, iCardType, iDisType, iUseType):
		      devParkNative.dispatchShopCoupon(sOrderNo, sCarLicense, sCouID, sCouName, iCouType, iCouTime, fCouMny, sStartDate, sEndDate, sCarportID, sInID, sCardNo, iCardType, iDisType, iUseType);
	}
    
    //金鹰酒店checkin
    public int checkInJy(String parkId, String carNo, String roomNo, int discountTime, String checkInTime) {
    	return !proxyTest ? ParkNative.checkIn("", carNo, roomNo, "", discountTime, checkInTime, parkId, "") :
    				devParkNative.checkInJy("", carNo, roomNo, "", discountTime, checkInTime, parkId, "");
    }
    //金鹰酒店checkout
    public int checkOutJy(String parkId, String carNo, String roomNo, int discountTime, String checkInTime, String checkOutTime) {
    	return !proxyTest ? ParkNative.checkOut("", carNo, roomNo, "", checkInTime, checkOutTime, discountTime, parkId, "") :
    		devParkNative.checkOutJy("", carNo, roomNo, "", checkInTime, checkOutTime, discountTime, parkId, "");
    }
    

    class DevParkNative {
        public ParkedCarInfo getParkedCarInfo(String pOrderNo, int iCarType, String pLicense
                , int iFreeTime, int iMatchMethod, int iCarOrder, String pSearchID, String pSearchInTime) {
            ParkedCarInfo parkedCarInfo = new ParkedCarInfo();
            parkedCarInfo.fMoney = -1f;
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
        public ParkedCarInfoJy getParkedCarInfoJy(String pOrderNo, int iCarType, String pLicense
        		, int iFreeTime, int iMatchMethod, int iCarOrder, String pSearchID, String pSearchInTime) {
        	ParkedCarInfoJy parkedCarInfoJy = new ParkedCarInfoJy();
        	parkedCarInfoJy.fMoney = -1f;
        	parkedCarInfoJy.iParkedTime = 15;
        	parkedCarInfoJy.iReturn = 0;
        	parkedCarInfoJy.sCarLicense = "苏A12345";
        	parkedCarInfoJy.sEndTime = FwUtil.sdf.format(new Date());
        	parkedCarInfoJy.sStartTime = FwUtil.sdf.format(new Date(System.currentTimeMillis() - 3600 * 1000));
        	parkedCarInfoJy.sInTime = FwUtil.sdf.format(new Date(System.currentTimeMillis() - 3600 * 1000));
        	parkedCarInfoJy.sID = "123";
        	parkedCarInfoJy.sInPic = "1.jpg";
        	
        	parkedCarInfoJy.sShopNo = "002";//金鹰门店编码
        	parkedCarInfoJy.sPosNo = "0";//车辆位置编码
        	parkedCarInfoJy.iDayTime = 30;//剩余白天停车时长，单位分钟
        	parkedCarInfoJy.iNightTime = 30;//剩余夜间停车时长，单位分钟
        	parkedCarInfoJy.fDayMny = 1;//剩余白天停车金额,单位元
        	parkedCarInfoJy.fNightMny = 1;//剩余夜间停车金额,单位元
        	
        	return parkedCarInfoJy;
        }
        
        public ParkedCarInfo dispatchShopCoupon(String sOrderNo, String sCarLicense, String sCouID, String sCouName, int iCouType, 
				int iCouTime, float fCouMny,String sStartDate, String sEndDate,String sCarportID, String sInID,
				String sCardNo, int iCardType, int iDisType, int iUseType){
        	ParkedCarInfo parkedCarInfo = new ParkedCarInfo();
            parkedCarInfo.fMoney = -1f;
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
        
        int payParkCarFeeJy(String pOrderNo, int iCarType, String pCarLicese, String pPayTime, float fMoney, String pSearchID,
                String pSearchInTime, int iPayMethod, String pPaySeq, String pInTime, String pStartTime, String pEndTime,
                int iPayWay, float fPayFee, float fDiscount, String sShopNo,
    			int iDayTime, int iNightTime, float fDayMny, float fNightMny, String sOrderTime) {
        	return 0;
        }
        
        int checkInJy(String pOrderNo, String pLicense, String pRoomNo, String pName, int iCouTime, String pChkInTime, String pCarportID, String pInID) {
        	return 0;
        }
        int checkOutJy(String pOrderNo, String pLicense, String pRoomNo, String pName, String pChkInTime, String pChkOutTime, int iOutCouTime, String pCarportID, String pInID) {
        	return 0;
        }
        
    }
}
