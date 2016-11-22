package xd.dl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.HttpClientTpl;
import xd.fw.job.BaseJob;
import xd.dl.service.ParkService;

@Service
public class FreeJob extends BaseJob implements IDongHui{
    @Autowired
    ParkService parkService;

    @Value("${park_no}")
    String parkingNo;

    @Value("${unique_key}")
    String uniqueKey;

    @Override
    public void doExecute() throws Exception {
        logger.info("start to execute free job");
        LeftParkInfo[] infos = ParkNative.getLeftParkInfo();

        String timeStamp = getTimeStamp();

        String ret = HttpClientTpl.post(svrAddress(), new String[][]{
                {"Parkingno", parkingNo},
                {"Freenum", String.valueOf(infos[0].iLeftNum + infos[1].iLeftNum)},
                {"Timestamp", timeStamp},
                {"Uniquekey",uniqueKey},
                {"Token", md5(timeStamp, parkingNo, uniqueKey)}
        });
        logger.info("free result:" + ret);
    }

    protected String svrAddress() {
        return "http://221.226.241.34:61158/mobile/index.php?v=2.0&act=thirdparking&op=spaces";
    }

}