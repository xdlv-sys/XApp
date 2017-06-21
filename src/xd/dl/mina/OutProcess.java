package xd.dl.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

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
        int isOffLine = (int)request.getNextValue(6);
        String orderNo = (String)request.getNextValue(7);

        String timeStamp = getTimeStamp();
        return new String[][]{
                {"parkingNo", parkingNo},
                {"orderNo", orderNo},
                {"carNumber", carNumber},
                {"enterTime", time1},
                {"outTime", time2},
                {"device_no",deviceNo},
        };
    }

    @Override
    public String svrAddress() {
        return "http://"+dhHost+"/order/completeUpload";
    }
}