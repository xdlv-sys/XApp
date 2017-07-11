package xd.fw.mina.tlv;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xd.fw.FwUtil;

import java.io.UnsupportedEncodingException;

public class TLVMessage {
    static Logger logger = LoggerFactory.getLogger(TLVMessage.class);

    public static final byte BYTE_TYPE = 1, INT_TYPE = 2, LONG_TYPE = 3, FLOAT_TYPE = 4, DOUBLE_TYPE = 5
            , STRING_TYPE = 6, BOOLEAN_TYPE = 7, IMG_TYPE = 8;
    public int timeout = 0;
    //byte type;
    Object value;
    TLVMessage next;

    public TLVMessage(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public TLVMessage getNext() {
        return getNext(0);
    }
    public TLVMessage getNext(int index){
        TLVMessage tmp = this;
        while (index -- > -1){
            tmp = tmp.next;
            if (tmp == null){
                return null;
            }
        }
        return tmp;
    }

    public Object getNextValue(int index, Object v){
        Object ret = getNextValue(index);
        return ret == null ? v : ret;
    }

    public Object getNextValue(int index){
        TLVMessage message = getNext(index);
        return message == null ? null : message.getValue();
    }

    public TLVMessage setNext(TLVMessage next) {
        this.next = next;
        return this.next;
    }
    public TLVMessage setNext(Object next){
        if (next instanceof TLVMessage){
            this.next = (TLVMessage)next;
        } else {
            this.next = new TLVMessage(next);
        }
        return this.next;
    }

    public static TLVMessage parse(IoBuffer buffer){
        return parse(buffer, FwUtil.UTF8);
    }
    public static TLVMessage parse(IoBuffer buffer, String charsetName) {
        byte type = buffer.get();
        switch (type) {
            case BYTE_TYPE:
                buffer.getInt();
                return new TLVMessage(buffer.get());
            case INT_TYPE:
                buffer.getInt();
                return new TLVMessage(buffer.getInt());
            case LONG_TYPE:
                buffer.getInt();
                return new TLVMessage(buffer.getLong());
            case FLOAT_TYPE:
                buffer.getInt();
                return new TLVMessage(buffer.getFloat());
            case DOUBLE_TYPE:
                buffer.getInt();
                return new TLVMessage(buffer.getDouble());
            case STRING_TYPE:
                int length = buffer.getInt();
                byte[] bytes = new byte[length];
                buffer.get(bytes);
                try {
                    return new TLVMessage(new String(bytes, charsetName));
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalArgumentException(e);
                }
            case BOOLEAN_TYPE:
                bytes = new byte[buffer.getInt()];
                buffer.get(bytes);
                return new TLVMessage(Boolean.parseBoolean(new String(bytes)));
            case IMG_TYPE:
                length = buffer.getInt();
                bytes = new byte[length];
                buffer.get(bytes);
                return new TLVMessage(bytes);

        }
        throw new IllegalArgumentException("invalidate buffer:" + type);
    }

    public void fill(IoBuffer buffer, String charsetName) {
        if (value == null) {
            return;
        }
        if (value instanceof Byte) {
            buffer.put(BYTE_TYPE);
            buffer.putInt(Byte.SIZE / 8);
            buffer.put((Byte) value);
            return;
        }
        if (value instanceof Integer) {
            buffer.put(INT_TYPE);
            buffer.putInt(Integer.SIZE / 8);
            buffer.putInt((Integer) value);
            return;
        }
        if (value instanceof Long) {
            buffer.put(LONG_TYPE);
            buffer.putInt(Long.SIZE / 8);
            buffer.putLong((Long) value);
            return;
        }
        if (value instanceof Float) {
            buffer.put(FLOAT_TYPE);
            buffer.putInt(Float.SIZE / 8);
            buffer.putFloat((Float) value);
            return;
        }
        if (value instanceof Double) {
            buffer.put(DOUBLE_TYPE);
            buffer.putInt(Double.SIZE / 8);
            buffer.putDouble((Double) value);
            return;
        }
        if (value instanceof String) {
            try {
                byte[] bytes = ((String) value).getBytes(charsetName);
                buffer.put(STRING_TYPE);
                buffer.putInt(bytes.length);
                buffer.put(bytes);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException(e);
            }
            return;
        }
        if (value instanceof Boolean) {
            byte[] bytes = String.valueOf(value).getBytes();
            buffer.put(BOOLEAN_TYPE);
            buffer.putInt(bytes.length);
            buffer.put(bytes);
            return;
        }
        if (value instanceof byte[]){
            byte[] bytes = (byte[])value;
            buffer.put(IMG_TYPE);
            buffer.putInt(bytes.length);
            buffer.put(bytes);
            return;
        }
        throw new IllegalArgumentException("invalidate object:" + value);
    }

    void waitForSend(){
        synchronized (this){
            try {
                wait(1000);
            } catch (InterruptedException e) {
                logger.error("",e);
            }
        }
    }
    void notifySend(){
        synchronized (this){
            notifyAll();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(value.toString());
        TLVMessage tmp = this;
        while ((tmp = tmp.getNext()) != null){
            builder.append("->").append(tmp.getValue());
        }
        return builder.toString();

    }

    public static void main(String[] args){
        IoBuffer buffer = IoBuffer.wrap(new byte[]{
                0x02,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xCB,(byte)0xD5,(byte)0x41,(byte)0x31,(byte)0x32,(byte)0x33,(byte)0x38,(byte)0x38,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,(byte)0x32,(byte)0x30,(byte)0x31,(byte)0x37,(byte)0x30,(byte)0x37,(byte)0x30,(byte)0x34,(byte)0x31,(byte)0x37,(byte)0x35,(byte)0x32,(byte)0x33,(byte)0x31,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,(byte)0x32,(byte)0x30,(byte)0x31,(byte)0x37,(byte)0x30,(byte)0x37,(byte)0x30,(byte)0x34,(byte)0x32,(byte)0x31,(byte)0x31,(byte)0x30,(byte)0x35,(byte)0x37,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x31,(byte)0x30,(byte)0x30,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x20,(byte)0x4E,(byte)0x31,(byte)0x35,(byte)0x30,(byte)0x32,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x31,(byte)0x30,(byte)0x33,(byte)0x31,(byte)0x37,(byte)0x30,(byte)0x37,(byte)0x30,(byte)0x34,(byte)0x31,(byte)0x38,(byte)0x31,(byte)0x33,(byte)0x34,(byte)0x33,(byte)0x35,(byte)0x36,(byte)0x31,(byte)0x35,(byte)0x37,(byte)0x36,(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x20,(byte)0x50,(byte)0x31,(byte)0x35,(byte)0x30,(byte)0x32,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x31,(byte)0x30,(byte)0x34,(byte)0x31,(byte)0x37,(byte)0x30,(byte)0x37,(byte)0x30,(byte)0x34,(byte)0x31,(byte)0x38,(byte)0x34,(byte)0x32,(byte)0x33,(byte)0x39,(byte)0x31,(byte)0x30,(byte)0x39,(byte)0x34,(byte)0x37,(byte)0x37,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,(byte)0x32,(byte)0x30,(byte)0x31,(byte)0x37,(byte)0x30,(byte)0x37,(byte)0x30,(byte)0x34,(byte)0x31,(byte)0x37,(byte)0x35,(byte)0x32,(byte)0x33,(byte)0x31,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,(byte)0x32,(byte)0x30,(byte)0x31,(byte)0x37,(byte)0x30,(byte)0x37,(byte)0x30,(byte)0x34,(byte)0x31,(byte)0x38,(byte)0x34,(byte)0x32,(byte)0x31,(byte)0x32,(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x3E,(byte)0x99,(byte)0x99,(byte)0x9A,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x3E,(byte)0x99,(byte)0x99,(byte)0x9A
        });
        /*System.out.print(
        IoBuffer.allocate(10).putFloat(0.01f).flip().getHexDump());*/
        while (buffer.hasRemaining()){
            System.out.println(TLVMessage.parse(buffer,"gb2312"));
        }
    }
}
