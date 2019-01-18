package xd.dl.mina;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import xd.dl.yl.Config;
import xd.dl.yl.YL;
import xd.fw.mina.tlv.TLVMessage;

/**
 * 银联，发起无感支付
 * 消息号：32（PROXY_YINLIAN_AUTO_FEE_QUERY）
 */

@Service
public class YinLianAutoFeeQuery extends SendRequest {
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
     * 银联，发起无感支付
     */
    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
    	
    	//第一步：将request的TLV格式解析，转换为JSON

    	System.out.println("++++++++++银联，发起无感支付，获得TLV数据++++++++++");
    	System.out.println(request.toString());
    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    	
//    	魔数 //见tlv格式说明
//    	消息总长度 //见tlv格式说明
//    	消息号 int;
//    	版本号 int;//填1
//    	车牌号string;
//    	发送给智慧通行平台的订单金额，用户支付到运营商实际金额 float;
//    	入场时间string;
//    	出场时间string;
//    	订单号string;
//    	支付请求唯一序列号(订单流水号) string;
//    	总金额 float;
//    	自动离场抬杆方式 int;// 0:先抬杆后扣款,1:先扣款后抬杆
//    	增加一个岗亭ID

    	int _count = 0;
    	
//    	int code = request.getNextInt(_count++);//消息号 
//    	int version = request.getNextInt(_count++);//版本号，填1 
    	int version = 1;//版本号，填1 
    	String plateNumber = request.getNextString(_count++);	//车牌号	
    	float payAmount = (float)request.getNextValue(_count++);	
    	String startTime = request.getNextString(_count++);	
    	String endTime = request.getNextString(_count++);	
    	String orderId = request.getNextString(_count++);	
    	String syncId = request.getNextString(_count++);	//停车记录唯一标识(订单号)
    	float serviceAmount = (float)request.getNextValue(_count++);	
    	int auto = request.getNextInt(_count++);	// 0:先抬杆后扣款,1:先扣款后抬杆
    	String watchId = request.getNextString(_count++);	//岗亭ID
    	
    	JSONObject data = new JSONObject();
			data.put("syncId", syncId);
			data.put("plateNumber", plateNumber);
			data.put("orderId", orderId);
			data.put("payAmount", Math.floor(payAmount * 100));
			data.put("serviceAmount", Math.floor(serviceAmount * 100));
			data.put("orderDate", endTime);		//订单生成时间
			data.put("startTime", startTime);
			data.put("endTime", endTime);
			data.put("parkId", Config.park_id);
			data.put("parkName", Config.park_name);
    	
    	//第二步：调用银联
    	JSONObject rsp = YL.payBill(data);
    	
    	System.out.println("<<<<<<<<<<<<<<<银联，发起无感支付，返回JSON数据>>>>>>>>>>>>>>>>>>>");
    	System.out.println(rsp.toString());
    	System.out.println("<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
    	
    	//第三步：解析JSON，组织TLV返回
    	/*
    		
			{
			  "resultCode": 0,
			  "description": "成功"
			}

    	 */
    	int _resultCode = rsp.getInt("resultCode");		
		String _description = rsp.getString("description");		
		
		//测试先注释
		if(_resultCode == 0) {
			//开启查询订单状态 的 独立线程
			YinLianPayTask task = new YinLianPayTask(orderId, watchId, data);
			task.run();
		}
		
		
//		魔数 //见tlv格式说明
//		消息总长度 //见tlv格式说明
//		消息号 int;
//		时间ID string;
//		版本号 int;//填1
//		返回结果 int;//状态码
//		返回的错误提示 string;//返回描述
//		车牌号 string;
//		岗亭发送时传给智慧通行平台的订单金额 float;
//		订单号 string;
//		岗亭发送时传的支付请求唯一序列号(订单流水号) string;
//		全渠道订单号 string;//填空
//		优惠金额 float;//传0
//		岗亭ID
		
        ret
			.setNext(version)
			.setNext(_resultCode)
			.setNext(_description)
			.setNext(plateNumber)
			.setNext(payAmount)
			.setNext(orderId)
			.setNext(syncId)
			.setNext("")
			.setNext(0)
        	.setNext(watchId);
        
        System.out.println("[[[[[[[[[[[[[[[银联，发起无感支付，返回TLV数据]]]]]]]]]]]]]]");
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
