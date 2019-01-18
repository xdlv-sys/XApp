package xd.dl.job;
public class ParkNative
{
    public native static int initialized( String pDbIP,String pDbName,String pDbUserID,String pDbPwd );
    public native static int unitialized();
    public synchronized native static ParkedCarInfo getParkedCarInfo(String pOrderNo, int iCarType, String pLicense, int iFreeTime, int iMatchMethod, int iCarOrder, 
														String pSearchID, String pSearchInTime);
    public synchronized native static ParkedCarInfoJy getParkedCarInfoJy(String pOrderNo, int iCarType, String pLicense, int iFreeTime, int iMatchMethod, int iCarOrder, 
			String pSearchID, String pSearchInTime);
    public synchronized native static int payParkCarFee(String pOrderNo, int iCarType, String pCarLicese, String pPayTime, float fMoney, String pSearchID,
										   String pSearchInTime, int iPayMethod, String pPaySeq, String pInTime, String pStartTime, String pEndTime,
										   int iPayWay, float fPayFee, float fDiscount);
    public synchronized native static int payParkCarFeeJy(String pOrderNo, int iCarType, String pCarLicese, String pPayTime, float fMoney, String pSearchID,
            String pSearchInTime, int iPayMethod, String pPaySeq, String pInTime, String pStartTime, String pEndTime,
            int iPayWay, float fPayFee, float fDiscount, String sShopNo,
			int iDayTime, int iNightTime, float fDayMny, float fNightMny, String sOrderTime);
 	public synchronized native static LeftParkInfo[] getLeftParkInfo();
    public synchronized native static ViewCarportRoomInfo[] getCarportInfo(String pLicense, String pCarportID, int iLicenseColor, int iMethod);
    public synchronized native static int payCarportRent(String pCarLicense, String pRoomNum, String pCarportNum, String[] aCarportNum, String pPayTime, String pSerialNumber,
                                            int iMonthCnt, float fMoney, boolean bPayAllCarport, int iLicenseColor, int iPayWay, float fPayFee, float fDiscount, int iMethod);
    public synchronized native static int updateAutoPayInfo(String pMemberCode, int iAutoLeave, float fBalance);
    public synchronized native static AccountCheck getAccountCheck(String pStartTime, String pEndTime);

    public synchronized native static ParkedCarInfo getCarInPark(String sOrderNo, String sCarLicense, int iMatchMethod, int iCarOrder, String sSearchID);
    public synchronized native static ParkedCarInfo dispatchCoupon(String sOrderNo, String sCarLicense, String sCouID, String sCouName, int iCouType,
                                            int iCouTime, float fCouMny,String sStartDate, String sEndDate,String sCarportID, String sInID);
	public synchronized native static String	getCarInParkByName(String license);
	public synchronized native static ParkedCarInfo dispatchShopCoupon(String sOrderNo, String sCarLicense, String sCouID, String sCouName, int iCouType, 
											int iCouTime, float fCouMny,String sStartDate, String sEndDate,String sCarportID, String sInID,
											String sCardNo, int iCardType, int iDisType, int iUseType);
	public synchronized native static ShopCheckType getShopCheckType(String sOrderNo, String sCarLicense, String sInID);
	
	// 金鹰酒店-checkin
	public synchronized native static int checkIn(String pOrderNo, String pLicense, String pRoomNo, String pName, int iCouTime, String pChkInTime, String pCarportID, String pInID);
	// 金鹰酒店-checkout
	public synchronized native static int checkOut(String pOrderNo, String pLicense, String pRoomNo, String pName, String pChkInTime, String pChkOutTime, 
									int iOutCouTime, String pCarportID, String pInID);

    public static void main(String[] args) {

        int iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
        System.out.println("iInitRtn=" + iInitRtn);
        ParkedCarInfo obj = getParkedCarInfo("N1234", 0,"苏A12366",0, 0, 0, "", "");
        System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime +
                " InPic = "+obj.sInPic + " ID = "+obj.sID + "StartTime = "+obj.sStartTime + "EndTime = "+obj.sEndTime + " return val =" + obj.iReturn);
//
//	obj = getParkedCarInfo(0,"苏A12367",15, 0, 3, "", "");
//	System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " ID = "+obj.sID + " return val =" + obj.iReturn);
//
//	obj = getParkedCarInfo(0,"苏A12366",15, 1, 0, "", "");
//	System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " ID = "+obj.sID + " return val =" + obj.iReturn);
//
//	obj = getParkedCarInfo(0,"苏A12366",15, 1, 1, "", "");
//	System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " ID = "+obj.sID + " return val =" + obj.iReturn);
//
//	obj = getParkedCarInfo(0,"苏A12366",15, 1, 2, "", "");
//	System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " ID = "+obj.sID + " return val =" + obj.iReturn);
//
//	obj = getParkedCarInfo(0,"苏A12366",15, 1, 3, "", "");
//	System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " ID = "+obj.sID + " return val =" + obj.iReturn);
//
//	obj = getParkedCarInfo(0,"苏A12366",15, 1, 4, "", "");
//	System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " ID = "+obj.sID + " return val =" + obj.iReturn);
//	obj = getParkedCarInfo(0,"苏A12366",15, 1, 5, "", "");
//	System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " ID = "+obj.sID + " return val =" + obj.iReturn);
        payParkCarFee("N1234", 0,"苏A12366","2016-11-18 14:45:16",.1f, "81703", "2017/6/19 11:29:18", 1, "P1234", "2017/6/19 11:29:18","2017/6/19 17:04:18","2017/6/19 17:35:18",1003,.1f,0);
//	payParkCarFee(0,"苏A12368","2016-11-18 14:45:08",.6f, "79754", "2016/11/17 11:17:29", 1);
//	payParkCarFee(0,"苏A12466","2016-11-18 14:45:08",.6f, "79751", "2016/11/17 11:47:29", 1);
//	LeftParkInfo[] aobject;
//	aobject=getLeftParkInfo();
//	for(int i = 0; i < aobject.length; i++)
//	{
//		System.out.println("ParkName = "+aobject[i].sParkName+ " SumNum = "+aobject[i].iSumNum + " LeftNum =" +  aobject[i].iLeftNum + " iCarProp=" + aobject[i].iCarProp);
//  }

        String aCarportNum[] = new String[] { "102前上", "虚拟20", "虚拟24", "虚拟25" };
        ViewCarportRoomInfo[] aCarportObj;
        aCarportObj=getCarportInfo("苏A12344", "321456", 1, 0);
        for(int i = 0; i < aCarportObj.length; i++)
        {
            System.out.println("sCarportNum = "+aCarportObj[i].sCarportNum + " sRoomNum = "+aCarportObj[i].sRoomNum + " sName = "+aCarportObj[i].sName
                    + " sAddress = "+aCarportObj[i].sAddress + " sPhoneNumber = "+aCarportObj[i].sPhoneNumber + " sPosition = "+aCarportObj[i].sPosition + " sStartDate = "+aCarportObj[i].sStartDate
                    + " sEndDate = "+aCarportObj[i].sEndDate + " fDeposit = "+aCarportObj[i].fDeposit + " bTemporary = "+aCarportObj[i].bTemporary
                    + " sRemark =" +  aCarportObj[i].sRemark + " sRentName=" + aCarportObj[i].sRentName + " fRentMoney=" + aCarportObj[i].fRentMoney + " sParkName = "+aCarportObj[i].sParkName
                    + " sBillNo =" +  aCarportObj[i].sBillNo + " iLicenseColor=" + aCarportObj[i].iLicenseColor);
        }
        payCarportRent("苏A12344", "102", "虚拟20", aCarportNum, "2016-12-27 09:32:40", "20161227152129", 1, 0.07f, true, 1, 1003, 0.07f, 0.07f, 0);
//	payCarportRent("苏A12344", "102", "虚拟20", aCarportNum, "2016-12-27 09:32:41", "20161227152124", 3, 4.0f, false);
//	payCarportRent("苏A12344", "102", "虚拟20", aCarportNum, "2016-12-27 09:32:42", "20161227152130", 3, 12, true);
//	payCarportRent("苏A12344", "102", "虚拟20", aCarportNum, "2016-12-27 09:32:43", "20161227152126", 3, 3, true);

        unitialized();
        System.out.println("unitialized1");
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	System.out.println("iInitRtn=" + iInitRtn);
// 	obj = getParkedCarInfo(0,"苏A12377",15);
// 	payParkCarFee(0,"苏A12377","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	System.out.println("unitialized2");
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"苏A12366",15);
// 	payParkCarFee(0,"苏A12366","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"苏A12399",15);
// 	payParkCarFee(0,"苏A12399","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"苏A12311",15);
// 	payParkCarFee(0,"苏A12311","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"苏A12322",15);
// 	payParkCarFee(0,"苏A12322","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"苏A12333",15);
// 	payParkCarFee(0,"苏A12333","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();

    }
}