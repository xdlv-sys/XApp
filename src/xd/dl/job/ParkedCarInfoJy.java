package xd.dl.job;

public class ParkedCarInfoJy {

	public String sInTime;//入场时间

	public float fMoney;//计算的总金额

	public int  iParkedTime;//停留时间，单位分钟

	public String sInPic;//图片所在位置

	public String sCarLicense;//车牌号

	public String sID;//入场记录在数据库中的ID

	public String sStartTime;	//开始结算时间

	public String sEndTime;	//结束结算时间

	public String sShopNo;//金鹰门店编码

	public String sPosNo;//车辆位置编码

	public int iDayTime;//剩余白天停车时长，单位分钟

	public int iNightTime;//剩余夜间停车时长，单位分钟

	public float fDayMny;//剩余白天停车金额,单位元

	public float fNightMny;//剩余夜间停车金额,单位元

	public int iReturn;//调用返回值,0为成功，其余为出错
}