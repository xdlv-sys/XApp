package xd.fw.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class AutoPay extends SendRequest{

    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String) request.getValue();
        String price = String.valueOf(request.getNext(0).getValue());
        String totalPrice = String.valueOf(request.getNextValue(1));
        String time1 = (String) request.getNext(2).getValue();
        String time2 = (String) request.getNext(3).getValue();
        String deviceNo = (String)request.getNextValue(4);

        String timeStamp = getTimeStamp();
        return new String[][]{
                {"Parkingno", parkingNo},
                {"Carnumber", carNumber},
                {"Uniquekey",uniqueKey},
                {"DeviceNo",deviceNo},
                {"Price", price},
                {"TotalPrice", totalPrice},
                {"Timestamp", timeStamp},
                {"Time1", time1},
                {"Time2", time2},
                {"Token", md5(carNumber, timeStamp, parkingNo, uniqueKey)}
        };
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/mobile/index.php?v=2.0&act=thirdparking&op=autoPay";
    }
}