package xd.fw.mina.tlv;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.*;

/**
 * Created by xd on 2016/5/13.
 */
public class TLVCodecFactory implements ProtocolCodecFactory {

    static Logger logger = Logger.getLogger(TLVCodecFactory.class);
    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;
    private String charset;

    public TLVCodecFactory(String charset) {
        encoder = new TLVEncoder();
        decoder = new TLVDecoder();
        this.charset = charset;
    }

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }

    static byte[] magic = new byte[]{5, 16};// my daughter's birthday
    static int HEAD_LENGTH = magic.length + Integer.SIZE / 8;

    class TLVEncoder extends ProtocolEncoderAdapter {
        @Override
        public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
            TLVMessage msg = (TLVMessage) message;
            IoBuffer buffer = IoBuffer.allocate(1024, false);
            buffer.setAutoExpand(true);
            buffer.put(magic);
            buffer.putInt(0); // placeHolder for message length
            TLVMessage tmp = msg;
            do {
                tmp.fill(buffer, charset);
                tmp = tmp.getNext();
            } while (tmp != null);
            buffer.flip();

            buffer.position(magic.length);
            buffer.putInt(buffer.limit() - HEAD_LENGTH);
            buffer.position(0);
            out.write(buffer);
        }
    }

    class TLVDecoder extends CumulativeProtocolDecoder {
        @Override
        protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
            if (in.remaining() >= HEAD_LENGTH) {
                if (in.get() == magic[0]
                        && in.get() == magic[1]) {
                    logger.debug("magic is right.");
                } else {
                    throw new Exception("wrong magic");
                }
                int length = in.getInt();
                if (in.remaining() >= length) {
                    TLVMessage message = null, tmp,currentMsg = null;
                    int position = in.position();
                    while (in.position() - position < length) {
                        tmp = TLVMessage.parse(in, charset);
                        if (message == null) {
                            currentMsg = message = tmp;
                        } else {
                            currentMsg.setNext(tmp);
                            currentMsg = tmp;
                        }
                    }
                    out.write(message);
                    return true;
                }
            }
            return false;
        }
    }
}
