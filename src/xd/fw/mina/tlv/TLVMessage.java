package xd.fw.mina.tlv;

import org.apache.mina.core.buffer.IoBuffer;
import xd.fw.FwUtil;

import java.io.UnsupportedEncodingException;

public class TLVMessage {
    public static final byte BYTE_TYPE = 1, INT_TYPE = 2, LONG_TYPE = 3, FLOAT_TYPE = 4, DOUBLE_TYPE = 5, STRING_TYPE = 6;
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
        return next;
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
        throw new IllegalArgumentException("invalidate object:" + value);
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(value.toString());
        TLVMessage tmp = this;
        while ((tmp = tmp.getNext()) != null){
            stringBuffer.append("->").append(tmp.getValue());
        }
        return stringBuffer.toString();

    }

    public static void main(String[] args){
        TLVMessage root = new TLVMessage("hello");
        root.setNext(12).setNext(new TLVMessage(12.01f)).setNext("end");

        System.out.println(root);
    }
}
