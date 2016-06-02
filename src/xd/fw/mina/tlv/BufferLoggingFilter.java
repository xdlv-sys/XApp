package xd.fw.mina.tlv;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.filter.logging.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xd on 2016/6/2.
 */
public class BufferLoggingFilter extends LoggingFilter{

    Logger logger;

    public BufferLoggingFilter(){
        super(BufferLoggingFilter.class);
        logger = LoggerFactory.getLogger(getName());
    }
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (logger.isTraceEnabled()){
            logger.trace("RECEIVED: {}", ((IoBuffer)message).getHexDump());
        }
        nextFilter.messageReceived(session, message);
    }

    @Override
    public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (logger.isTraceEnabled()){
            logger.trace("SEND: {}",((IoBuffer)writeRequest.getMessage()).getHexDump());
        }
        super.filterWrite(nextFilter, session, writeRequest);
    }
}
