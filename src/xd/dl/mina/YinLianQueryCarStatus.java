package xd.dl.mina;

import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.dl.yl.YL;
import xd.fw.mina.tlv.TLVMessage;

/**
 * 银联，平台查询车辆绑定状态
 * 消息号：30（PROXY_YINLIAN_QUERY_CAR_STATUS）
 */

@Service
public class YinLianQueryCarStatus extends SendRequest {
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
     * 银联平台查询车辆绑定状态
     */
    @Override
    TLVMessage constructMessage(TLVMessage ret, TLVMessage request, JSONObject retJson, IoSession session) {
    	
    	//第一步：将request的TLV格式解析，转换为JSON

    	System.out.println("++++++++++银联，车辆绑定状态，获得TLV数据++++++++++");
    	System.out.println(request.toString());
    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    	
    	//获得TLV格式：
//    	魔数 //见tlv格式说明
//    	消息总长度 //见tlv格式说明
//    	消息号 int;
//    	版本号 int;//填1
//    	车牌号 string;
    	
    	int _count = 0;
    	
//    	int code = request.getNextInt(_count++);//消息号 
//    	int version = request.getNextInt(_count++);//版本号，填1 
    	int version = 1;//版本号，填1 
    	String carNum = request.getNextString(_count++);//车牌号	
    	
    	//第二步：调用银联
    	JSONObject rsp = YL.carStatus(carNum);
    	
    	System.out.println("<<<<<<<<<<<<<<<银联，车辆绑定状态，返回JSON数据>>>>>>>>>>>>>>>>>>>");
    	System.out.println(rsp.toString());
    	System.out.println("<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
    	
    	//第三步：解析JSON，组织TLV返回
    	/*
    		{
			  "resultCode": 0,
			  "description": "string",
			  "data": {
			    "status": 1,			车辆代扣状态，0:可代扣;1:不可代扣
				"statusDescription": 4,	车辆绑定以及无感支付签约状态，1:车辆绑定并开通无感支付 2:冻结 3:车辆绑定未开通无感支付 4:车辆未绑定 【当状态为2，3，4时，车辆代扣状态为不可代扣】
				"signTime": 2016-04-16T16:06:05Z 	签约生效时间，如果未签约，返回值为空。UTC时间。例如：2016-04-16T16:06:05Z
			  }
			}

    	 */
    	int _resultCode = rsp.getInt("resultCode");		
		String _description = rsp.getString("description");		
		
			JSONObject data = rsp.optJSONObject("data");
			String _signTime = data.getString("signTime");		
			int _status = data.getInt("status");		
			int _statusDescription = data.getInt("statusDescription");		
		
//		魔数 //见tlv格式说明
//		消息总长度 //见tlv格式说明
//		消息号 int;
//		时间ID string;
//		版本号 int;//填1
//		返回结果 int;// resultCode状态码
//		返回的错误提示 string;// description返回描述
//		车牌号 string;
//		签约生效时间 string;
//		车辆代扣状态 int;
//		车辆绑定以及无感支付签约状态 int;
    	
        ret
			.setNext(version)
			.setNext(_resultCode)
			.setNext(_description)
			.setNext(carNum)
			.setNext(_signTime)
			.setNext(_status)
			.setNext(_statusDescription);
        
        System.out.println("[[[[[[[[[[[[[[[银联，车辆绑定状态，返回TLV数据]]]]]]]]]]]]]]");
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
