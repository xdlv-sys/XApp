package xd.dl.job;

public class ParkNative
{
    public native static int initialized( String pDbIP,String pDbName,String pDbUserID,String pDbPwd );
    public native static int unitialized();
    public native static ParkedCarInfo getParkedCarInfo(int iCarType, String pLicense, int iFreeTime, boolean bFuzzyMatch, int iCarOrder);
    public native static int payParkCarFee(int iCarType, String pCarLicese, String pPayTime, float fMoney);
    static {
        System.loadLibrary("ParkingMeter");
    }

    public static void main(String[] args) {
//        ParkNative test = new ParkNative();
//	int iInitRtn = test.initialized("193.168.1.100","hjcsfbz","sa","sa");
//	System.out.println("iInitRtn=" + iInitRtn);
//	ParkedCarInfo obj = test.getParkedCarInfo(0,"ËÕA12366",15);
//	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
//	test.unitialized();


        int iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
        System.out.println("iInitRtn=" + iInitRtn);
        ParkedCarInfo obj = getParkedCarInfo(0,"ËÕA12366",15, false, 2);
        System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " return val =" + obj.iReturn);

        obj = getParkedCarInfo(0,"ËÕA12367",15, false, 3);
        System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " return val =" + obj.iReturn);

        obj = getParkedCarInfo(0,"ËÕA12366",15, true, 0);
        System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " return val =" + obj.iReturn);

        obj = getParkedCarInfo(0,"ËÕA12366",15, true, 1);
        System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " return val =" + obj.iReturn);

        obj = getParkedCarInfo(0,"ËÕA12366",15, true, 2);
        System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " return val =" + obj.iReturn);

        obj = getParkedCarInfo(0,"ËÕA12366",15, true, 3);
        System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " return val =" + obj.iReturn);

        obj = getParkedCarInfo(0,"ËÕA12366",15, true, 4);
        System.out.println("CarLicense = "+obj.sCarLicense+ " money = "+obj.fMoney + " intime =" +  obj.sInTime + " parkedtime=" + obj.iParkedTime + " InPic = "+obj.sInPic + " return val =" + obj.iReturn);
        payParkCarFee(0,"ËÕA12366","2016-10-22 14:44:57",0.1f);
        payParkCarFee(0,"ËÕA12396","2016-10-22 14:44:57",0.1f);
        payParkCarFee(0,"ËÕA12466","2016-10-22 14:44:57",0.1f);
        unitialized();
        System.out.println("unitialized1");
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	System.out.println("iInitRtn=" + iInitRtn);
// 	obj = getParkedCarInfo(0,"ËÕA12377",15);
// 	payParkCarFee(0,"ËÕA12377","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	System.out.println("unitialized2");
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"ËÕA12366",15);
// 	payParkCarFee(0,"ËÕA12366","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"ËÕA12399",15);
// 	payParkCarFee(0,"ËÕA12399","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"ËÕA12311",15);
// 	payParkCarFee(0,"ËÕA12311","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"ËÕA12322",15);
// 	payParkCarFee(0,"ËÕA12322","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();
//
// 	iInitRtn = initialized("193.168.1.100","hjcsfbz","sa","sa");
// 	obj = getParkedCarInfo(0,"ËÕA12333",15);
// 	payParkCarFee(0,"ËÕA12333","2016-8-25 8:56:56",0.5f);
// 	System.out.println("money = "+obj.fMoney + "intime =" +  obj.sInTime + "parkedtime=" + obj.iParkedTime + "return val =" + obj.iReturn);
// 	unitialized();

    }
}