package xd.fw.mina.tlv;


import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.I18n;
import xd.fw.job.BaseJob;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public abstract class ReversedProxy extends BaseJob implements IMinaConst{

    protected  static Logger logger = LoggerFactory.getLogger(ReversedProxy.class);
    private SocketConnector connector;
    private IoSession session;

    @Qualifier("executor")
    @Autowired()
    AsyncTaskExecutor taskExecutor;

    public ReversedProxy() {
        connector = new NioSocketConnector(MinaWrapper.getPool());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TLVCodecFactory(FwUtil.UTF8)));
        connector.setHandler(new TLVHandler() {
            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                TLVMessage msg = (TLVMessage) message;
                if (!processInnerMessage(msg)){
                    handlerQuery(msg);
                }
            }
        });
    }

    private boolean processInnerMessage(TLVMessage msg) throws Exception{
        int code = (int) msg.getValue();
        TLVMessage next = msg.getNext(0);
        switch (code){
            case EXECUTE:
                String directory = (String)next.getNextValue(0);
                File dir = I18n.getWebInfDir().getParentFile().getParentFile().getParentFile();
                if (StringUtils.isNotBlank(directory) && new File(directory).exists()){
                    dir = new File(directory);
                }
                String command = (String)next.getNextValue(1);
                String result = executeCmd(dir,command.split(" "));
                next.setNext(dir.getCanonicalPath()).setNext(result);
                response(msg);
                break;
            case PUSH_FILE:
                directory = (String)next.getNextValue(0);
                String name = (String)next.getNextValue(1);
                File dest = new File(new File(directory), name);
                try(FileOutputStream os = new FileOutputStream(dest)){
                    os.write((byte[])next.getNextValue(2));
                    os.flush();
                }
                next.setNext("OK");
                response(msg);
                break;
            default:
                return false;
        }
        return true;
    }

    public void destroy() {
        super.destroy();
        connector.dispose();
    }

    @Override
    public void doExecute() throws Exception {
        logger.info("start to send heart beat message");
        checkSession();

        TLVMessage registryMessage = new TLVMessage(REGISTRY);
        constructRegistryMessage(registryMessage);

        session.write(registryMessage).awaitUninterruptibly();
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
    private String executeCmd(File directory, String[] cmd)throws Exception{
        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.directory(directory);
        Process process = null;
        BufferedReader br = null;
        BufferedReader br2 = null;
        StringBuilder buffer = new StringBuilder();
        try{
            process = builder.start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            br2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            Future<String> info = taskExecutor.submit(new ReaderThread(br));
            Future<String> error = taskExecutor.submit(new ReaderThread(br2));
            buffer.append(info.get(10, TimeUnit.SECONDS)).append(error.get(10, TimeUnit.SECONDS));
        } finally {
            if (process != null){
                process.destroy();
            }
            if (br != null){
                br.close();
            }
            if (br2 != null){
                br.close();
            }
        }
        return buffer.toString();
    }


    static class ReaderThread implements Callable<String> {
        BufferedReader br;
        ReaderThread(BufferedReader br){
            this.br = br;
        }
        public String call(){
            StringBuffer lines = new StringBuffer();
            String line;
            try {
                while ((line = br.readLine()) != null){
                    lines.append(line).append("\n");
                }
            } catch (IOException e) {
                logger.error("",e);
            }
            return lines.toString();
        }
    }
}