package xd.fw.mina;


import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.HttpClientTpl;
import xd.fw.job.IDongHui;
import xd.fw.mina.tlv.ReversedHandler;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class ParkHandler extends ReversedHandler {
    @Autowired
    OutProcess outProcess;

    @Autowired
    EnterProcess enterProcess;

    @Override
    protected void handlerRegistry(TLVMessage msg, IoSession session) {
        //岗亭注册，直接返回
        session.write(msg);
    }

    @Override
    protected boolean handlerMessage(TLVMessage msg, IoSession session) {
        byte code = (byte)msg.getNextValue(0);
        if (code != 1 && code != 2){
            return false;
        }
        SendRequest sendRequest = code == 1 ? enterProcess : outProcess;

        String[][] params = new String[0][];
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
        ret.setNext(Integer.valueOf(jsonObject.getString("msg"))).setNext(msg.getNextValue(0));
        session.write(ret);

        return true;
    }
}
