package xd.fw.mina;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.job.ParkNative;
import xd.fw.mina.tlv.ReversedProxy;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Service
public class ParkProxy extends ReversedProxy {

    static final int QUERY_CAR = 3, PAY_FEE = 4, PAY_FEE_NOTIFY = 9;

    @Autowired
    ParkNative parkNative;
    @Autowired
    ParkHandler parkHandler;

    @Value("${center_ip}")
    String host;
    @Value("${center_port}")
    int port;

    @Value("${park_id}")
    String parkId;

    @Value("${park_name}")
    String parkName;

    @Value("${version}")
    int version;

    @PreDestroy
    public void destroy(){
        super.destroy();
    }

    @Override
    protected void constructRegistryMessage(TLVMessage registryMessage) {
        registryMessage.setNext(parkId).setNext(parkName).setNext(100).setNext(version);
    }

    @Override
    protected InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    @Override
    protected void handlerQuery(TLVMessage msg) throws Exception {
        logger.info("receive:" + msg);
        //2->1471225285910->苏A12345->001(parkId)->0(carType)
        //reuse msg
        int code = (int) msg.getValue();
        TLVMessage next = msg.getNext(0);
        switch (code){
            case QUERY_CAR:
                String carNumber = (String)msg.getNextValue(1);
                String watchId = (String)msg.getNextValue(2);
                ParkNative.ParkedInfo parkedInfo;
                if (StringUtils.isNotBlank(watchId)){
                    parkedInfo = parkHandler.queryCarInfo(QUERY_CAR,watchId,carNumber);
                } else {
                    byte carType = (byte)msg.getNextValue(3);
                    parkedInfo = parkNative.getParkedInfo(carType, carNumber);
                    parkedInfo.carNumber = carNumber;
                }
                if (parkedInfo != null && parkedInfo.iReturn != 6){
                    next.setNext(parkedInfo.carNumber).setNext(parkedInfo.fMoney
                    ).setNext(parkedInfo.sInTime).setNext(parkedInfo.iParkedTime);
                } else {
                    //just return null
                    next.setNext(NULL_MSG);
                }

                response(msg);
                break;
            case PAY_FEE:
                carNumber = (String)msg.getNextValue(1);
                float totalFee = (float)msg.getNextValue(2);
                String timeStamp = (String)msg.getNextValue(3);
                watchId = (String)msg.getNextValue(4);
                boolean success;
                if (StringUtils.isNotBlank(watchId)){
                    success = parkHandler.payFee(PAY_FEE,watchId,carNumber,totalFee);
                } else {
                    //byte carType = (byte)msg.getNextValue(4);
                    success = parkNative.payFee(carNumber, timeStamp, totalFee);
                }
                next.setNext(success ? "OK" : "FAIL");
                response(msg);
                break;
            default:
                throw new Exception("can not recognize the code:" + msg.getValue());
        }
    }
}
