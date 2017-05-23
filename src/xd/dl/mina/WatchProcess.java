package xd.dl.mina;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class WatchProcess extends EnterProcess2 {
    @Value("${watch_address}")
    String watchAddress;

    public String[][] constructParams(TLVMessage request) throws Exception {
        String watchId = (String) request.getValue();
        String carNumber = (String) request.getNextValue(0);
        return new String[][]{
                {"watchId", watchId},
                {"parkId", parkId},
                {"carNumber", carNumber}
        };
    }

    @Override
    public String svrAddress() {
        return watchAddress;
    }
}