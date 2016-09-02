package xd.fw.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;

@Service
public class OutProcess extends SendRequest{

    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String) request.getValue();
        float price = (float)(request.getNext(0).getValue());
        String totalPrice = String.valueOf(request.getNextValue(1));
        String time1 = (String) request.getNext(2).getValue();
        String time2 = (String) request.getNext(3).getValue();
        int color = (int)request.getNextValue(4);
        String deviceNo = (String)request.getNextValue(5);
        String colorValue;
        switch(color){
            case 1:
                colorValue = "blue";
                break;
            case 2:
                colorValue = "yellow";
                break;
            case 3:
                colorValue = "white";
                break;
            default:
                colorValue = "black";
        }

        String timeStamp = getTimeStamp();
        return new String[][]{
                {"Parkingno", parkingNo},
                {"Carnumber", carNumber},
                {"CarPlateColor",colorValue },
                {"Uniquekey",uniqueKey},
                {"DeviceNo",deviceNo},
                {"Price", String.valueOf(price)},
                {"totalPrice", totalPrice},
                {"Time1", time1},
                {"Time2", time2},
                {"Timestamp", timeStamp},
                {"Token", md5(carNumber, timeStamp, parkingNo, uniqueKey)},
                {"IsOffPay", price > 0 ? "0" : "1"}
        };
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/mobile/index.php?v=2.0&act=thirdparking&op=leaveParking";
    }
}