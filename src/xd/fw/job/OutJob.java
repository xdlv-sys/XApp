package xd.fw.job;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.HttpClientTpl;
import xd.fw.bean.EnterOrOutRecord;
import xd.fw.service.ParkService;

import java.util.Date;
import java.util.List;

public class OutJob extends EnterJob {

    protected List<EnterOrOutRecord> getRecords(){
        return parkService.geUploadRecords(1);
    }
    protected String[][] contructParams(String timeStamp,EnterOrOutRecord enterOrOutRecord)throws Exception{
        return new String[][]{
                {"Parkno", enterOrOutRecord.getOrderNum()},
                {"Parkingno", parkingNo},
                {"Carnumber", enterOrOutRecord.getCarNumber()},
                {"Price", enterOrOutRecord.getPrice().toString()},
                {"Time1", sdf.format(enterOrOutRecord.getEnterTime())},
                {"Time2", sdf.format(enterOrOutRecord.getOutTime())},
                {"Timestamp", timeStamp},
                {"Token", md5(enterOrOutRecord.getCarNumber(), timeStamp, parkingNo, CODE)}
        };
    }

    @Override
    protected String svrAddress(){
        return "http://" + getServerHostName() + "/mobile/index.php?act=user&op=appearance";
    }
}