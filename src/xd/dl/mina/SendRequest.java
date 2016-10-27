package xd.dl.mina;

import org.springframework.beans.factory.annotation.Value;
import xd.dl.job.IDongHui;
import xd.fw.mina.tlv.TLVMessage;

abstract class SendRequest implements IDongHui{

    @Value("${park_no}")
    String parkingNo;

    @Value("${unique_key}")
    String uniqueKey;
    @Value("${dh_host}")
    String dhHost;

    abstract String[][] constructParams(TLVMessage request) throws Exception;

    abstract String svrAddress();

    void constructMessage(TLVMessage ret,TLVMessage request){
        ret.setNext(request.getNextValue(0)).setNext(request.getNextValue(1));
    }
}