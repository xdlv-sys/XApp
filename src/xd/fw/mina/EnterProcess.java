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
                {"Time1", time1},
                {"Timestamp", timeStamp},
                {"Token", md5(carNumber, timeStamp, parkingNo, CODE)}
        };
    }

    @Override
    public String svrAddress() {
        return "http://" + serverHostName + "/mobile/index.php?act=user&op=approach";
    }
}
