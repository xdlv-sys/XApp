package xd.fw.test;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.testng.annotations.Test;
import xd.fw.FwUtil;
import xd.fw.mina.tlv.MinaWrapper;
import xd.fw.mina.tlv.TLVCodecFactory;
import xd.fw.mina.tlv.TLVHandler;
import xd.fw.mina.tlv.TLVMessage;

import java.net.InetSocketAddress;

/**
 * Created by xd on 2/16/2017.
 */
public class MinaClient {
    @Test
    public void testWhite() throws Exception{
        NioSocketConnector connector = new NioSocketConnector(MinaWrapper.getPool());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TLVCodecFactory("gb2312")));
        connector.setHandler(new TLVHandler(){
            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                System.out.println(message);
                synchronized (MinaClient.this){
                    MinaClient.this.notifyAll();
                }
            }
        });

        //ConnectFuture future = connector.connect(new InetSocketAddress("hjcpay.com",48012));
        ConnectFuture future = connector.connect(new InetSocketAddress("localhost",48012));
        future.awaitUninterruptibly();

        TLVMessage message = new TLVMessage(12);
        //message.setNext("111").setNext("193.168.1.110").setNext(3);
        message.setNext("001").setNext("1").setNext(1);
        future.getSession().write(message);
        synchronized (MinaClient.this){
            wait();
        }
        connector.dispose();
    }
}
