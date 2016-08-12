package xd.fw.mina;

import org.springframework.beans.factory.annotation.Value;
import xd.fw.job.IDongHui;
import xd.fw.mina.tlv.TLVMessage;

abstract class SendRequest implements IDongHui{

    @Value("${park_no}")
    String parkingNo;

    abstract String[][] constructParams(TLVMessage request) throws Exception;

    abstract String svrAddress();

    String getCarNumber(TLVMessage request){
        return (String)request.getValue();
    }
}