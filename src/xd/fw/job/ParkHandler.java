package xd.fw.job;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVHandler;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class ParkHandler extends TLVHandler{
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        logger.info("receive:" + message);
        TLVMessage tlvMessage = new TLVMessage(0);
        session.write(tlvMessage);
    }
}
