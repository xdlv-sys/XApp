package xd.fw.job;

import org.apache.log4j.Logger;
import xd.fw.FwUtil;
import xd.fw.mina.tlv.TLVMessage;

interface SendRequest {

    String CODE = "fsAsf2015";
    String[][] constructParams(String timeStamp, TLVMessage request) throws Exception;

    String svrAddress();

    default String[] getParkingNoAndHostName() {
        FreeJob freeJob = (FreeJob) FwUtil.getBean("freeJob");
        return new String[]{freeJob.parkingNo, freeJob.serverHostName};
    }

    default void save() {
    }
    default Logger logger(){
        return Logger.getLogger(getClass());
    }

    default String md5(String... strings) throws Exception {
        StringBuffer buffer = new StringBuffer();
        for (String string : strings) {
            buffer.append(string);
        }
        return FwUtil.md5(buffer.toString());
    }

    default String getCarNumber(TLVMessage request){
        return (String)request.getValue();
    }
}