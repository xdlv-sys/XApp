package xd.fw.mina;


import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.HttpClientTpl;
import xd.fw.job.IDongHui;
import xd.fw.job.ParkNative;
import xd.fw.mina.tlv.ReversedHandler;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class ParkHandler extends ReversedHandler {
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


    @Override
    protected void handlerRegistry(TLVMessage msg, IoSession session) {
        //岗亭注册，直接返回 0->200->watchId
        TLVMessage ret = new TLVMessage(msg.getValue());
        ret.setNext(200).setNext(msg.getNextValue(0));
        session.write(ret);
    }

    @Override
    protected boolean handlerMessage(TLVMessage msg, IoSession session) {
        int code = (int)msg.getValue();
        SendRequest sendRequest;
        switch (code){
            case 1:
                sendRequest = enterProcess;
                break;
            case 2 :
                sendRequest = outProcess;
                break;
            case 5:
                sendRequest = sigIn;
                break;
            case 6:
                sendRequest = sigOut;
                break;
            case 7 :
                sendRequest = thumb;
                break;
            default:
                return false;
        }

        String[][] params;
        try {
            params = sendRequest.constructParams(msg.getNext());
        } catch (Exception e) {
            logger.error("",e);
            return true;
        }

        logger.info(ArrayUtils.toString(params));

        JSONObject jsonObject;
        String json = null;
        try {
            json = HttpClientTpl.post(sendRequest.svrAddress(), params);
            jsonObject = JSONObject.fromObject(json);
        } catch (Exception e) {
            logger.error("error json:" + json ,e);
            jsonObject = new JSONObject();
            jsonObject.put("state",false);
            jsonObject.put("msg","-1");
        }

        TLVMessage ret = new TLVMessage(code);
        ret.setNext(Integer.valueOf(jsonObject.getString("code"))).setNext(msg.getNextValue(0));
        session.write(ret);

        return true;
    }

    public ParkNative.ParkedInfo queryCarInfo(int code,String watchId, String carNumber){
        TLVMessage message = createRequest(code, carNumber);
        TLVMessage ret = request(watchId, message);
        ParkNative.ParkedInfo parkedInfo = new ParkNative.ParkedInfo();
        if (ret != null && 200 == (int)ret.getValue() ){
            parkedInfo.fMoney = (float)ret.getNextValue(0);
            parkedInfo.sInTime = (String)ret.getNextValue(1);
            parkedInfo.iParkedTime = (int)ret.getNextValue(2);
            return parkedInfo;
        }
        return null;
    }

    public boolean payFee(int code, String watchId, String carNumber, float money){
        TLVMessage message = createRequest(code, carNumber, money);
        TLVMessage ret = request(watchId, message);
        return ret != null && 200 == (int)ret.getValue();
    }
}
