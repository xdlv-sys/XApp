package xd.dl.mina;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import xd.dl.yl.YL;
import xd.fw.mina.tlv.TLVMessage;

/**
 * 银联，离场
 * 消息号:34(PROXY_YINLIAN_CAR_OUT)
 */

@Service
public class YinLianCarOut extends SendRequest {
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

    	System.out.println("++++++++++银联，离场，获得TLV数据++++++++++");
    	System.out.println(request.toString());
    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    	
//    	魔数 //见tlv格式说明
//    	消息总长度 //见tlv格式说明
//    	消息号 int;
//    	版本号 int;//填1
//    	车牌号 string;
//    	入场时间 string;
//    	出场时间 string;
//    	停车记录唯一标识(订单号) string;
    	
    	int _count = 0;
    	
//    	int code = request.getNextInt(_count++);//消息号 
//    	int version = request.getNextInt(_count++);//版本号，填1 
    	int version = 1;//版本号，填1 
    	String plateNumber = request.getNextString(_count++);	//车牌号	
    	String startTime = request.getNextString(_count++);	//入场时间	
    	String endTime = request.getNextString(_count++);	//离场时间	
    	String syncId = request.getNextString(_count++);	//停车记录唯一标识(订单号)
    	
    	//第二步：调用银联
    	JSONObject rsp = YL.noticeExit(syncId, plateNumber, startTime, endTime);
    	
    	System.out.println("<<<<<<<<<<<<<<<银联，离场，返回JSON数据>>>>>>>>>>>>>>>>>>>");
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
		
		
//		魔数 //见tlv格式说明
//		消息总长度 //见tlv格式说明
//		消息号 string;
//		时间ID string;
//		版本号 int;//填1
//		返回结果 int;//状态码
//		返回的错误提示 string;//返回描述
//		车牌号 string;
//		入场时间 string;
//		停车记录唯一标识(订单号) string;
    	
        ret
			.setNext(version)
			.setNext(_resultCode)
			.setNext(_description)
			.setNext(plateNumber)
			.setNext(startTime)
			.setNext(syncId);
        
        System.out.println("[[[[[[[[[[[[[[[银联，离场，返回TLV数据]]]]]]]]]]]]]]");
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
