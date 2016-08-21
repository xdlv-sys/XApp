package xd.fw.mina;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.job.ParkNative;
import xd.fw.mina.tlv.*;
import xd.fw.service.ParkService;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Service
public class ParkProxy extends ReversedProxy {

    static final byte QUERY_CAR = 2, PAY_FEE = 3;

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

    @PreDestroy
    public void destroy(){
        super.destroy();
    }

    @Override
    protected void constructRegistryMessage(TLVMessage registryMessage) {
        registryMessage.setNext(parkId).setNext(parkName).setNext(100);
    }

    @Override
    protected InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    @Override
    protected void handlerQuery(TLVMessage msg) throws Exception {
        //2->1471225285910->苏A12345->001(parkId)->0(carType)
        //reuse msg
        byte code = (byte) msg.getValue();
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
                }
                next.setNext(parkedInfo.fMoney
                ).setNext(parkedInfo.sInTime).setNext(parkedInfo.iParkedTime);
                response(msg);
                break;
            case PAY_FEE:
                next.setNext("OK");
                response(msg);
                break;
            default:
                throw new Exception("can not recognize the code:" + msg.getValue());
        }
    }
}
