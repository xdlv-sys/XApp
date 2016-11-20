package xd.dl.job;

public class ParkNative
{
    public native static int initialized( String pDbIP,String pDbName,String pDbUserID,String pDbPwd );
    public native static int unitialized();
    public native static ParkedCarInfo getParkedCarInfo(int iCarType, String pLicense, int iFreeTime, int iMatchMethod, int iCarOrder, String pSearchID, String pSearchInTime);
    public native static int payParkCarFee(int iCarType, String pCarLicese, String pPayTime, float fMoney, String pSearchID, String pSearchInTime, int iPayMethod);
    public native static LeftParkInfo[] getLeftParkInfo();

    static {
        System.loadLibrary("ParkingMeter");
    }
}