package xd.fw.mina.tlv;


import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import xd.fw.FwUtil;
import xd.fw.job.BaseJob;

import java.net.InetSocketAddress;

public abstract class ReversedProxy extends BaseJob implements IMinaConst{

    private SocketConnector connector;
    IoProcessor<NioSession> pool;
    private IoSession session;

    public ReversedProxy() {
        pool = new SimpleIoProcessorPool<NioSession>(NioProcessor.class);
        connector = new NioSocketConnector(pool);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TLVCodecFactory(FwUtil.UTF8)));
        connector.setHandler(new TLVHandler() {
            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                TLVMessage msg = (TLVMessage) message;
                logger.debug("message:" + msg);
                handlerQuery(msg);
            }
        });
    }

    public void destroy() {
        super.destroy();
        connector.dispose();
        pool.dispose();
    }

    @Override
    public void doExecute() throws Exception {
        logger.info("start to send heart beat message");
        checkSession();

        TLVMessage registryMessage = new TLVMessage(REGISTRY);
        constructRegistryMessage(registryMessage);

        session.write(registryMessage);
    }

    protected abstract void constructRegistryMessage(TLVMessage registryMessage);

    protected abstract InetSocketAddress inetSocketAddress();

    protected abstract void handlerQuery(TLVMessage msg) throws Exception;

    protected void response(TLVMessage response){
        session.write(response);
    }

    private synchronized void checkSession() throws Exception {
        int count = 0;
        boolean reconnect = false;
        while (session == null || !session.isConnected()) {
            reconnect = true;
            ConnectFuture future = connector.connect(inetSocketAddress());
            future.awaitUninterruptibly();
            try {
                session = future.getSession();
            } catch (Exception e) {
                logger.warn("can not connect center, try again later:" + e);
            }
            if (count++ > 0) {
                Thread.sleep(count * 1000);
                if (count > 10) {
                    count = 10;
                }
            }
        }
        if (reconnect && session != null) {
            logger.info("connected center...");
        }
    }
}