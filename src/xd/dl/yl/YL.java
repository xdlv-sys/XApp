package xd.dl.yl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class YL {
	//key,订单号；value,支付状态（1.等待支付中，0.线下支付，2.银联支付）
    public static HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
    //订单号 和 岗亭ID
    public static HashMap<String, String> watchMap = new HashMap<String, String>();
    //订单号 和备用字段,json格式
    public static HashMap<String, JSONObject> orderInfoMap = new HashMap<String, JSONObject>();
    
	
	public static void main(String[] args) {
//		System.out.println("查询车辆停车状态：" + carStatus("苏A22388"));
		
//		System.out.println("入场通知：" + noticeEntrance("20181217001", "苏A22388", "2018-12-17 16:06:05"));
		
		System.out.println("订单状态：" + payStatus("N1502000000101181221170556654243"));
		
//		System.out.println("离场通知：" + noticeExit("20181217001", "苏A22388", "2018-04-16 16:06:05", "2018-04-16 18:06:05"));
	}
	
	/**
	 * 查询车辆绑定以及无感支付签约状态
	 * /car/sign/status
	 	
	 	{
		  "resultCode": 0,
		  "description": "string",
		  "data": {
		    "status": 1,			车辆代扣状态，0:可代扣;1:不可代扣
			"statusDescription": 4,	车辆绑定以及无感支付签约状态，1:车辆绑定并开通无感支付 2:冻结 3:车辆绑定未开通无感支付 4:车辆未绑定 【当状态为2，3，4时，车辆代扣状态为不可代扣】
			"signTime": 2016-04-16T16:06:05Z 	签约生效时间，如果未签约，返回值为空。UTC时间。例如：2016-04-16T16:06:05Z
		  }
		}

	 * @return
	 */
	public static JSONObject carStatus(String carNum) {
		JSONObject result = new JSONObject();
		//一定要按照key的字母表顺序插入
		JSONObject req = new JSONObject();
			req.put("appId", Config.yinlian_appId);
			req.put("plateNumber", carNum);		//车牌号
			req.put("timestamp", System.currentTimeMillis() / 1000);
//		System.out.println("加密前：" + req.toString());
		
		result = get("/car/sign/status", req);
		
		//如果没有签约时间，则增加一个空字段
		if(!result.optJSONObject("data").containsKey("signTime")) {
			result.optJSONObject("data").put("signTime", "");
		}
		
		return result;
	}
	
	/**
	 * 车辆入场通知
	 * /notice/entrance
	 
		{
		  "resultCode": 0,
		  "description": "成功"
		}

	 * @return
	 */
	public static JSONObject noticeEntrance(String syncId, String plateNumber, String startTime) {
		JSONObject result = new JSONObject();
		
		/** 请求参数
		 	{
			  "appId": "string",
			  "data": {
			    "syncId": "string",			//停车记录唯一标识（订单号）
			    "plateNumber": "string",	//车牌号
			    "startTime": 0,				//车辆进入停车场时间。UTC时间。例如：2018-03-16T16:06:05Z
			    "parkId": "string",		//运营商分配给停车场的id，由运营商定义
			    "parkName": "string"	//运营商分配给停车场的名称，由运营商定义
			  },
			  "timestamp": 0,
			  "signature": "string"
			}
		 */
		
		//一定要按照key的字母表顺序插入
		JSONObject req = new JSONObject();
		req.put("appId", Config.yinlian_appId);
		
		JSONObject data = new JSONObject();
			data.put("syncId", syncId);
			data.put("plateNumber", plateNumber);
			data.put("startTime", startTime);
			data.put("parkId", Config.park_id);
			data.put("parkName", "测试停车场");
		req.put("data", data);
		
		req.put("timestamp", System.currentTimeMillis() / 1000);
//		System.out.println("加密前：" + req.toString());
		
		result = post("/notice/entrance", req);
		
		return result;
	}
	
	/**
	 * 查询订单状态
	 * /pay/status
	 
		{
		  "resultCode": 0,
		  "description": "string",
		  "data": {
		    "orderId": "string",	订单号，由运营商生成
		    "upOrderId": "string",	全渠道订单号
		    "payAmount": 0,			订单金额，精确到分。
		    "orderDate": 0,			订单支付时间。UTC时间。例如：2018-03-16T16:06:05Z
		    "payStatus": 0,			订单状态。
										0: 支付成功;
										1: 支付中;
										2: 支付失败;
										3: 退款中;
										4: 退款成功;
										5: 退款失败
		    "reserved": {
		      "discountAmount": 0	优惠金额，精确到分，以分为单位
		    }
		  }
		}

	 * @return
	 */
	public static JSONObject payStatus(String orderId) {
		JSONObject result = new JSONObject();
		
		//一定要按照key的字母表顺序插入
		JSONObject req = new JSONObject();
			req.put("appId", Config.yinlian_appId);
			req.put("orderId", orderId);		//订单号
			req.put("timestamp", System.currentTimeMillis() / 1000);
//		System.out.println("加密前：" + req.toString());
		
		result = get("/pay/status", req);
		
		return result;
	}
	
	/**
	 * 车辆离场通知
	 * /notice/exit
	 
		{
		  "resultCode": 0,
		  "description": "成功"
		}

	 * @return
	 */
	public static JSONObject noticeExit(String syncId, String plateNumber, String startTime, String endTime) {
		JSONObject result = new JSONObject();
		
		/** 请求参数
		 	
			{
			  "appId": "string",
			  "data": {
			    "syncId": "string",			停车记录唯一标识
			    "plateNumber": "string",	车牌号
			    "startTime": 0,				车辆进入停车场时间。UTC时间。例如：2018-03-16T16:06:05Z
			    "endTime": 0,				车辆离开停车场时间。UTC时间。例如：2018-03-16T16:06:05Z
			    "parkId": "string",			运营商分配给停车场的id，由运营商定义
			    "parkName": "string"		运营商分配给停车场的名称，由运营商定义
			  },
			  "timestamp": 0,
			  "signature": "string"
			}

		 */
		
		//一定要按照key的字母表顺序插入
		JSONObject req = new JSONObject();
		req.put("appId", Config.yinlian_appId);
		
		JSONObject data = new JSONObject();
			data.put("syncId", syncId);
			data.put("plateNumber", plateNumber);
			data.put("startTime", startTime);
			data.put("endTime", endTime);
			data.put("parkId", Config.park_id);
			data.put("parkName", Config.park_name);
		req.put("data", data);
		
		req.put("timestamp", System.currentTimeMillis() / 1000);
//		System.out.println("加密前：" + req.toString());
		
		result = post("/notice/exit", req);
		
		return result;
	}
	
	/**
	 * 推送订单
	 * /pay/bill
	 
		{
		  "resultCode": 0,
		  "description": "成功"
		}

	 * @return
	 */
	public static JSONObject payBill(JSONObject param) {
		JSONObject result = new JSONObject();
		
		String syncId = param.optString("syncId");
		String plateNumber = param.optString("plateNumber");
		String orderId = param.optString("orderId");
		int payAmount = param.optInt("payAmount");
		int serviceAmount = param.optInt("serviceAmount");
		String orderDate = param.optString("orderDate");
		String startTime = param.optString("startTime");
		String endTime = param.optString("endTime");
		
		/** 请求参数
		 	
			{
			  "appId": "string",
			  "industryCode": "string",
			  "data": {
			    "syncId": "string",			停车记录唯一标识
			    "plateNumber": "string",	车牌号
			    "orderId": "string",		订单号，由运营商生成
			    "payAmount": 0,				订单金额，精确到分，以分为单位。用户支付到运营商实际金额（比如运营商扣除优惠劵，代金券等）
			    "serviceAmount": 0,			总金额，精确到分，以分为单位。用户在运营商产生的总费用
			    "orderDate": 0,				订单生成时间。UTC时间。例如：2018-03-16T16:06:05Z
			    "startTime": 0,				车辆进入停车场时间。UTC时间。例如：2018-03-16T16:06:05Z	
			    "endTime": 0,				车辆离开停车场时间。UTC时间。例如：2018-03-16T16:06:05Z
			    "parkId": "string",			运营商分配给停车场的id，由运营商定义
			    "parkName": "string",		运营商分配给停车场的名称，由运营商定义
			    "payCallback": "string",	支付完成后，智慧通行平台向此url推送支付结果
			    "accSplitData": "string"	分账域，该字段上传时须将Json转义,相关字段说明：1. accSplitType 分账类型 2. accSplitRuleId 分账规则ID （分账类型为2时才出现，15位字母或数字，分账规则由商户通过平台配置来完成） 3. accSplitMchts 分账对象组（分账类型为1时才出现，accSplitMcht数组，最多支持5个分账对象，该对象包含以下两个字段【accSplitMerId 分账二级商户代码，由一级商户通过商户服务门户添加的15位商户代码，accSplitAmt 分账入账金额，精确到分】）
												JSON格式说明：
												
												accSplitType=1时：
												{"accSplitType":"1","accSplitMchts":[{"accSplitMerId":"123456789012341","accSplitAmt":"100"},{"accSplitMerId":"123456789012342","accSplitAmt":"200"}]}
												
												accSplitType=2时：
												{"accSplitType":"2","accSplitRuleId":"123456789012345"}
			  },
			  "timestamp": 0,
			  "signature": "string"
			}

		 */
		
		//一定要按照key的字母表顺序插入
		JSONObject req = new JSONObject();
		req.put("appId", Config.yinlian_appId);
		req.put("industryCode", Config.yinlian_appId);
		
		JSONObject data = new JSONObject();
			data.put("syncId", syncId);
			data.put("plateNumber", plateNumber);
			data.put("orderId", orderId);
			data.put("payAmount", payAmount);
			data.put("serviceAmount", serviceAmount);
			data.put("orderDate", orderDate);
			data.put("startTime", startTime);
			data.put("endTime", endTime);
			data.put("parkId", Config.park_id);
			data.put("parkName", Config.park_name);
			
			data.put("payCallback", Config.yinlian_paycallback);
			
//			//分账域
//			JSONObject accSplitData = new JSONObject();
//				accSplitData.put("accSplitType", "1");
//				
//				JSONArray accSplitMchts = new JSONArray();
//					JSONObject accSplitMcht = new JSONObject();
//						accSplitMcht.put("accSplitMerId", "123456789012341");
//						accSplitMcht.put("accSplitAmt", "100");
//				accSplitMchts.add(accSplitMcht);
//				accSplitData.put("accSplitMchts", accSplitMchts);
//			data.put("accSplitData", accSplitData);
			
			data.put("accSplitData", "");
		req.put("data", data);
		
		req.put("timestamp", System.currentTimeMillis() / 1000);
//		System.out.println("加密前：" + req.toString());
		
		result = post("/pay/bill", req);
		
		return result;
	}
	
	/**
	 * 申请退款
	 * /notice/entrance
	 
		{
		  "resultCode": 0,
		  "description": "string",
		  "data": {
		    "orderId": "string",		订单号，由高速业务运营商生成
		    "upRefundId": "string"		由智慧通行平台生成的退款流水号
		  }
		}

	 * @return
	 */
	public static JSONObject refundBill(String plateNumber, int refundAmount, String refundId, String orderId) {
		JSONObject result = new JSONObject();
		
		/** 请求参数
		 	{
			  "appId": "string",
			  "data": {
			    "plateNumber": "string",		车牌号
			    "refundAmount": 0,				实际退款金额，精确到分，以分为单位。如果存在优惠活动必须以订单金额全额退款
			    "refundId": "string",			退款流水号，由运营商生成	
			    "orderId": "string",			已经支付成功的订单号，由运营商生成
			    "refundCallback": "string"		退款完成后，运营商接收退款结果通知的url
			  },
			  "timestamp": 0,
			  "signature": "string"
			}
		 */
		
		//一定要按照key的字母表顺序插入
		JSONObject req = new JSONObject();
		req.put("appId", Config.yinlian_appId);
		
		JSONObject data = new JSONObject();
			data.put("plateNumber", plateNumber);
			data.put("refundAmount", refundAmount);
			data.put("refundId", refundId);
			data.put("orderId", orderId);
			data.put("refundCallback", Config.yinlian_refundcallback);
		req.put("data", data);
		
		req.put("timestamp", System.currentTimeMillis() / 1000);
//		System.out.println("加密前：" + req.toString());
		
		result = post("/refund/bill", req);
		
		return result;
	}
	
	 //1.等待支付中，0.线下支付，2.银联支付
    public void autoTask(String orderId){
    	if(YL.orderMap.containsKey(orderId)) {
    		int status = YL.orderMap.get(orderId);
    		if(status == 1) {
    			
    			YL.orderMap.put(orderId, 0);
    		}else{
    			YL.orderMap.remove(orderId);
    		}
    	}
    }
	
	
	
	/**
	 * 发送get请求
	 * @param url	地址
	 * @param param	参数
	 * @return
	 */
	public static JSONObject get(String methodName, JSONObject param) {
		//公共参数，为所有请求增加加密信息
		String signature = "";
		try {
			signature = HMacMD5.encodeMac(param.toString(), Config.yinlian_sign);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		param.put("signature", signature);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(Config.yinlian_baseUrl + methodName + getParam(param));
//		System.out.println("发起请求: " + get.getURI());
        try {
        	JSONObject result = JSONObject.fromObject(EntityUtils.toString(httpClient.execute(get).getEntity(), Consts.UTF_8));
//        	System.out.println("原始数据：" + result.toString());
        	
    		return result;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * 发送post请求
	 * @param url	地址
	 * @param param	参数
	 * @return
	 */
	public static JSONObject post(String methodName, JSONObject param) {
		//公共参数，为所有请求增加加密信息
		String signature = "";
		try {
			signature = HMacMD5.encodeMac(param.toString(), Config.yinlian_sign);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		param.put("signature", signature);
		
		//发起请求
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(Config.yinlian_baseUrl + methodName);
		
		StringEntity jsonEntity = new StringEntity(param.toString(), Consts.UTF_8);
        jsonEntity.setContentType("application/json");
        post.setEntity(jsonEntity);
        try {
        	JSONObject result = JSONObject.fromObject(EntityUtils.toString(httpClient.execute(post).getEntity(), Consts.UTF_8));
//        	System.out.println("原始数据：" + result.toString());

    		return result;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 拼接参数,格式：?xx=aa&yy=bb
	 * @param json
	 * @return
	 */
	public static String getParam(JSONObject json) {
		for (String key : (Set<String>)json.keySet()) {
			String value = json.getString(key);
			json.put(key, value);
		}
		String one = json.toString();
		String two = "?" + one.substring(1, one.length() - 1).replace(",", "&").replace(":", "=").replace("\"", "");
		return two;
	}
	
}
