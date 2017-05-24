package xd.dl.mina;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class WatchProcess extends EnterProcess2 {
    @Value("${watch_address}")
    String watchAddress;

    public String[][] constructParams(TLVMessage request) throws Exception {
        return new String[][]{
                {"watchId", (String) request.getValue()},
                {"parkId", parkId},
                {"carNumber", (String) request.getNextValue(0)},
                {"startTime", transferDate((String) request.getNextValue(1))},
                {"outTime", transferDate((String) request.getNextValue(2))},
                {"price", String.valueOf(request.getNextValue(3))}
        };
    }

    @Override
    public String svrAddress() {
        return watchAddress;
    }
}