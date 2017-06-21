package xd.dl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.dl.service.ParkService;
import xd.fw.HttpClientTpl;
import xd.fw.job.BaseJob;

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

        String timeStamp = getTimeStamp();

        int leftCount = ParkNative.getLeftCount();
        String ret = HttpClientTpl.post(svrAddress(), new String[][]{
                {"Parkingno", parkingNo},
                {"Freenum", String.valueOf(leftCount)},
                {"Timestamp", timeStamp},
                {"Uniquekey",uniqueKey},
                {"Token", md5(timeStamp, parkingNo, uniqueKey)}
        });
        logger.info("free {} result {}",leftCount,ret);
    }

    protected String svrAddress() {
        return "http://221.226.241.34:61158/mobile/index.php?v=2.0&act=thirdparking&op=spaces";
    }

}