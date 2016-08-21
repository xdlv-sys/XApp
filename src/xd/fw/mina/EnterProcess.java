package xd.fw.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class EnterProcess extends SendRequest{

    @Override
    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String)request.getValue();
        String time1 = (String)request.getNext().getValue();
        String timeStamp = getTimeStamp();
        return new String[][]{
                {"Parkingno", parkingNo},
                {"Carnumber", carNumber},
                {"CarPlateColorType","4"},
                {"Uniquekey", uniqueKey},
                {"DeviceNo",deviceNo},
                {"Thumbname","default.jpg"},
                {"Time1", time1},
                {"Timestamp", timeStamp},
                {"Token", md5(carNumber, timeStamp, parkingNo, uniqueKey)}
        };
    }

    @Override
    public String svrAddress() {
        return "http://221.226.241.34:61158/mobile/index.php?v=2.0&act=thirdparking&op=approach";
    }
}
