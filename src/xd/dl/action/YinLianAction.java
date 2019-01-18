package xd.dl.action;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.web.bind.annotation.RequestBody;

import net.sf.json.JSONObject;
import xd.dl.job.IDongHui;
import xd.dl.yl.YL;
@Action("/yinlian")
public class YinLianAction extends DLBaseAction implements IDongHui {


    @Action("/payResult")
    public String payResult(@RequestBody String requestBody) throws Exception{
    	System.out.println("requestBody:" + requestBody);
    	String result = "";
    	
    	/**
			{
			  "appId": "string",
			  "timestamp": "string",
			  "data": {
			    "syncId": "string",			订单流水号，由运营商生成，考虑同一订单多次支付。例如支付完成后，半小时未出场，根据运营商规则不同，可能会使用同一订单号继续计费。用流水号唯一标识收费操作。
			    "orderId": "string",		订单号，由运营商生成
			    "upOrderId": "string",		全渠道订单号
			    "payAmount": 0,				订单金额，精确到分。
			    "orderDate": 0,				订单支付时间。UTC时间。例如：2018-03-16T16:06:05Z
			    "payStatus": 0,				订单状态。0: 支付成功; 1: 支付中; 2: 支付失败; 3: 退款中; 4: 退款成功; 5: 退款失败
			    "reserved": {
			      "discountAmount": 0		优惠金额，精确到分，以分为单位
			    }
			  },
			  "signature": "string"
			}
    	 */
    	JSONObject data = JSONObject.fromObject(requestBody).optJSONObject("data");
    	
    	String orderId = data.optString("orderId");
    	if(YL.orderMap.containsKey(orderId)) {
    		//1.等待支付中，0.线下支付，2.银联支付
    		int status = YL.orderMap.get(orderId);
    		if(status == 1){
    			//通知C++
    			
    		}else if(status == 0){
    			//已线下支付，退款
    			
    			YL.orderMap.remove(orderId);
    		}else {
    			//重复请求，忽略
    			YL.orderMap.remove(orderId);
    		}
    	}
    	
    	return result;
    }
    
    
}
