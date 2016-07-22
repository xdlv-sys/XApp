package xd.fw.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
@Service
public class OutJob implements SendRequest {

    long current = 1;
    @Autowired
    JdbcTemplate jdbcTemplate;

    Logger logger = Logger.getLogger(OutJob.class);

    @PostConstruct
    public void init(){
        final boolean[] hasCurrentSequence = new boolean[]{false};

        jdbcTemplate.query("select currentIndex from DCurrentSequence where currentDate=?"
                , new Object[]{new Date(System.currentTimeMillis())}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                //check if has current data record
                hasCurrentSequence[0] = true;
                current = rs.getInt(1);
                logger.info("current:" + current);
            }
        });
        if (!hasCurrentSequence[0]){
            int success = jdbcTemplate.update("insert into DCurrentSequence values(?,?)",new Date(System.currentTimeMillis()),current);
            logger.info("insert new current date successfully:" + success);
        }
    }

    @Override
    public void save() {
        int success = jdbcTemplate.update("update DCurrentSequence set currentIndex=? where currentDate=?"
                , current, new Date(System.currentTimeMillis()));
        logger.info("save current date successfully:" + success);
    }

    String getCurrentDay() {
        return new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
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