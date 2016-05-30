package xd.fw.job;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.HttpClientTpl;
import xd.fw.bean.EnterOrOutRecord;
import xd.fw.mina.tlv.TLVMessage;
import xd.fw.service.ParkService;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class OutJob implements SendRequest {

    long current = 1;
    String currentDate;
    String parkProperties = "/park.properties";

    public OutJob() {
        Properties properties = new Properties();
        try (InputStream ins = SendRequest.class.getResourceAsStream(parkProperties)) {
            properties.load(ins);
            String currentStr = properties.getProperty("current");
            if (StringUtils.isNotBlank(currentStr)) {
                current = Long.parseLong(currentStr);
            }
            logger().info("current sequence:" + current);

            String currentDate = properties.getProperty("currentDate");
            //reset current when another new day
            if (!getCurrentDay().equals(currentDate)) {
                current = 1;
            }
        } catch (Exception e) {
            logger().error("", e);
        }
    }

    @Override
    public void save() {
        Properties properties = new Properties();
        try (OutputStream outs = new FileOutputStream(SendRequest.class.getResource(parkProperties).getFile())) {
            properties.setProperty("current", String.valueOf(current));
            properties.setProperty("currentDate", getCurrentDay());
            properties.store(outs, "");
        } catch (Exception e) {
            logger().error("", e);
        }
    }

    String getCurrentDay() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    synchronized String getCurrentSequence() {
        return String.format(getCurrentDay() + "%08d", current++);
    }

    @Override
    public String[][] constructParams(String timeStamp, TLVMessage request) throws Exception {
        String[] parkingNoAndHostName = getParkingNoAndHostName();
        String carNumber = (String) request.getValue();
        String price = String.valueOf(request.getNext(0).getValue());
        String time1 = (String) request.getNext(1).getValue();
        String time2 = (String) request.getNext(2).getValue();

        return new String[][]{
                {"Parkno", getCurrentSequence()},
                {"Parkingno", parkingNoAndHostName[0]},
                {"Carnumber", carNumber},
                {"Price", price},
                {"Time1", time1},
                {"Time2", time2},
                {"Timestamp", timeStamp},
                {"Token", md5(carNumber, timeStamp, parkingNoAndHostName[0], CODE)}
        };
    }

    @Override
    public String svrAddress() {
        return "http://" +  getParkingNoAndHostName()[1] + "/mobile/index.php?act=user&op=appearance";
    }
}