package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.dl.ws.Parking;
import xd.dl.ws.ParkingPortType;
import xd.fw.mina.tlv.TLVMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 金鹰停车场--岗亭支付接口①/②
 * 消息号：25(PROXY_JINYING_VIP_PAY)
 */

@Service
public class JinyingVipPay extends SendRequest {
    @Value("${out_address}")
    String outAddress;

    public String[][] constructParams(TLVMessage request) {
        return null;
    }

    @Override
    protected String json(TLVMessage request, IoSession session) {
    	return null;
    }

    void _putJson(JSONObject json, String[] v) {
        json.put(v[0], v[1]);
    }
    
    /**
     * 金鹰停车场--岗亭支付接口②实现在此
     * 将TLV数据转换为JSON数据，通过WS调用，返回数据，再组织成TLV数据返回
     * 消息号：25(PROXY_JINYING_VIP_PAY)
     */
    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
    	
    	//第一步：将request的TLV格式解析，转换为JSON

    	System.out.println("++++++++++金鹰VIP支付，接口①，获得TLV数据++++++++++");
    	System.out.println(request.toString());
    	System.out.println("++++++++++++++++++++++++++++++++++++++++");
    	
    	//获得TLV格式：
//    	魔数 //见tlv格式说明
//    	消息总长度 //见tlv格式说明
//    	消息号 int;
//    	版本号 int;//填1
//    	交互类型 string;
//    	支付方式 string;
//    	车牌号	string;
//    	分店号	string;
//    	输入串	string;
//    	输入类型	string;
//    	停车支付流水号 string;
//    	缴费地点	string;
//    	白天停车时间 int;//单位分钟
//    	晚间停车时间 int;//单位分钟
//    	白天停车金额 float;//单位元
//    	晚间停车金额 float;//单位元
    	
    	int _count = 0;
    	//魔数 //见tlv格式说明
//    	_count++;
    	//消息总长度 //见tlv格式说明
//    	_count++;
    	
//    	int code = request.getNextInt(_count++);//消息号 
//    	int version = request.getNextInt(_count++);//版本号，填1 
    	int code = 25;//消息号 
    	int version = 1;//版本号，填1 
    	String commType = request.getNextString(_count++);//交互类型
    	String payNo = request.getNextString(_count++);//支付方式 
    	String carNo = request.getNextString(_count++);//车牌号	
    	String companyNo = request.getNextString(_count++);//分店号	
    	String inputStr = request.getNextString(_count++);//输入串	
    	String inputFlag = request.getNextString(_count++);//输入类型	
    	String glideNo = request.getNextString(_count++);//停车支付流水号 
    	String siteNo = request.getNextString(_count++);//缴费地点	
    	int dayTime = request.getNextInt(_count++);//白天停车时间 单位分钟
    	int nightTime = request.getNextInt(_count++);//晚间停车时间 单位分钟
    	float daySum = Float.parseFloat(String.valueOf(request.getNextValue(_count++)));//白天停车金额 单位元
    	float nightSum = Float.parseFloat(String.valueOf(request.getNextValue(_count++)));//晚间停车金额 单位元
    	
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
    	
    	JSONObject jobject = new JSONObject();
    	jobject.put("commType", commType);
    	jobject.put("payNo", payNo);
    	jobject.put("carNo", carNo);
    	jobject.put("companyNo", companyNo);
    	jobject.put("inputStr", inputStr);
    	jobject.put("inputFlag", inputFlag);
    	jobject.put("glideNo", glideNo);
    	jobject.put("siteNo", siteNo);
    	jobject.put("dayTime", dayTime);
    	jobject.put("nightTime", nightTime);
    	jobject.put("daySum", daySum);
    	jobject.put("nightSum", nightSum);
    	
    	System.out.println("=========金鹰VIP支付，接口①，JSON数据=========");
    	System.out.println(jobject.toString());
    	System.out.println("====================================");
    	
    	//第二步：WebService调用ProcParkingFee，获得JSON
		Parking pk = new Parking();
		ParkingPortType type = pk.getParkingHttpSoap12Endpoint();
    	JSONObject jobject_r = JSONObject.fromObject(type.procParkingFee(jobject.toString()));
    	
    	System.out.println("<<<<<<<<<<<<<<<金鹰VIP支付，接口②，WS返回JSON数据>>>>>>>>>>>>>>>>>>>");
    	System.out.println(jobject_r.toString());
    	System.out.println("<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
    	
    	//第三步：解析JSON，组织TLV返回
    	int _shakeFlag = jobject_r.getInt("shakeFlag");		//1	错误码
		String _resultMessage = jobject_r.getString("resultMessage");		//非1	错误信息
		String _carNo = jobject_r.getString("carNo");		//车牌号
		String _commType	= jobject_r.getString("commType");		//交互类型
		String _companyNo = jobject_r.getString("companyNo");			//分店号
		String _glideNo	= jobject_r.getString("glideNo");		//停车支付流水号
		String _inputStr = jobject_r.getString("inputStr"); 		//输入串
		String _orderTime = jobject_r.getString("orderTime");			//订单时间
		//订单时间处理成yyyy-mm-dd hh:mm:ss
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		try{
			_orderTime = sdf.format(sdf2.parse(_orderTime));
		}catch(Exception e){
			_orderTime = sdf.format(new Date());
		}
		String _payNo	= jobject_r.getString("payNo");		//支付方式
		double _dayPaySum	= jobject_r.getDouble("dayPaySum");		//白天支付金额
		double _nightPaySum	= jobject_r.getDouble("nightPaySum");		//晚间支付金额
		int _dayPayTime = jobject_r.getInt("dayPayTime");		//白天支付时间
		int _nightPayTime = jobject_r.getInt("nightPayTime");		//晚间支付时间
		
//		魔数 //见tlv格式说明
//		消息总长度 //见tlv格式说明
//		消息号 int;
//		时间ID string;
//		版本号 int;//填1
//		返回结果(错误码) int;
//		返回的错误提示(错误信息) string;
//		车牌号 string;
//		交互类型 string;
//		分店号string;
//		停车支付流水号 string;
//		输入串 string;
//		订单时间 string;
//		支付方式 string
//		白天支付金额 float;
//		晚间支付金额 float;
//		白天支付时间 int;
//		晚间支付时间 int;
    	
        ret
//			.setNext(code)
//			.setNext(this.getTimeStamp())
			.setNext(version)
			.setNext(_shakeFlag)
			.setNext(_resultMessage)
			.setNext(_carNo)
			.setNext(_commType)
			.setNext(_companyNo)
			.setNext(_glideNo)
			.setNext(_inputStr)
			.setNext(_orderTime)
			.setNext(_payNo)
			.setNext(Float.parseFloat(String.valueOf(_dayPaySum)))
			.setNext(Float.parseFloat(String.valueOf(_nightPaySum)))
			.setNext(_dayPayTime)
			.setNext(_nightPayTime);
        
        System.out.println("[[[[[[[[[[[[[[[金鹰VIP支付，接口③，返回TLV数据]]]]]]]]]]]]]]");
    	System.out.println(ret.toString());
    	System.out.println("[[[[[[[[[[[[[[[]]]]]]]]]]]]]]");
    	
        return ret;
    }

    @Override
    public String svrAddress() {
        return outAddress;
    }

    @Override
    protected boolean run() {
        return centerFlag;
    }
    
}
