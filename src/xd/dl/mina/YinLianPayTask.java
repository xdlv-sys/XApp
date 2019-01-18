package xd.dl.mina;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import xd.dl.yl.YL;

/**
 * 银联，发起无感支付
 * 消息号：32（PROXY_YINLIAN_AUTO_FEE_QUERY）
 */

@Service
public class YinLianPayTask extends Thread {

    int count = 1;			//计数器
    String orderId;			//订单号 	//1.等待支付中，0.线下支付，2.银联支付
    String watchId;			//岗亭ID
    boolean flag = true;	//线程标志位
    
    public YinLianPayTask() {
	}
    
    public YinLianPayTask(String orderId, String watchId, JSONObject data) {
		this.orderId = orderId;
		this.watchId = watchId;
		
		YL.orderMap.put(orderId, 1);
		YL.watchMap.put(orderId, watchId);
		
		YL.orderInfoMap.put(orderId, data);
	}
    
    
//    @Autowired
    ParkHandler parkHandler = new ParkHandler();
    
	@Override
	public void run() {
		System.out.println("[" + orderId + "],开启独立线程");
		//10s后调用
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//开始业务
		do {
			JSONObject orderInfo = YL.orderInfoMap.get(orderId);
			
			if(count <= 5) {
				
				System.out.println("[orderId=" + orderId + "],定时任务开始执行");
				if(YL.orderMap.containsKey(orderId)) {		//1.等待支付中，0.线下支付，2.银联支付
		    		int status = YL.orderMap.get(orderId);
		    		System.out.println("[orderId=" + orderId + "],缓存中有订单，状态是[" + status + "]");
		    		if(status == 1) {
		    			/**
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
		    			 */
		    			
		    			//查询订单状态
		    			JSONObject order = YL.payStatus(orderId);
		    			//只有通讯成功的时候才进行处理
		    			if (order.optInt("resultCode") == 0) {
		    				//测试先注释
			    			JSONObject orderData = order.optJSONObject("data");
//			    			JSONObject orderData = new JSONObject();
//			    				orderData.put("orderId", orderId);
//			    				orderData.put("upOrderId", "qqd" + orderId);
//			    				orderData.put("payAmount", 0.5);
//			    				orderData.put("orderDate", "2018-12-22 10:57:52");
//			    				orderData.put("payStatus", 2);
//			    				JSONObject reserved = new JSONObject();
//			    				reserved.put("discountAmount", 0);
//			    				orderData.put("reserved", reserved);
			    			
			    			int payStatus = orderData.optInt("payStatus");	// 0: 支付成功; 1: 支付中; 2: 支付失败; 3: 退款中; 4: 退款成功; 5: 退款失败
			    			System.out.println("[orderId=" + orderId + "]，支付结果[" + payStatus + "]");
			    			if(payStatus == 0 ) {	
			    				//支付成功，响应给C++
			    				parkHandler.payResultYl(ParkProxy.YINLIAN_PAY_RESULT, orderInfo.optString("plateNumber"), ((float)orderData.optInt("payAmount")) / 100, orderId, 
			    							orderInfo.optString("syncId"), orderData.optString("upOrderId"), ((float)orderData.optJSONObject("reserved").optInt("discountAmount")) / 100, payStatus, watchId);
			    				
			    				YL.orderMap.put(orderId, 2);
			    				//取消任务
			    				flag = false;
							}else if(payStatus == 2) {
								//支付失败，响应给C++
								parkHandler.payResultYl(ParkProxy.YINLIAN_PAY_RESULT, orderInfo.optString("plateNumber"), ((float)orderData.optInt("payAmount")) / 100, orderId, 
										orderInfo.optString("syncId"), orderData.optString("upOrderId"), ((float)orderData.optJSONObject("reserved").optInt("discountAmount")) / 100, payStatus, watchId);
								
			    				YL.orderMap.put(orderId, 0);
			    				//取消任务
			    				flag = false;
							}
						}else{
							//查询订单异常，忽略
			    			System.out.println("[orderId=" + orderId + "][resultCode=" + order.optInt("resultCode") + "][错误描述：" + order.optString("description") + "]，本次跳过");
						}
		    			
		    		}else{
		    			//订单已被支付，退出任务
		    			System.out.println("[orderId=" + orderId + "][status=" + status + "]订单已被支付，退出任务");
		    			YL.orderMap.remove(orderId);
		    			flag = false;
		    		}
		    	}else{
		    		//缓存中没有此订单，退出任务
	    			System.out.println("[orderId=" + orderId + "]缓存中没有此订单，退出任务");
	    			flag = false;
		    	}
				
				count++;
				//以2s的间隔去查询
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				YL.orderMap.put(orderId, 0);
				System.out.println("[orderId=" + orderId + "][缓存中状态设置为=" + YL.orderMap.get(orderId) + "]这里是超时");
				flag = false;

				//通知C++支付失败（超时）
				parkHandler.payResultYl(ParkProxy.YINLIAN_PAY_RESULT, orderInfo.optString("plateNumber"), 0.0f, orderId, orderInfo.optString("syncId"), "", 0.0f, 2, watchId);
				
			}
		
		} while (flag);
	}

    
}
