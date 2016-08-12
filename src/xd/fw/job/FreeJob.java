package xd.fw.job;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.service.ParkService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FreeJob extends BaseJob implements IDongHui{
    @Autowired
    ParkService parkService;

    @Value("${park_no}")
    String parkingNo;

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
        return "http://" + serverHostName + "/mobile/index.php?act=user&op=spaces";
    }

}