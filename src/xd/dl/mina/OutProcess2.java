package xd.dl.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class OutProcess2 extends EnterProcess2 {

    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String) request.getValue();
        String time1 = (String) request.getNext(2).getValue();
        String time2 = (String) request.getNext(3).getValue();
        return new String[][]{
                {"parkId", parkId},
                {"carNumber", carNumber},
                {"enterTime", transferDate(time1)},
                {"outTime", transferDate(time2)}
        };
    }
}