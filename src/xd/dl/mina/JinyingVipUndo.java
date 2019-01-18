package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.dl.ws.Parking;
import xd.dl.ws.ParkingPortType;
import xd.fw.mina.tlv.TLVMessage;

/**
 * 金鹰停车场--金鹰岗亭VIP卡支付停车缴费时间冲正
 * 消息号：26(PROXY_JINYING_VIP_UNDO)
 */

@Service
public class JinyingVipUndo extends SendRequest {
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
     * 金鹰停车场--金鹰岗亭VIP卡支付停车缴费时间冲正
     * 消息号：26(PROXY_JINYING_VIP_UNDO)
     */
    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
    	
    	//第一步：将request的TLV格式解析，转换为JSON

    	System.out.println("++++++++++金鹰岗亭VIP卡支付停车缴费时间冲正，获得TLV数据++++++++++");
    	System.out.println(request.toString());
    	System.out.println("++++++++++++++++++++++++++++++++++++++++");
    	
    	//获得TLV格式：
//    	魔数 //见tlv格式说明
//    	消息总长度 //见tlv格式说明
//    	消息号 int;
//    	版本号 int;//填1
//    	交互类型 string
//    	支付方式 string
//    	车牌号	string;
//    	分店号	string;
//    	输入串	string;
//    	停车支付流水号 string;
//    	缴费地点	string;
    	
    	int _count = 0;
    	//魔数 //见tlv格式说明
//    	_count++;
    	//消息总长度 //见tlv格式说明
//    	_count++;
    	
//    	int code = request.getNextInt(_count++);//消息号 
//    	int version = request.getNextInt(_count++);//版本号，填1 
    	
    	int code = 26;
    	int version = 1;
    	String commType = request.getNextString(_count++);//交互类型
    	String payNo = request.getNextString(_count++);//支付方式 
    	String carNo = request.getNextString(_count++);//车牌号	
    	String companyNo = request.getNextString(_count++);//分店号	
    	String inputStr = request.getNextString(_count++);//输入串	
    	String glideNo = request.getNextString(_count++);//停车支付流水号 
    	String siteNo = request.getNextString(_count++);//缴费地点	
    	
    	//JSON格式
//    	commType	String	交互类型 3:支付（手机） 4:支付（岗亭）
//    	payNo	String	支付方式 01:VIP停车时间  02:购物小票  03:VIP积分兑换  04:现金  05:信用卡  06:市民卡   07:微信支付  08:支付宝支付
//    	carNo	String	车牌号
//    	companyNo	String	分店号
//    	inputStr	String	输入串（当支付方式为VIP卡停车时间、VIP积分兑换时，该值为VIP卡号；当购物小票时，为交易号串）
//    	glideNo	String	停车支付流水号（金鹰生成并回传第三方，双方系统根据此流水号保持一致）
//    	siteNo	String	缴费地点
    	
    	JSONObject jobject = new JSONObject();
    	jobject.put("commType", commType);
    	jobject.put("payNo", payNo);
    	jobject.put("carNo", carNo);
    	jobject.put("companyNo", companyNo);
    	jobject.put("inputStr", inputStr);
    	jobject.put("glideNo", glideNo);
    	jobject.put("siteNo", siteNo);
    	
    	System.out.println("=========金鹰岗亭VIP卡支付停车缴费时间冲正，JSON数据=========");
    	System.out.println(jobject.toString());
    	System.out.println("====================================");
    	
    	//第二步：WebService调用CancelParkingFee，获得JSON
    	Parking pk = new Parking();
		ParkingPortType type = pk.getParkingHttpSoap12Endpoint();
    	JSONObject jobject_r = JSONObject.fromObject(type.cancelParkingFee(jobject.toString()));
    	
    	System.out.println("<<<<<<<<<<<<<<<金鹰岗亭VIP卡支付停车缴费时间冲正，WS返回JSON数据>>>>>>>>>>>>>>>>>>>");
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
			.setNext(_inputStr);
        
        System.out.println("[[[[[[[[[[[[[[[金鹰岗亭VIP卡支付停车缴费时间冲正，返回TLV数据]]]]]]]]]]]]]]");
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
