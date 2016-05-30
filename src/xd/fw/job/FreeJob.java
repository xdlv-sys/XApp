package xd.fw.job;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.HttpClientTpl;
import xd.fw.service.ParkService;

import java.util.Date;

public class FreeJob extends EnterJob {

    @Override
    public void doExecute() throws Exception {
        logger.info("start to execute free job");
        int freeParkPosition = parkService.getFreeParkStation();

        String timeStamp = getTimeStamp();

        JSONObject tmp = post(svrAddress(), new String[][]{
                {"Parkingno", parkingNo},
                {"Freenum", String.valueOf(freeParkPosition)},
                {"Timestamp", timeStamp},
                {"Token", md5(timeStamp, parkingNo, CODE)}
        });
        logger.info("free result:" + tmp);
    }

    protected String svrAddress() {
        return "http://" + getServerHostName() + "/mobile/index.php?act=user&op=spaces";
    }
}