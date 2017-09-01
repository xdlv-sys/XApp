package xd.dl.mina;

import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

@Service
public class OutProcess2 extends BaseEnterOutProcess {

    public String[][] constructParams(TLVMessage request) throws Exception {
        String[][] superParams = super.constructParams(request);
        if (superParams != null){
            return superParams;
        }

        String carNumber = (String) request.getValue();
        String time1 = (String) request.getNext(2).getValue();
        String time2 = (String) request.getNext(3).getValue();
        return new String[][]{
                {"parkId", parkId},
                {"carNumber", carNumber},
                {"enterTime", convertDate(time1)},
                {"outTime", convertDate(time2)}
        };
    }

    @Override
    int accessType() {
        return 1;
    }
}