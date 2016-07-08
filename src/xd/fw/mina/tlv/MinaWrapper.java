package xd.fw.mina.tlv;


import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;


public class MinaWrapper {
    InetSocketAddress defaultLocalAddress;
    boolean reuseAddress = true;
    IoHandler handler;
    IoFilterChainBuilder filterChainBuilder;
    NioSocketAcceptor acceptor;

    public void init() throws IOException {
        acceptor = new NioSocketAcceptor();
        acceptor.setDefaultLocalAddress(defaultLocalAddress);
        acceptor.setReuseAddress(reuseAddress);
        acceptor.setFilterChainBuilder(filterChainBuilder);
        acceptor.setHandler(handler);
        acceptor.bind();
    }

    public void destroy() throws Exception{
        acceptor.unbind();

        Method method = acceptor.getClass().getDeclaredMethod("destroy");
        Logger logger = Logger.getLogger("Mina");
        logger.info(method);
        method.setAccessible(true);
        method.invoke(acceptor);

        DefaultIoFilterChainBuilder dBuilder = (DefaultIoFilterChainBuilder)filterChainBuilder;
        for (IoFilterChain.Entry entry : dBuilder.getAll()){
            if (entry.getFilter() instanceof ExecutorFilter){
                ((ExecutorService)((ExecutorFilter)entry.getFilter()).getExecutor()).shutdownNow();
            }
        }
    }

    public SocketSessionConfig getSessionConfig(){
        return acceptor.getSessionConfig();
    }

    public void setDefaultLocalAddress(InetSocketAddress defaultLocalAddress) {
        this.defaultLocalAddress = defaultLocalAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public void setHandler(IoHandler handler) {
        this.handler = handler;
    }

    public void setFilterChainBuilder(IoFilterChainBuilder filterChainBuilder) {
        this.filterChainBuilder = filterChainBuilder;
    }
}
