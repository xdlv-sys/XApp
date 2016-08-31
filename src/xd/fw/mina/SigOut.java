package xd.fw.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class SigOut extends SendRequest {

    @Override
    String[][] constructParams(TLVMessage request) throws Exception {
        String deviceNo = (String)request.getValue();
        String timeStamp = getTimeStamp();
        return new String[][]{
                {"Parkingno", parkingNo},
                {"DeviceNo",deviceNo},
                {"Uniquekey", uniqueKey},
                {"SignoutStatus","1"},
                {"Timestamp", timeStamp},
                {"Token", md5(timeStamp, parkingNo, uniqueKey)}
        };
    }

    @Override
    String svrAddress() {
        return "http://"+dhHost+"/mobile/index.php?v=2.0&act=thirdparking&op=signout";
    }
}
