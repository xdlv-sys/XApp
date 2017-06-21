package xd.dl.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class EnterProcess extends SendRequest{

    @Override
    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String)request.getValue();
        int color = (int)request.getNextValue(1);
        String deviceNo = (String)request.getNextValue(2);
        String orderNo = (String)request.getNextValue(3);
        String timeStamp = getTimeStamp();
        return new String[][]{
                {"parkingNo", parkingNo},
                {"orderNo", orderNo},
                {"carNumber", carNumber},
                {"carPlateColorType",String.valueOf(color)},
                {"enterTime", timeStamp},
                {"deviceCode",deviceNo}
        };
    }

    @Override
    void constructMessage(TLVMessage ret, TLVMessage request) {
        ret.setNext(request.getNextValue(0)).setNext(0);
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/order/add";
    }
}
