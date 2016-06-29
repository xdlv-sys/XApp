package xd.fw.job;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVCodecFactory;
import xd.fw.mina.tlv.TLVHandler;
import xd.fw.mina.tlv.TLVMessage;
import xd.fw.service.ParkService;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Service
public class ProxyJob extends BaseJob {

    final byte REGISTRY = 1, QUERY_CAR = 2, QUERY_CAR_RESP = 3,FREE = 4, FREE_RESP = 5;

    @Autowired
    ParkService parkService;

    @Value("${center_ip}")
    String host;
    @Value("${center_port}")
    int port;

    @Value("${park_id}")
    String parkId;


    private SocketConnector connector;
    private IoSession session;

    @PostConstruct
    public void init(){
        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TLVCodecFactory("UTF-8")));
        connector.setHandler(new TLVHandler(){
            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                TLVMessage msg = (TLVMessage)message;
                TLVMessage ret;
                switch ((byte)msg.getValue()){
                    case QUERY_CAR:
                        String carNumber = (String)msg.getNext().getValue();
                        logger.debug("query for " + carNumber);

                        ret = new TLVMessage(QUERY_CAR_RESP);
                        ret.setNext("2016-1-1 18:30:20").setNext("3小时20分钟").setNext(0.1f).setNext(carNumber);
                        session.write(ret);
                        break;
                    case FREE:
                        ret = new TLVMessage(FREE_RESP);
                        ret.setNext(parkService.getFreeParkStation());
                        session.write(ret);
                        break;
                    default:
                        throw new Exception("can not recognize the code:" + msg.getValue());
                }
            }
        });
    }

    @Override
    public void doExecute() throws Exception {
        logger.info("start to send heart beat message");
        checkSession();
        TLVMessage registryMessage = new TLVMessage(REGISTRY);
        registryMessage.setNext(parkId);
        session.write(registryMessage);
    }

    private synchronized void checkSession()throws Exception{
        int count = 0;
        boolean reconnect = false;
        while (session == null || !session.isConnected()){
            reconnect = true;
            ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
            future.awaitUninterruptibly();
            try {
                session = future.getSession();
            }catch (Exception e){
                logger.warn("can not connect center, try again later:" + e);
            }
            if (count++ > 0){
                Thread.sleep(count * 1000);
                if (count > 10){
                    count = 10;
                }
            }
        }
        if (reconnect && session != null){
            logger.info("connected center...");
        }
    }

}
