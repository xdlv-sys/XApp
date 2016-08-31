package xd.fw.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class EnterProcess extends SendRequest{

    @Override
    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String)request.getValue();
        String time1 = (String)request.getNextValue(0);
        int color = (int)request.getNextValue(1);
        String deviceNo = (String)request.getNextValue(2);
        String timeStamp = getTimeStamp();
        return new String[][]{
                {"Parkingno", parkingNo},
                {"Carnumber", carNumber},
                {"CarPlateColorType",String.valueOf(color)},
                {"Uniquekey", uniqueKey},
                {"DeviceNo",deviceNo},
                {"Thumbname",timeStamp + ".jpg"},
                {"Time1", time1},
                {"Timestamp", timeStamp},
                {"Token", md5(carNumber, timeStamp, parkingNo, uniqueKey)}
        };
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/mobile/index.php?v=2.0&act=thirdparking&op=approach";
    }
}
