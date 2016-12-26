package xd.dl.job;

public class ParkNative {
    public native static int initialized(String pDbIP, String pDbName, String pDbUserID, String pDbPwd);

    public native static int unitialized();

    public native static ParkedCarInfo getParkedCarInfo(int iCarType, String pLicense, int iFreeTime, int iMatchMethod, int iCarOrder, String pSearchID, String pSearchInTime);

    public native static int payParkCarFee(int iCarType, String pCarLicese, String pPayTime, float fMoney, String pSearchID, String pSearchInTime, int iPayMethod);

    public native static LeftParkInfo[] getLeftParkInfo();

    public native static ViewCarportRoomInfo[] getCarportInfo(String pLicense);

    public native static int payCarportRent(String pCarLicense, String pRoomNum, String pCarportNum, String[] aCarportNum, String pPayTime, int iMonthCnt, float fMoney, boolean bPayAllCarport);

    public static int getLeftCount() {
        LeftParkInfo[] leftInfos = getLeftParkInfo();
        int leftCount = 0;
        for (LeftParkInfo l : leftInfos) {
            leftCount += l.iLeftNum;
        }
        return leftCount;
    }

    static {
        System.loadLibrary("ParkingMeter");
    }
}