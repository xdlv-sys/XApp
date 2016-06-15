package xd.fw.mina.tlv;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created by xd on 2016/5/14.
 */
public class Test extends IoHandlerAdapter {
    public static final int CONNECT_TIMEOUT = 3000;

    private String host;
    private int port;
    private SocketConnector connector;
    private IoSession session;

    public Test(String host, int port) {
        this.host = host;
        this.port = port;
        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TLVCodecFactory("UTF-8")));
        connector.setHandler(this);

        ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
        future.awaitUninterruptibly();
        session = future.getSession();

        TLVMessage enter = new TLVMessage(1);
        enter.setNext("苏A12345").setNext("20160530163322");
        session.write(enter);

        TLVMessage out = new TLVMessage(2);
        out.setNext("苏A12345").setNext(1.0f).setNext("20160530163322").setNext("20160530163323");
        session.write(out);

    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("receive:" + message);
    }

    public static void main(String[] args)throws Exception{
        Test t = new Test("127.0.0.1",48011);
        System.in.read();
        t.session.closeNow();
        System.out.println("close");
    }
}
