package xd.fw.job;

import com.sun.org.glassfish.gmbal.ManagedObject;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import xd.fw.mina.tlv.TLVCodecFactory;
import xd.fw.mina.tlv.TLVMessage;

import java.net.InetSocketAddress;

public class ParkTest extends IoHandlerAdapter {

    Object ret;
    public String testPark(String params) throws Exception{
        String[] ps = params.split(",");
        String host = ps[0], port = ps[1], action=ps[2];

        SocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TLVCodecFactory("UTF-8")));
        connector.setHandler(this);

        ConnectFuture future = connector.connect(new InetSocketAddress(host, Integer.parseInt(port)));
        future.awaitUninterruptibly();
        IoSession session = future.getSession();

        if ("1".equals(action)){
            TLVMessage enter = new TLVMessage(1);
            enter.setNext("?A12345").setNext("20160530163322");
            session.write(enter);
        } else {
            TLVMessage out = new TLVMessage(2);
            out.setNext("?A12345").setNext(1.0f).setNext("20160530163322").setNext("20160530163323");
            session.write(out);
        }
        synchronized (this){
            wait(10000);
        }
        return ret + "";

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        session.closeNow();
        ret = message;
        synchronized (this){
            notifyAll();
        }
    }
}
