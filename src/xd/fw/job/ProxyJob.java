package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.*;
import xd.fw.service.ParkService;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Service
public class ProxyJob extends ReversedProxy {

    static final byte NULL_MSG = 0, REGISTRY = 1, QUERY_CAR = 2, QUERY_CAR_RESP = 3,FREE = 4, FREE_RESP = 5, PAY_FEE = 6, PAY_FEE_RESP = 7;

    @Autowired
    ParkService parkService;

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
        registryMessage.setNext(REGISTRY).setNext(parkId).setNext(parkName).setNext(100);
    }

    @Override
    protected InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    @Override
    protected void handlerQuery(TLVMessage msg) throws Exception {
        //messageId->code->Id
        //reuse msg
        byte code = (byte) msg.getNext().getValue();
        switch (code){
            case QUERY_CAR:
                String carNumber = (String)msg.getNext(1).getValue();
                logger.debug("query for " + carNumber);

                msg.setNext(QUERY_CAR_RESP).setNext("2016-1-1 18:30:20"
                ).setNext("3小时20分钟").setNext(0.01f).setNext(carNumber);
                response(msg);
                break;
            case FREE:
                msg.setNext(FREE_RESP).setNext(parkService.getFreeParkStation());
                response(msg);
                break;
            case PAY_FEE:
                msg.setNext(PAY_FEE_RESP).setNext("OK");
                response(msg);
                break;
            default:
                throw new Exception("can not recognize the code:" + msg.getValue());
        }
    }
}
