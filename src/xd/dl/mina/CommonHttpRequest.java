package xd.dl.mina;

import xd.fw.mina.tlv.TLVMessage;

public class CommonHttpRequest extends SendRequest {

    TLVMessage request;
    @Override
    String svrAddress() {
        return request.getNextString(0);
    }

    @Override
    String[][] constructParams(TLVMessage request) {
        this.request = request;
        return new String[0][];
    }

    @Override
    protected String json() {
        return request.getNextString(1);
    }

    @Override
    protected boolean run() {
        return centerFlag;
    }
}
