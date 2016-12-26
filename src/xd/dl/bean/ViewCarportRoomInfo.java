package xd.dl.bean;

public class ViewCarportRoomInfo
{
    public String sCarportNum;		//车位号
    public String sRoomNum;			//组号
    public String sName;				//姓名
    public String sAddress;			//地址
    public String sPhoneNumber;		//电话
    public String sPosition;			//位置
    public String	sStartDate;			//有效起始日期
    public String	sEndDate;			//有效结束日期
    public float	fDeposit;			//押金
    public boolean	bTemporary;			//临时
    public String sRemark;			//备注
    public String sRentName;		//包月类型
    public float fRentMoney;		//包月金额
    public String sParkName;		//停车场名

    public String getsCarportNum() {
        return sCarportNum;
    }

    public void setsCarportNum(String sCarportNum) {
        this.sCarportNum = sCarportNum;
    }

    public String getsRoomNum() {
        return sRoomNum;
    }

    public void setsRoomNum(String sRoomNum) {
        this.sRoomNum = sRoomNum;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsPhoneNumber() {
        return sPhoneNumber;
    }

    public void setsPhoneNumber(String sPhoneNumber) {
        this.sPhoneNumber = sPhoneNumber;
    }

    public String getsPosition() {
        return sPosition;
    }

    public void setsPosition(String sPosition) {
        this.sPosition = sPosition;
    }

    public String getsStartDate() {
        return sStartDate;
    }

    public void setsStartDate(String sStartDate) {
        this.sStartDate = sStartDate;
    }

    public String getsEndDate() {
        return sEndDate;
    }

    public void setsEndDate(String sEndDate) {
        this.sEndDate = sEndDate;
    }

    public float getfDeposit() {
        return fDeposit;
    }

    public void setfDeposit(float fDeposit) {
        this.fDeposit = fDeposit;
    }

    public boolean isbTemporary() {
        return bTemporary;
    }

    public void setbTemporary(boolean bTemporary) {
        this.bTemporary = bTemporary;
    }

    public String getsRemark() {
        return sRemark;
    }

    public void setsRemark(String sRemark) {
        this.sRemark = sRemark;
    }

    public String getsRentName() {
        return sRentName;
    }

    public void setsRentName(String sRentName) {
        this.sRentName = sRentName;
    }

    public float getfRentMoney() {
        return fRentMoney;
    }

    public void setfRentMoney(float fRentMoney) {
        this.fRentMoney = fRentMoney;
    }

    public String getsParkName() {
        return sParkName;
    }

    public void setsParkName(String sParkName) {
        this.sParkName = sParkName;
    }
}