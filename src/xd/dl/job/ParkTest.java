package xd.dl.job;

import org.apache.mina.core.buffer.IoBuffer;
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
            TLVMessage enter = new TLVMessage((byte)1);
            enter.setNext("苏A12345").setNext("20160530163322");
            session.write(enter);
        } else if ("2".equals(action)) {
            TLVMessage out = new TLVMessage((byte)2);
            out.setNext("苏A12345").setNext(1.0f).setNext("20160530163322").setNext("20160530163323");
            session.write(out);
        } else if ("20".equals(action)) {
            // 20->苏A12388->20180911085135->20180911091302->0001->N1502000000101180911091300897253->TEST1->1->->20180911085135->20180911091302->1->0.1->0.1->0.0->0.0
            TLVMessage out = new TLVMessage(20);
            out.setNext("苏A12388").setNext("20180911085135").setNext("20180911091302").setNext("0001")
                    .setNext("N1502000000101180911091300897253").setNext("a")
                    .setNext(1).setNext("").setNext("20180911085135").setNext("20180911091302")
                    .setNext(1).setNext(0.1f).setNext(0.1f).setNext(0f).setNext(0f);
            session.write(out);
        }
        else if ("21".equals(action)) {
            // 21->orderId -> carNumber -> price
            TLVMessage out = new TLVMessage(21);
            out.setNext("N1502000000101180911091300897253").setNext("苏A12388").setNext(0.1f);
            session.write(out);
        }
        synchronized (this){
            wait(10000);
        }
        return ret + "";
    }

    public void sendDirectBuffer(){
        byte[] bytes = new byte[]{0x05,(byte)0x10,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x29,(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xBD,(byte)0xF2,(byte)0x41,(byte)0x48,(byte)0x55,(byte)0x4B,(byte)0x4A,(byte)0x50,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,(byte)0x32,(byte)0x30,(byte)0x31,(byte)0x36,(byte)0x30,(byte)0x36,(byte)0x30,(byte)0x34,(byte)0x32,(byte)0x30,(byte)0x35,(byte)0x32,(byte)0x30,(byte)0x35};

        SocketConnector connector = new NioSocketConnector();
        /*connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TLVCodecFactory("gb2312")));*/
        connector.setHandler(this);

        ConnectFuture future = connector.connect(new InetSocketAddress("localhost", 48011));
        future.awaitUninterruptibly();
        IoSession session = future.getSession();
        session.write(IoBuffer.wrap(bytes));
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        session.closeNow();
        ret = message;
        synchronized (this){
            notifyAll();
        }
    }

    public static void main(String[] args) throws Exception {
        //new ParkTest().sendDirectBuffer();
        //new ParkTest().testPark("localhost,48011,1");

        //new ParkTest().testPark("localhost,48011,2");

        new ParkTest().testPark("localhost,48011,21");
    }
}
