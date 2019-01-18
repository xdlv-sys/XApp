package xd.dl.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) {
//		ObjectFactory factory = new ObjectFactory();
//		JAXBElement<String> json = factory.createProcParkingFeeJson("");
//		ProcParkingFee fee = new ProcParkingFee();
//			fee.setJson(json);
			
//		ParkingPortType type = (ParkingPortType) Parking.create(new QName("ProcParkingFee"));
//		type.procParkingFee("");
		
		
		//JSON格式
//    	commType	String	交互类型 3:支付（手机） 4:支付（岗亭）
//    	payNo	String	支付方式 01:VIP停车时间  02:购物小票  03:VIP积分兑换  04:现金  05:信用卡  06:市民卡   07:微信支付  08:支付宝支付
//    	carNo	String	车牌号
//    	companyNo	String	分店号
//    	inputStr	String	输入串（当支付方式为VIP卡停车时间、VIP积分兑换时，该值为VIP卡号；当购物小票时，为交易号串）
//    	inputFlag	String	输入类型 ‘1’:二维码 ‘2’:手输 ‘0’或空:刷卡
//    	glideNo	String	停车支付流水号（金鹰生成并回传第三方，双方系统根据此流水号保持一致）
//    	siteNo	String	缴费地点
//    	dayTime	int	白天停车时间(停车时间为15分钟的倍数)
//    	nightTime	int	晚间停车时间(停车时间为60分钟的倍数)
//    	daySum	Number	白天停车金额
//    	nightSum	Number	晚间停车金额
    	
    	JSONObject json = new JSONObject();
	    	json.put("commType", "3");
	    	json.put("payNo", "01");
	    	json.put("carNo", "沪A123456");
	    	json.put("companyNo", "1");
	    	json.put("inputStr", "123456");
	    	json.put("inputFlag", "1");
	    	json.put("glideNo", "123456123456");
	    	json.put("siteNo", "远中产业园");
	    	json.put("dayTime", 15);
	    	json.put("nightTime", 60);
	    	json.put("daySum", 1.5);
	    	json.put("nightSum", 1.4);
		
		Parking pk = new Parking();
		ParkingPortType type1 = pk.getParkingHttpSoap12Endpoint();
		System.out.println("请求" + json.toString());
		System.out.println("响应：" + type1.procParkingFee(json.toString()));
	}

}
