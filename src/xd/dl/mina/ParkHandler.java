package xd.dl.mina;


import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xd.dl.job.ParkedCarInfo;
import xd.fw.FwUtil;
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
            String requestJson = sendRequest.json();
            if (requestJson != null) {
                logger.info("before http {} json:{}", id, requestJson);
                json = FwUtil.reTry(() -> {
                    String ret = HttpClientTpl.postJson(sendRequest.svrAddress(), requestJson
                            , new String[][]{
                                    {"Token", sendRequest.token()}
                            });
                    if (JSONObject.fromObject(ret).getInt("code") != 200) {
                        logger.error("failed:{}, try again", ret);
                        enterProcess2.loginTo();
                        throw new Exception("");
                    }
                    return ret;
                }, 1);
                json = HttpClientTpl.postJson(sendRequest.svrAddress(), requestJson
                        , new String[][]{
                                {"Token", sendRequest.token()}
                        });
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
        sendRequest.constructMessage(ret.setNext(generateId()), msg.getNext(), jsonObject);
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

    public boolean payFee(int code, String watchId, String carNumber, float money) {
        TLVMessage message = createRequest(code, carNumber, money);
        TLVMessage ret = request(watchId, message);
        return ret != null && 200 == (int) ret.getValue();
    }

    @Async
    public void notifyWatchIdPayFee(int code, String carNumber, float parkingPrice, String orderNo, String memberCode, int leavel) {
        TLVMessage message = createRequest(code, 200, "OK", carNumber, parkingPrice, orderNo, memberCode, leavel);
        List<TLVMessage> messages = notifyAllId(message);
        for (TLVMessage m : messages) {
            if (m != null && 200 == (int) m.getValue()) {
                logger.info("notify wh successfully:", m);
            } else {
                logger.warn("fail to notify wh:", m);
            }
        }
    }
}
