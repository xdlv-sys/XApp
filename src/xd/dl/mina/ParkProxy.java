package xd.dl.mina;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang.StringUtils;
import org.apache.derby.iapi.services.io.ArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.dl.job.ParkNative;
import xd.dl.job.ParkedCarInfo;
import xd.fw.mina.tlv.ReversedProxy;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;

@Service
public class ParkProxy extends ReversedProxy {

    static final int QUERY_CAR = 3, PAY_FEE = 4, PAY_FEE_NOTIFY = 9;

    @Autowired
    ParkHandler parkHandler;

    @Value("${center_ip}")
    String host;
    @Value("${center_port}")
    int port;

    @Value("${park_id}")
    String parkId;

    @Value("${park_name}")
    String parkName;

    @Value("${version}")
    int version;

    @Value("${db_host}")
    String dbHost;
    @Value("${db_name}")
    String dbName;
    @Value("${db_user}")
    String user;
    @Value("${db_pwd}")
    String pwd;

    @PostConstruct
    public void init() {
        ParkNative.initialized(dbHost, dbName, user, pwd);
    }

    @PreDestroy
    public void destroy() {
        super.destroy();
        ParkNative.unitialized();
    }

    @Override
    protected void constructRegistryMessage(TLVMessage registryMessage) {
        registryMessage.setNext(parkId).setNext(parkName).setNext(100).setNext(version);
    }

    @Override
    protected InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    byte[] picBuffer = new byte[2 * 1024 * 1024];
    private ArrayOutputStream aos = new ArrayOutputStream(picBuffer);

    @Override
    protected void handlerQuery(TLVMessage msg) throws Exception {
        logger.info("receive:" + msg);
        //2->1471225285910->苏A12345->001(parkId)->0(carType)->1 (carOrder)
        //reuse msg
        int code = (int) msg.getValue();
        TLVMessage next = msg.getNext(0);
        switch (code) {
            case QUERY_CAR:
                String carNumber = (String) msg.getNextValue(1);
                String watchId = (String) msg.getNextValue(2);
                ParkedCarInfo parkedCarInfo;
                if (StringUtils.isNotBlank(watchId)) {
                    /*parkedCarInfo = new ParkedCarInfo();
                    parkedCarInfo.sCarLicense = "苏A12300";
                    parkedCarInfo.fMoney = 0.01f;
                    parkedCarInfo.iParkedTime = 162;
                    parkedCarInfo.sInTime = "2017-10-10 00:00：01";*/
                    parkedCarInfo = parkHandler.queryCarInfo(QUERY_CAR,watchId,carNumber);
                } else {
                    byte carType = (byte) msg.getNextValue(3);
                    byte carOrder = (byte) msg.getNextValue(4);
                    parkedCarInfo = ParkNative.getParkedCarInfo(carType, carNumber, 15, true, carOrder);
                }
                if (parkedCarInfo != null && parkedCarInfo.iReturn != 6
                        && parkedCarInfo.iReturn != 11) {
                    float scale = (float)msg.getNextValue(5);

                    TLVMessage tmp = next.setNext(parkedCarInfo.sCarLicense).setNext(parkedCarInfo.fMoney
                    ).setNext(parkedCarInfo.sInTime
                    ).setNext(parkedCarInfo.iParkedTime);

                    File picFile;
                    if (StringUtils.isNotBlank(parkedCarInfo.sInPic)
                            && (picFile = new File(parkedCarInfo.sInPic)).exists()) {
                        aos.setPosition(0);
                        Thumbnails.of(picFile).scale(scale).toOutputStream(aos);
                        byte[] data = new byte[aos.getPosition()];
                        System.arraycopy(picBuffer, 0, data, 0, data.length);
                        tmp.setNext(data);
                        logger.info("compress:{},{}", picFile.length(), data.length);
                    }
                } else {
                    //just return null
                    next.setNext(NULL_MSG);
                }

                response(msg);
                break;
            case PAY_FEE:
                carNumber = (String) msg.getNextValue(1);
                float totalFee = (float) msg.getNextValue(2);
                String timeStamp = (String) msg.getNextValue(3);
                watchId = (String) msg.getNextValue(4);
                boolean success;
                if (StringUtils.isNotBlank(watchId)) {
                    success = parkHandler.payFee(PAY_FEE, watchId, carNumber, totalFee);
                } else {
                    byte carType = (byte) msg.getNextValue(5);
                    success = ParkNative.payParkCarFee(carType, carNumber, timeStamp, totalFee) == 0;
                }
                next.setNext(success ? "OK" : "FAIL");
                response(msg);
                break;
            default:
                throw new Exception("can not recognize the code:" + msg.getValue());
        }
    }
}
