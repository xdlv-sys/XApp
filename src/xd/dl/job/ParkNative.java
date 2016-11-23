package xd.dl.job;

import xd.fw.FwUtil;

public class ParkNative
{
    public native static int initialized( String pDbIP,String pDbName,String pDbUserID,String pDbPwd );
    public native static int unitialized();
    public native synchronized static ParkedCarInfo getParkedCarInfo(int iCarType, String pLicense, int iFreeTime, int iMatchMethod, int iCarOrder, String pSearchID, String pSearchInTime);
    public native synchronized static int payParkCarFee(int iCarType, String pCarLicese, String pPayTime, float fMoney, String pSearchID, String pSearchInTime, int iPayMethod);
    public native synchronized static LeftParkInfo[] getLeftParkInfo();

    public static int getLeftCount() {
        LeftParkInfo[] leftInfos = getLeftParkInfo();
        return leftInfos[0].iLeftNum + leftInfos.length > 1 ? leftInfos[1].iLeftNum : 0;
    }

    static {
        System.loadLibrary("ParkingMeter");
    }
}