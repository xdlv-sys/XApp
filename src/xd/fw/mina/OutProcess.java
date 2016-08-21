package xd.fw.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class OutProcess extends SendRequest{

    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String) request.getValue();
        String price = String.valueOf(request.getNext(0).getValue());
        String time1 = (String) request.getNext(1).getValue();
        String time2 = (String) request.getNext(2).getValue();
        String timeStamp = getTimeStamp();
        return new String[][]{
                {"Parkingno", parkingNo},
                {"Carnumber", carNumber},
                {"CarPlateColor","blue"},
                {"Uniquekey",uniqueKey},
                {"DeviceNo",deviceNo},
                {"Price", price},
                {"Time1", time1},
                {"Time2", time2},
                {"Timestamp", timeStamp},
                {"Token", md5(carNumber, timeStamp, parkingNo, uniqueKey)},
                {"IsOffPay", Float.parseFloat(price) > 0 ? "0" : "1"}
        };
    }

    @Override
    public String svrAddress() {
        return "http://221.226.241.34:61158/mobile/index.php?v=2.0&act=thirdparking&op=leaveParking";
    }
}