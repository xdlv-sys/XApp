package xd.fw.mina.tlv;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.filter.logging.LoggingFilter;


public class BufferLoggingFilter extends LoggingFilter{

    Logger logger = Logger.getLogger(BufferLoggingFilter.class);

    public BufferLoggingFilter(){
        super(BufferLoggingFilter.class);
    }
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (logger.isTraceEnabled()){
            logger.trace(String.format("SEND: {%s}",((IoBuffer)message).getHexDump()));
        }
        nextFilter.messageReceived(session, message);
    }

    @Override
    public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (logger.isTraceEnabled()){
            logger.trace(String.format("SEND: {%s}",((IoBuffer)writeRequest.getMessage()).getHexDump()));
        }
        super.filterWrite(nextFilter, session, writeRequest);
    }
}
