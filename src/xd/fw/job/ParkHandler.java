package xd.fw.job;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.bean.EnterOrOutRecord;
import xd.fw.mina.tlv.TLVHandler;
import xd.fw.mina.tlv.TLVMessage;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

@Service
public class ParkHandler extends TLVHandler{

    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    final String CODE = "fsAsf2015";

    static Logger logger = Logger.getLogger(ParkHandler.class);

    final SendRequest enter = new SendRequest() {
        @Override
        public String[][] constructParams(String timeStamp, TLVMessage request) throws Exception {
            String[] parkingNoAndHostName = getParkingNoAndHostName();
            String carNumber = (String)request.getValue();
            String time1 = (String)request.getNext().getValue();
            return new String[][]{
                    {"Parkingno", parkingNoAndHostName[0]},
                    {"Carnumber", carNumber},
                    {"Time1", time1},
                    {"Timestamp", timeStamp},
                    {"Token", md5(carNumber, timeStamp, parkingNoAndHostName[0], CODE)}
            };
        }

        @Override
        public String svrAddress() {
            return "http://" + getParkingNoAndHostName()[1] + "/mobile/index.php?act=user&op=approach";
        }
    };

    final SendRequest out = new OutJob();

    public void save(){
        out.save();
    }

    String getTimeStamp(){
        return sdf.format(new Date());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        logger.info("receive:" + message);
        TLVMessage request = (TLVMessage)message;
        int action = (int)request.getValue();
        if (action != 1 && action != 2){
            throw new Exception("Illegal action:" + action);
        }
        SendRequest sendRequest = action == 1 ? enter : out;

        String timeStamp = getTimeStamp();
        String[][] params = sendRequest.constructParams(timeStamp,request.getNext());

        StringBuffer buffer = new StringBuffer();
        for (String[] strs : params){
            buffer.append(strs[0]).append("=").append(strs[1]).append("\n");
        }
        logger.info(buffer.toString());

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

        TLVMessage ret = new TLVMessage(action);
        ret.setNext(Integer.valueOf(jsonObject.getString("msg"))).setNext(request.getNext().getValue());
        session.write(ret);
    }
}
