package xd.dl.mina;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.derby.iapi.services.io.ArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.dl.job.*;
import xd.dl.yl.YL;
import xd.fw.FwUtil;
import xd.fw.mina.tlv.ReversedProxy;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.Date;

@Service
public class ParkProxy extends ReversedProxy {
    Logger logger = LoggerFactory.getLogger(ReversedProxy.class);

    public static final int QUERY_CAR = 3, QUERY_CAR_JY = 23, PAY_FEE = 4, PAY_FEE_JY = 27, QUERY_CAR2 = 13, 
    		PAY_FEE_NOTIFY = 9, CHARGE_NOTIFY = 15, NO_CARD_ENTRY = 16, DISPATCH_COUPON = 17, 
    		SIMILAR_CAR_NUMBER = 18, PAY_FEE_NOTIFY_HJC = 19, PAY_RESULT_YL = 28, CHECK_IN_JY = 29, CHECK_OUT_JY = 30, YINLIAN_PAY_RESULT = 33,
    		PROXY_JINYING_PRE_PAY_NOTICE = 37;

    @Autowired
    ParkNativeBean parkNativeBean;

    @Autowired
    ParkHandler parkHandler;

    @Value("${center_ip}")
    String host;
    @Value("${center_port}")
    int port;

    @Value("${park_id}")
    String parkId;

    @Value("${version}")
    int version;

    @Value("${db_host}")
    String dbHost;
    @Value("${db_name}")
    String dbName;
    @Value("${db_user}")
    String user;
    @Value("${db_pwd}")
    String pwd;
    @Value("${park_name}")
    String parkName;
    @Value("${db_load:false}")
    boolean dbLoad = false;

    @PostConstruct
    public void init() {
        if (dbLoad) {
            ParkNative.initialized(dbHost, dbName, user, pwd);
        }
    }

    @PreDestroy
    public void destroy() {
        super.destroy();
        logger.info("un initialize park native");
        ParkNative.unitialized();
    }

    @Override
    protected void constructRegistryMessage(TLVMessage registryMessage) {
        registryMessage.setNext(parkId).setNext(parkName).setNext(ParkHandler.freeCount).setNext(version);
    }

    @Override
    protected InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    private byte[] picBuffer = new byte[2 * 1024 * 1024];
    private ArrayOutputStream aos = new ArrayOutputStream(picBuffer);

    @Override
    protected void handlerQuery(TLVMessage msg) throws Exception {
        logger.info("receive:" + msg);
        //2->1471225285910->苏A12345->001(parkId)->0(carType)->1 (carOrder)
        //reuse msg
        int code = (int) msg.getValue();
        TLVMessage next = msg.getNext(0);
        switch (code) {
            case QUERY_CAR:
                String carNumber = (String) msg.getNextValue(1);
                String watchId = (String) msg.getNextValue(2);
                ParkedCarInfo parkedCarInfo = null;
                if (StringUtils.isNotBlank(watchId)) {
                    parkedCarInfo = parkHandler.queryCarInfo(QUERY_CAR, watchId, carNumber);
                } else {
                    byte carType = (byte) msg.getNextValue(3);
                    byte carOrder = (byte) msg.getNextValue(4);
                    String dbId = (String) msg.getNextValue(5);
                    String enterTime = (String) msg.getNextValue(6, "");
                    if (StringUtils.isBlank(dbId)) {
                        parkedCarInfo = parkNativeBean.getParkedCarInfo("", carType, carNumber, 15, 1, carOrder, "", "");
                    } else {
                        parkedCarInfo = parkNativeBean.getParkedCarInfo("", carType, carNumber, 15, 2, carOrder, dbId, enterTime);
                    }
                }
                if (parkedCarInfo != null && parkedCarInfo.iReturn != 6
                        && parkedCarInfo.iReturn != 11) {
                    float scale = (float) msg.getNextValue(7);

                    TLVMessage tmp = next.setNext(parkedCarInfo.sCarLicense).setNext(parkedCarInfo.fMoney
                    ).setNext(parkedCarInfo.sInTime
                    ).setNext(parkedCarInfo.iParkedTime).setNext(parkedCarInfo.sID);

                    File picFile;
                    if (scale > 0 && StringUtils.isNotBlank(parkedCarInfo.sInPic)
                            && (picFile = new File(parkedCarInfo.sInPic)).exists()) {
                        aos.setPosition(0);
                        Thumbnails.of(picFile).scale(scale).toOutputStream(aos);
                        byte[] data = new byte[aos.getPosition()];
                        System.arraycopy(picBuffer, 0, data, 0, data.length);
                        tmp.setNext(data);
                        logger.info("compress:{},{}", picFile.length(), data.length);
                    }
                } else {
                    //just return null
                    next.setNext(NULL_MSG);
                }
                response(msg);
                break;
        case QUERY_CAR_JY:
        	carNumber = (String) msg.getNextValue(1);
//            parkId = (String) msg.getNextValue(2);
            
            System.out.println("23号查车命令QUERY_CAR_JY:" + carNumber);
            
            ParkedCarInfoJy parkedCarInfoJy = null;
            
            parkedCarInfoJy = parkNativeBean.getParkedCarInfoJy("", 0, carNumber, 15, 3, 0, "", "");
            if (parkedCarInfoJy != null && parkedCarInfoJy.iReturn != 6 && parkedCarInfoJy.iReturn != 11) {
            	next.setNext(parkedCarInfoJy.sInTime).setNext(parkedCarInfoJy.fMoney).setNext(parkedCarInfoJy.iParkedTime).setNext(parkedCarInfoJy.sInPic).setNext(parkedCarInfoJy.sCarLicense).setNext(parkedCarInfoJy.sID)
            	.setNext(parkedCarInfoJy.sStartTime).setNext(parkedCarInfoJy.sEndTime).setNext(parkedCarInfoJy.sShopNo).setNext(parkedCarInfoJy.sPosNo).setNext(parkedCarInfoJy.iDayTime).setNext(parkedCarInfoJy.iNightTime)
            	.setNext(parkedCarInfoJy.fDayMny).setNext(parkedCarInfoJy.fNightMny).setNext(parkedCarInfoJy.iReturn);
            	
            } else {
                //just return null
                next.setNext(NULL_MSG);
            }
            
            response(msg);
            break;
            case PAY_FEE:
            	//2018-09-13 by jinyi 原TLV以下：
            	//PAY_FEE-->(1)CarNumber-->(2)TotalFee（实际支付金额）-->(3)TimeStamp-->(4)WatchId-->(5)CarType-->(6)SId-->(7)EnterTime
            	//在后面追加：
            	//-->(8)DiscountType-->(9)DiscountDetail-->(10)DiscountFee-->(11)CardId
            	//-->(12)ParkId

           	 //2018-09-13 by jinyi 开始与代理联通优惠券信息
            	System.out.println("-----------[PAY_FEE]代理从平台接收新的数据结构-----------");
            	System.out.println(msg.toString());
            	System.out.println("--------------------------------------------------");
            	
                carNumber = (String) msg.getNextValue(1);
                float totalFee = (float) msg.getNextValue(2);
                String timeStamp = (String) msg.getNextValue(3, "");
                watchId = (String) msg.getNextValue(4);
                
                //2018-09-13 by jinyi，开始获得平台传送的四个参数
                int DiscountType = 0;
                String DiscountDetail = "";
                float DiscountFee = 0;
                String CardId = "";
                try {
                	DiscountType = (Integer) msg.getNextValue(8, "");
                	DiscountDetail = (String) msg.getNextValue(9, "");
                	DiscountFee = (float) msg.getNextValue(10, "");
                	CardId = (String) msg.getNextValue(11, "");
				} catch (Exception e) {
					
				}
                
                
                //优惠类型，默认是0
                int _DiscountType = 0;
                if(DiscountType == 3){
                	//优惠类型是金卡，翻译成接口的“金卡”
                	_DiscountType = 1;
                }else if(DiscountType == 4){
                	//优惠类型是预付卡，翻译成接口的“预付卡”
                	_DiscountType = 3;
                	//预付卡时，DiscountDetail在平台直接存储的是流水号，这里需要组织成：[预付卡]-[流水号:****]
                	DiscountDetail = "[预付卡]-[流水号:"+DiscountDetail+"]";
                }else if(DiscountType == 1 || DiscountType == 2){
                	//优惠类型是积分或银卡，翻译成接口的“银卡”
                	_DiscountType = 2;
                }
                
                System.out.println("-----------[DiscountType]"+_DiscountType+"-[DiscountDetail]"+DiscountDetail+"-[DiscountFee]"+DiscountFee+"-[CardId]"+CardId+"-----------");
                
                //2018-09-14 by jinyi，开始获得平台传送的两个个参数
                String ParkId = (String) msg.getNextValue(12, "");
                System.out.println("-----------[ParkId]"+ParkId+"-----------");
                
                boolean success;
                if (StringUtils.isNotBlank(watchId)) {
                	
                	//2018-09-13 by jinyi，岗亭支付，在payFee中详细描述参数情况
                    success = parkHandler.payFee(PAY_FEE, watchId, carNumber, totalFee, _DiscountType, DiscountType, DiscountDetail, DiscountFee, CardId, ParkId);
                    
                } else {
                    byte carType = (byte) msg.getNextValue(5);
                    String sId = (String) msg.getNextValue(6, "");
                    String searchTime = (String) msg.getNextValue(7, "");
                    
                    
                    System.out.println("-----------【公司预支付成功通知】-----------");
                    
                  //2018-09-13 by jinyi，新增记录会员支付接口
                    /************************************************************************/
                    /*插入商场券*/
                    /* pOrderNo 预留东汇订单号,传空字符串，不是NULL*/
                    /* pLicense要插入的车牌*/
                    /*sCouID优惠券ID,为只包含数字的整型字符串*/
                    /*pCouName券名,积分兑换、电子券、金卡免费 三种之一*/
                    /*iCouType券类型,积分兑换也当作券处理1.免次券、2.免时长券、3.免金额券*/
                    /*iCouTime减免时间,单位分钟 无填0*/
                    /*fCouMny优惠金额, 单位元, 无填0*/
                    /*pStartDate券生效时间, 如没填1900-01-01 00:00:00*/
                    /*pEndDate券结束时间,如没填当前时间加100年*/
                    /*pCarportID停车场编号*/
                    /*pInID入场表中与券相联系的入场记录ID*/
                    /*pCardNo会员卡号*/
                    /*iCardType卡类型1.金卡、2.银卡*/
                    /*iDisType优惠类型1.积分、2.优惠券、3.金卡免费*/
                    /*iUseType记录用的类型0:只在计算金额时有用1.最终支付时有用的记录,现填1*/
                    /************************************************************************/

                    if(DiscountFee <= 0){
                    	System.out.println("第一步、优惠支付没有使用，跳过调用新接口[dispatchShopCoupon]");
                    }else{
                    	 System.out.println("第一步、调用新接口[dispatchShopCoupon]");
                         
                         ParkedCarInfo carInfo = parkNativeBean.dispatchShopCoupon("", carNumber, "", DiscountDetail, 3, 
                     			0, DiscountFee,"1900-01-01 00:00:00", "2999-01-01 00:00:00",
                     			ParkId, sId, CardId, _DiscountType, DiscountType, 1);
                         System.out.println("第一步调用结果：["+(carInfo != null)+"]"); 
                    }
                    
                    System.out.println("第二步、在原接口[payParkCarFee]中设置最后一个参数[DiscountFee]{"+DiscountFee+"}");
                    //2018-09-13 by jinyi，原payParkCarFee已经支持折扣参数，将优惠价设置成最后一个参数
//                    int ret = parkNativeBean.payParkCarFee("", carType, carNumber
//                            , timeStamp, totalFee, sId, searchTime, 1, "", "", "", "", 0, 0, 0);
                    int ret = parkNativeBean.payParkCarFee("", carType, carNumber
                            , timeStamp, totalFee, sId, searchTime, 1, "", "", "", "", 0, 0, DiscountFee);
                    logger.info("return from park car fee: {}", ret);
                    
                    System.out.println("第二步调用结果：["+(ret == 0)+"]"); 
                    
                    success = ret == 0;
                    if (success) {
                        // notify all station
                        logger.info("notify all stations");
                        
                        System.out.println("第三步、在通知所有岗亭的原接口[notifyWatchIdPayFee]中，设置最后一个参数[DiscountFee]{"+DiscountFee+"}");
                        
                        //2018-09-13 by jinyi，预支付，只需要增加优惠金额一个参数
                        parkHandler.notifyWatchIdPayFee(ParkProxy.PAY_FEE_NOTIFY_HJC, carNumber,totalFee, "", CardId, 0, DiscountFee);
                        
                        System.out.println("第三步调用结束"); 
                    }
                    
                    System.out.println("-----------【公司预支付成功通知】结束！-----------");
                }
                
                
                
                next.setNext(success ? "OK" : "FAIL");
                response(msg);
                break;
	        case PAY_FEE_JY:
	        	System.out.println("27号支付命令,PAY_FEE_JY,金鹰支付");
	        	int v = 1; 
	        	int iPayMethod = (int) msg.getNextValue(v++);
	        	String pPayWay = (String) msg.getNextValue(v++);
	        	int iPayWay = Integer.valueOf(pPayWay);
	        	String pLicense = (String) msg.getNextValue(v++);
	        	String sShopNo = (String) msg.getNextValue(v++);
	        	String pPaySeq = (String) msg.getNextValue(v++);
	        	int iDayTime = (int) msg.getNextValue(v++);
	        	int iNightTime = (int) msg.getNextValue(v++);
	        	float fDayMny = (float) msg.getNextValue(v++);
	        	float fNightMny = (float) msg.getNextValue(v++);
	        	int sOrderTime = (int) msg.getNextValue(v++);
	        	System.out.println("[sOrderTime]" + sOrderTime);
	        	String orderTime = FwUtil.sdf.format(new Date(Long.valueOf(sOrderTime) * 1000));	//转成字符串
	        	String pPayTime = FwUtil.sdf.format(new Date());	//转成字符串
	        	
	        	
	        	System.out.println("[iPayMethod]" + iPayMethod);
	        	System.out.println("[iPayWay]" + iPayWay);
	        	System.out.println("[pLicense]" + pLicense);
	        	System.out.println("[sShopNo]" + sShopNo);
	        	System.out.println("[pPaySeq]" + pPaySeq);
	        	System.out.println("[iDayTime]" + iDayTime);
	        	System.out.println("[iNightTime]" + iNightTime);
	        	System.out.println("[fDayMny]" + fDayMny);
	        	System.out.println("[fNightMny]" + fNightMny);
	        	System.out.println("[orderTime]" + orderTime);
	        	System.out.println("[pPayTime]" + pPayTime);
	        	//通知C++代理
	        	int rsp = parkNativeBean.payParkCarFeeJy(3, iPayWay, pLicense, sShopNo, pPaySeq, iDayTime, iNightTime, fDayMny, fNightMny, orderTime, pPayTime);
	        	if(rsp == 0) {
	        		//通知岗亭 测试先注释
	        		parkHandler.proxyJinyingPrePayNotice(ParkProxy.PROXY_JINYING_PRE_PAY_NOTICE, iPayMethod, pPayWay, pLicense, sShopNo, pPaySeq, iDayTime, iNightTime, fDayMny, fNightMny, orderTime, pPayTime);
	        	}
        		next.setNext(rsp);
        		System.out.println("得到的响应["+ rsp + "]");
	        	response(msg);
	        	break;
	        case PAY_RESULT_YL:
	        	System.out.println("28号支付命令,PAY_RESULT_YL,银联支付结果");
	        	int c = 1; 
	        	
	        	String syncId = (String) msg.getNextValue(c++);	//订单流水号，由运营商生成，考虑同一订单多次支付。例如支付完成后，半小时未出场，根据运营商规则不同，可能会使用同一订单号继续计费。用流水号唯一标识收费操作。
		    	String orderId = (String) msg.getNextValue(c++);	//订单号，由运营商生成
		    	String upOrderId = (String) msg.getNextValue(c++);	//全渠道订单号
		    	int payAmount = (int) msg.getNextValue(c++);	//订单金额，精确到分。
		    	float fpayAmount = (float)payAmount;
		    	String orderDate = (String) msg.getNextValue(c++);	//订单支付时间。UTC时间。例如：2018-03-16T16:06:05Z
		    	int payStatus = (int) msg.getNextValue(c++);	//订单状态。0: 支付成功; 1: 支付中; 2: 支付失败; 3: 退款中; 4: 退款成功; 5: 退款失败	
		    	int discountAmount = (int) msg.getNextValue(c++);	//优惠金额，精确到分，以分为单位
		    	
	        	System.out.println("[syncId]" + syncId);
	        	System.out.println("[orderId]" + orderId);
	        	System.out.println("[upOrderId]" + upOrderId);
	        	System.out.println("[payAmount]" + payAmount);
	        	System.out.println("[orderDate]" + orderDate);
	        	System.out.println("[payStatus]" + payStatus);
	        	System.out.println("[discountAmount]" + discountAmount);
	        	
	        	//业务处理
	        	if(YL.orderMap.containsKey(orderId)) {
	        		//1.等待支付中，0.线下支付，2.银联支付
	        		JSONObject orderInfo = YL.orderInfoMap.get(orderId);
	        		int status = YL.orderMap.get(orderId);
	        		System.out.println("[orderId=" + orderId + "][status=" + status + "][缓存中存在]");
	        		if(status == 1){
	        			System.out.println("[orderId=" + orderId + "][等待支付中，通知岗亭]");
	        			//通知C++
	        			parkHandler.payResultYl(YINLIAN_PAY_RESULT, orderInfo.optString("plateNumber"), ((float)payAmount) / 100, orderId, syncId, upOrderId, ((float)discountAmount) / 100, payStatus, YL.watchMap.get(orderId));
        				if(payStatus == 0) {	//支付成功，标记为2.银联支付
        					YL.orderMap.put(orderId, 2);
        				}else if(payStatus == 2) {	//支付失败，标记为0.线下支付
        					YL.orderMap.put(orderId, 0);
        				}
	        		}else if(status == 0){
	        			System.out.println("[orderId=" + orderId + "][线下已支付，调用银联退款]");
	        			//已线下支付，调用银联退款
	        			JSONObject billRsp = YL.refundBill(orderInfo.optString("plateNumber"), payAmount, String.valueOf(System.currentTimeMillis()), orderId);
	        			System.out.println("[退款结果][" + billRsp.toString() + "]");
	        			YL.orderMap.remove(orderId);
	        		}else { 
	        			System.out.println("[orderId=" + orderId + "][线上已支付，重复请求，忽略]");
	        			//重复请求，忽略
	        			YL.orderMap.remove(orderId);
	        		}
	        	}else{
	        		System.out.println("[缓存中不存在，什么都不做][orderId=" + orderId + "]");
	        	}
	        	
	        	next.setNext(0);
	        	System.out.println("代理返回给平台：[0]");
	        	response(msg);
	        	break;
	        case CHECK_IN_JY:
	        	System.out.println("29号命令,CHECK_IN_JY,金鹰酒店");
	        	int i = 1; 
	        	parkId = (String) msg.getNextValue(i++);
	        	String carNo = (String) msg.getNextValue(i++);
	        	String roomNo = (String) msg.getNextValue(i++);
	        	int discountTime = (int) msg.getNextValue(i++);
	        	String checkInTime = (String) msg.getNextValue(i++);
	        	
	        	System.out.println("[parkId]" + parkId);
	        	System.out.println("[carNo]" + carNo);
	        	System.out.println("[roomNo]" + roomNo);
	        	System.out.println("[discountTime]" + discountTime);
	        	System.out.println("[checkInTime]" + checkInTime);
	        	rsp = parkNativeBean.checkInJy(parkId, carNo, roomNo, discountTime, checkInTime);
	        	next.setNext(rsp);
	        	System.out.println("[29号]得到的响应["+ rsp + "]");
	        	response(msg);
	        	break;
	        case CHECK_OUT_JY:
	        	System.out.println("30号命令,CHECK_OUT_JY,金鹰酒店");
	        	
	        	i = 1; 
	        	parkId = (String) msg.getNextValue(i++);
	        	carNo = (String) msg.getNextValue(i++);
	        	roomNo = (String) msg.getNextValue(i++);
	        	discountTime = (int) msg.getNextValue(i++);
	        	checkInTime = (String) msg.getNextValue(i++);
	        	String checkOutTime = (String) msg.getNextValue(i++);
	        	
	        	System.out.println("[parkId]" + parkId);
	        	System.out.println("[carNo]" + carNo);
	        	System.out.println("[roomNo]" + roomNo);
	        	System.out.println("[discountTime]" + discountTime);
	        	System.out.println("[checkInTime]" + checkInTime);
	        	System.out.println("[checkOutTime]" + checkOutTime);
	        	rsp = parkNativeBean.checkOutJy(parkId, carNo, roomNo, discountTime, checkInTime, checkOutTime);
	        	next.setNext(rsp);
	        	System.out.println("[30号]得到的响应["+ rsp + "]");
	        	response(msg);
	        	break;
            case QUERY_CAR2:
                carNumber = (String) msg.getNextValue(1);
                ViewCarportRoomInfo[] carportInfos = ParkNative.getCarportInfo(carNumber, null, 0, 0);
                if (carportInfos == null || carportInfos.length == 0) {
                    logger.info("there is no carportInfo for query:{}", carNumber);
                    next.setNext(NULL_MSG);
                    response(msg);
                    return;
                }
                next = next.setNext(parkId).setNext(carportInfos.length);
                for (ViewCarportRoomInfo info : carportInfos) {
                    next = next.setNext(safe(info.sCarportNum)).setNext(safe(info.sRoomNum))
                            .setNext(safe(info.sName))
                            .setNext(safe(info.sAddress))
                            .setNext(safe(info.sPhoneNumber))
                            .setNext(safe(info.sPosition))
                            .setNext(safe(info.sStartDate))
                            .setNext(safe(info.sEndDate))
                            .setNext(info.fDeposit)
                            .setNext(info.bTemporary)
                            .setNext(safe(info.sRemark))
                            .setNext(safe(info.sRentName))
                            .setNext(info.fRentMoney)
                            .setNext(safe(info.sParkName));
                }
                logger.info("return query_car-2 : {}", msg);
                response(msg);
                break;
            case CHARGE_NOTIFY:
                String outTradeNo = (String) msg.getNextValue(1);
                carNumber = (String) msg.getNextValue(2);
                String roomNumber = (String) msg.getNextValue(3);
                String carPorts = (String) msg.getNextValue(4);
                String payTime = (String) msg.getNextValue(5);
                int months = (Byte) msg.getNextValue(6);
                totalFee = (float) msg.getNextValue(7);

                String[] carPortArray = carPorts.split(",");
                success = ParkNative.payCarportRent(carNumber, roomNumber, carPortArray[0]
                        , carPortArray, payTime, outTradeNo, months, totalFee
                        , carPortArray.length > 1, 0, 0, 0, 0, 0) == 0;

                next.setNext(success ? "OK" : "FAIL");
                response(msg);
                break;
            case NO_CARD_ENTRY:
                watchId = (String) msg.getNextValue(1);
                String userId = (String) msg.getNextValue(2);

                logger.info("no card: {}-{}", watchId, userId);
                success = parkHandler.noCardEntry(NO_CARD_ENTRY, watchId, userId);
                next.setNext(success ? "OK" : "FAIL");
                response(msg);
                break;
            case DISPATCH_COUPON:
                carNumber = (String) msg.getNextValue(1);
                int couponId = (int) msg.getNextValue(2);
                String couponName = (String) msg.getNextValue(3);
                int couponType = (int) msg.getNextValue(4);
                int duration = (int) msg.getNextValue(5);
                int amount = (int) msg.getNextValue(6);
                String startTime = (String) msg.getNextValue(7);
                String endTime = (String) msg.getNextValue(8);
                String parkId = (String) msg.getNextValue(9);

                /*if (parkId != null) {
                    next.setNext(0).setNext(1.2f).setNext(10);
                    response(msg);
                    return;
                }*/

                ParkedCarInfo carInPark = ParkNative.getCarInPark("", carNumber, 3, 1, "");
                next.setNext(carInPark.iReturn);
                if (carInPark.iReturn == 0) {
                    ParkedCarInfo ret = ParkNative.dispatchCoupon("", carNumber, String.valueOf(couponId)
                            , couponName, couponType, duration, amount / 100f, startTime, endTime, parkId, carInPark.sID);
                    next.setNext(ret.iReturn).setNext(ret.fMoney).setNext(ret.iParkedTime);
                }
                response(msg);
                break;
            case SIMILAR_CAR_NUMBER:
                //String[] cars = new String[]{"苏A12345", "苏A12346", "苏A12347"};
                carNumber = (String) msg.getNextValue(1);
                next.setNext(ParkNative.getCarInParkByName(carNumber));
                //next.setNext(Arrays.stream(cars).filter(car -> car.contains(carNumber)).reduce("", (c1, c2) -> c1 + "," + c2));
                response(msg);
                break;
            default:
                throw new Exception("can not recognize the code:" + msg.getValue());
        }
    }

    String safe(String str) {
        return str == null ? "" : str;
    }
}

