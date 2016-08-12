package xd.fw.mina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Service
public class OutProcess extends SendRequest{

    long current = 1;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init(){
        final boolean[] hasCurrentSequence = new boolean[]{false};

        jdbcTemplate.query("select currentIndex from DCurrentSequence where currentDate=?"
                , new Object[]{new Date(System.currentTimeMillis())}, rs -> {
                    //check if has current data record
                    hasCurrentSequence[0] = true;
                    current = rs.getInt(1);
                    logger().info("current:" + current);
                });
        if (!hasCurrentSequence[0]){
            int success = jdbcTemplate.update("insert into DCurrentSequence values(?,?)",new Date(System.currentTimeMillis()),current);
            logger().info("insert new current date successfully:" + success);
        }
    }

    @PreDestroy
    public void save() {
        int success = jdbcTemplate.update("update DCurrentSequence set currentIndex=? where currentDate=?"
                , current, new Date(System.currentTimeMillis()));
        logger().info("save current date successfully:" + success);
    }

    String getCurrentDay() {
        return new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
    }

    synchronized String getCurrentSequence() {
        return String.format(getCurrentDay() + "%08d", current++);
    }

    public String[][] constructParams(TLVMessage request) throws Exception {
        String carNumber = (String) request.getValue();
        String price = String.valueOf(request.getNext(0).getValue());
        String time1 = (String) request.getNext(1).getValue();
        String time2 = (String) request.getNext(2).getValue();
        String timeStamp = getTimeStamp();
        return new String[][]{
                {"Parkno", getCurrentSequence()},
                {"Parkingno", parkingNo},
                {"Carnumber", carNumber},
                {"Price", price},
                {"Time1", time1},
                {"Time2", time2},
                {"Timestamp", timeStamp},
                {"Token", md5(carNumber, timeStamp, parkingNo, CODE)}
        };
    }

    @Override
    public String svrAddress() {
        return "http://" +  serverHostName + "/mobile/index.php?act=user&op=appearance";
    }
}