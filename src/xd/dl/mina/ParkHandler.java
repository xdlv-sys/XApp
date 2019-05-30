package xd.dl.mina;


import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xd.dl.job.ParkedCarInfo;
import xd.fw.HttpClientTpl;
import xd.fw.mina.tlv.ReversedHandler;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ParkHandler extends ReversedHandler {
    @Autowired
    UploadFreeJob uploadFreeJob;
    @Autowired
    OutProcess outProcess;

    @Autowired
    EnterProcess enterProcess;
    @Autowired
    SigIn sigIn;
    @Autowired
    SigOut sigOut;
    @Autowired
    Thumb thumb;
    @Autowired
    AutoPay autoPay;
    @Autowired
    EnterProcess2 enterProcess2;
    @Autowired
    OutProcess2 outProcess2;
    @Autowired
    WatchProcess watchProcess;
    @Autowired
    ChargeStandard chargeStandard;
    @Autowired
    CarOutProcess carOutProcess;
    @Autowired
    YlPay ylPay;
    @Autowired
    YlRefund ylRefund;
    @Autowired
    YlQuery ylQuery;
    /**
     * 金鹰停车场--岗亭支付接口①
     * 消息号：25(PROXY_JINYING_VIP_PAY)
     */
    @Autowired
    JinyingVipPay jinyingVipPay;
    /**
     * 金鹰停车场--金鹰岗亭VIP卡支付停车缴费时间冲正
     * 消息号：26(PROXY_JINYING_VIP_UNDO)
     */
    @Autowired
    JinyingVipUndo jinyingVipUndo;
    
    /**
     * 银联，平台查询车辆绑定状态
     * 消息号：30（PROXY_YINLIAN_QUERY_CAR_STATUS）
     */
    @Autowired
    YinLianQueryCarStatus yinLianQueryCarStatus;
    /**
     * 银联，入场
     * 消息号：31（PROXY_YINLIAN_CAR_IN）
     */
    @Autowired
    YinLianCarIn yinLianCarIn;
    /**
     * 银联，发起无感支付
     * 消息号：32（PROXY_YINLIAN_AUTO_FEE_QUERY）
     */
    @Autowired
    YinLianAutoFeeQuery yinLianAutoFeeQuery;
    /**
     * 银联，离场
     * 消息号:34(PROXY_YINLIAN_CAR_OUT)
     */
    @Autowired
    YinLianCarOut yinLianCarOut;
  
    
    @Value("${http-timeout:5000}")
    int httpTimeout;

    public static int freeCount = 0;

    @PostConstruct
    void init() {
        HttpClientTpl.setTimeOut(httpTimeout);
    }

    @Override
    protected void handlerRegistry(TLVMessage msg, IoSession session) {
        freeCount = (int)msg.getNextValue(1);
        handlerMessage(msg, session);
    }

    @Override
    protected boolean handlerMessage(TLVMessage msg, IoSession session) {
        int code = (int) msg.getValue();
        SendRequest sendRequest;
        switch (code) {
            case 0:
                sendRequest = uploadFreeJob;
                break;
            case 1:
                sendRequest = enterProcess;
                break;
            case 2:
                sendRequest = outProcess;
                break;
            case 5:
                sendRequest = sigIn;
                break;
            case 6:
                sendRequest = sigOut;
                break;
            case 7:
                sendRequest = thumb;
                break;
            case 8:
                sendRequest = autoPay;
                break;
            case 10:
                sendRequest = enterProcess2;
                break;
            case 11:
                sendRequest = outProcess2;
                break;
            case 12:
                sendRequest = watchProcess;
                break;
            case 13:
                sendRequest = chargeStandard;
                break;
            case 20:
                sendRequest = carOutProcess;
                break;
            case 21:
                sendRequest = ylPay;
                break;
            case 22:
                sendRequest = ylRefund;
                break;
            case 23:
                sendRequest = ylQuery;
                break;
            /**
             * 金鹰停车场--岗亭支付接口①
             * 消息号：25(PROXY_JINYING_VIP_PAY)
             */
            case 25:
                sendRequest = jinyingVipPay;
                break;
            /**
             * 金鹰停车场--金鹰岗亭VIP卡支付停车缴费时间冲正
             * 消息号：26(PROXY_JINYING_VIP_UNDO)
             */
            case 26:
                sendRequest = jinyingVipUndo;
                break;
            case 30:
            	/**
            	 * 银联，平台查询车辆绑定状态
            	 * 消息号：30（PROXY_YINLIAN_QUERY_CAR_STATUS）
            	 */
            	sendRequest = yinLianQueryCarStatus;
            	break;
            case 31:
            	 /**
                 * 银联，入场
                 * 消息号：31（PROXY_YINLIAN_CAR_IN）
                 */
            	sendRequest = yinLianCarIn;
            	break;
            case 32:
            	/**
                 * 银联，发起无感支付
                 * 消息号：32（PROXY_YINLIAN_AUTO_FEE_QUERY）
                 */
            	sendRequest = yinLianAutoFeeQuery;
            	break;
            case 34:
            	/**
            	 * 银联，离场
            	 * 消息号:34(PROXY_YINLIAN_CAR_OUT)
            	 */
            	sendRequest = yinLianCarOut;
            	break;
            default:
                return false;
        }
        if (!sendRequest.run()) {
            ret(code, msg, sendRequest, null, session);
            return true;
        }

        String[][] params;
        try {
            params = sendRequest.constructParams(msg.getNext());
        } catch (Exception e) {
            logger.error("", e);
            return true;
        }
        long id = System.currentTimeMillis();

        JSONObject jsonObject;
        String json = null;
        try {
            String requestJson = sendRequest.json(msg.getNext(), session);
            if (requestJson != null) {
                logger.info("before json {}:{}", id, requestJson);
                json = HttpClientTpl.postJson(sendRequest.svrAddress(), requestJson
                        , sendRequest.heads());
            } else {
                logger.info("before http {}:{}", id, ArrayUtils.toString(params));
                json = HttpClientTpl.post(sendRequest.svrAddress(), params);
            }

            jsonObject = JSONObject.fromObject(json);
        } catch (Exception e) {
            logger.error("error json:" + json, e);
            jsonObject = new JSONObject();
            jsonObject.put("msg", "network failure");
            jsonObject.put("code", -1);
        }

        logger.info("return from http {}: {}", id, jsonObject);

        ret(code, msg, sendRequest, jsonObject, session);
        return true;
    }

    private void ret(int code, TLVMessage msg, SendRequest sendRequest
            , JSONObject jsonObject, IoSession session) {
        TLVMessage ret = new TLVMessage(code);
        if (jsonObject == null){
            jsonObject = new JSONObject();
            jsonObject.put("msg", "not run");
            jsonObject.put("code", -2);
        }
        sendRequest.constructMessage(ret.setNext(generateId()), msg.getNext(), jsonObject, session);
        logger.info("return: {}", ret);
        session.write(ret);
    }

    public ParkedCarInfo queryCarInfo(int code, String watchId, String carNumber) {
        TLVMessage message = createRequest(code, carNumber);
        logger.info("start query car by wh: " + message);
        TLVMessage ret = request(watchId, message);
        logger.info("end query car by wh: " + ret);
        ParkedCarInfo parkedInfo = new ParkedCarInfo();
        if (ret != null && 200 == (int) ret.getValue()) {
            parkedInfo.sCarLicense = (String) ret.getNextValue(0);
            parkedInfo.fMoney = (float) ret.getNextValue(1);
            parkedInfo.sInTime = (String) ret.getNextValue(2);
            parkedInfo.iParkedTime = (int) ret.getNextValue(3);
            parkedInfo.sID = (String) ret.getNextValue(4);
            return parkedInfo;
        }
        return null;
    }

    public boolean noCardEntry(int code, String watchId, String userId) {
        TLVMessage message = createRequest(code, userId);
        TLVMessage ret = request(watchId, message);
        return ret != null && 200 == (int) ret.getValue();
    }

    public boolean payFee(int code, String watchId, String carNumber, float money, 
    					int _DiscountType,int DiscountType, String DiscountDetail, float DiscountFee, 
    					String CardId, String ParkID) {
    	
    	System.out.println("-----------【公司岗亭支付成功通知】-----------");
    	System.out.println("[优惠记录唯一ID]：[与订单编号相同，这里只提供-1]>>>"
        		+"[优惠名称]：["+DiscountDetail+"]>>>"
        		+"[兑换后类型]：[3.免金额券]>>>"
        		+"[免费时长]：[0]>>>"
        		+"[免费金额]：["+DiscountFee+"]>>>"
        		+"[券有效开始时间]：[1900-01-01 00:00:00]>>>"
        		+"[券有效结束时间]：[2999-01-01 00:00:00]>>>"
        		+"[停车场编号]：["+ParkID+"]>>>"
        		+"[会员卡卡号]：["+CardId+"]>>>"
        		+"[会员卡类型]：["+(_DiscountType)+"]>>>"
        		+"[优惠类型]：["+DiscountType+"]");
    	
        TLVMessage message = createRequest(code, carNumber, money
        		, -1, DiscountDetail, 3, 0, DiscountFee
        		, "1900-01-01 00:00:00", "2999-01-01 00:00:00"
        		, ParkID, CardId, _DiscountType, DiscountType);
        
        System.out.println("组织后的TLV数据："+message.toString());
        System.out.println("--------------------------------------------");
        
        TLVMessage ret = request(watchId, message);
        return ret != null && 200 == (int) ret.getValue();
    }

    @Async
    public void notifyWatchIdPayFee(int code, String carNumber, float parkingPrice, String orderNo, String memberCode, int leavel, float DiscountFee) {
        
    	logger.info("-----------【公司平台订单在线支付】-----------");
    	logger.info("[消息号]：["+code+"]>>>"
        		+"[时间ID]：[?]>>>"
        		+"[返回结果]：[200]>>>"
        		+"[返回的错误提示]：[OK]>>>"
        		+"[车牌号]：["+carNumber+"]>>>"
        		+"[线上实付费用]：["+parkingPrice+"]>>>"
        		+"[订单号]：["+orderNo+"]>>>"
        		+"[会员编号]：["+memberCode+"]>>>"
        		+"[是否允许自动离场]：["+leavel+"]>>>"
        		+"[会员优惠金额]：["+DiscountFee+"]");
    	
    	TLVMessage message = createRequest(code, 200, "OK", carNumber, parkingPrice, orderNo, memberCode, leavel, DiscountFee);
        
        logger.info("组织后的TLV数据："+message.toString());
        logger.info("--------------------------------------------");
        
        List<TLVMessage> messages = notifyAllId(message);
        for (TLVMessage m : messages) {
            if (m != null && 200 == (int) m.getValue()) {
                logger.info("notify wh successfully:", m);
            } else {
                logger.warn("fail to notify wh:", m);
            }
        }
    }
    
    //金鹰支付完成，通知岗亭支付结果
    public void proxyJinyingPrePayNotice(int code, int iPayMethod, String pPayWay, String pLicense, String sShopNo, String pPaySeq, 
			int iDayTime, int iNightTime, float fDayMny, float fNightMny, String sOrderTime, String pPayTime) {
        
    	
    	System.out.println("-----------【金鹰支付结果，通知岗亭】-----------");
//    	魔数 //见tlv格式说明
//    	消息总长度 //见tlv格式说明
//    	消息号 int;
//    	时间ID string;
//    	版本号 int;//填1
//    	返回结果 int;
//    	返回的错误提示 string;
//    	车牌号 string;
//    	支付流水号 string
//    	支付方式 string
//    	支付白天停车时间 int
//    	支付夜间停车时间 int
//    	支付白天停车金额 float
//    	支付夜间停车金额 float
//    	双方交易同步时间 string
    	
    	TLVMessage message = createRequest(code, 1, 1, "", pLicense, pPaySeq, pPayWay, iDayTime, iNightTime, fDayMny, fNightMny, pPayTime);
        
    	System.out.println("组织后的TLV数据："+message.toString());
    	System.out.println("--------------------------------------------");
        
        List<TLVMessage> messages = notifyAllId(message);
        for (TLVMessage m : messages) {
            if (m != null && 1 == (int) m.getValue()) {
                logger.info("金鹰支付完成，通知岗亭支付结果：", m);
                System.out.println("金鹰支付完成，通知岗亭支付结果[成功]");
            } else {
                logger.warn("金鹰支付完成，通知岗亭支付结果：", m);
                System.out.println("金鹰支付完成，通知岗亭支付结果[失败]");
            }
        }
    }
    
    public boolean payResultYl(int code, String plateNumber, float payAmount, String orderId, String syncId, String upOrderId, float discountAmount, int payStatus, String watchId) {

    	System.out.println("-----------【银联，支付结果】-----------");
    	
//    	魔数 //见tlv格式说明
//    	消息总长度 //见tlv格式说明
//    	消息号 int;
//    	时间ID string;
//    	版本号 int;//填1
//    	返回结果 int;//传0
//    	返回的错误提示 string;//传空
//    	车牌号 string;
//    	订单金额 float;
//    	订单号 string;
//    	请求唯一序列号(订单流水号) string;
//    	全渠道订单号 string; 
//    	优惠金额 float;
//    	订单状态 int;
    	
    	/* 业务数据要从版本号开始拼接  */

    	TLVMessage message = createRequest(code, 1, 0, "", plateNumber, payAmount, orderId, syncId, upOrderId, discountAmount, payStatus);

    	System.out.println("组织后的TLV数据："+message.toString());
    	System.out.println("--------------------------------------------");
        
        TLVMessage ret = request(watchId, message);
        System.out.println("拿到C++响应结果：" + ret);
        return true;
//        return ret != null && 200 == (int) ret.getValue();
    }

    @Async
    public void notifyWatchIdPassCar(int code, String carNumber, String userName, float price, String remark, String parkId, String dbId) {
        TLVMessage message = createRequest(code, 1, "", carNumber, userName, userName, price, remark, parkId, dbId);
        notifyAllId(message);
    }
}
