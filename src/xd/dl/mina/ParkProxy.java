package xd.dl.mina;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang.StringUtils;
import org.apache.derby.iapi.services.io.ArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.dl.job.ParkNative;
import xd.dl.job.ParkedCarInfo;
import xd.dl.job.ViewCarportRoomInfo;
import xd.fw.mina.tlv.ReversedProxy;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.net.InetSocketAddress;

@Service
public class ParkProxy extends ReversedProxy {
    Logger logger = LoggerFactory.getLogger(ReversedProxy.class);

    static final int QUERY_CAR = 3, PAY_FEE = 4, QUERY_CAR2 = 13, PAY_FEE_NOTIFY = 9, CHARGE_NOTIFY = 15, NO_CARD_ENTRY = 16;

    @Autowired
    ParkHandler parkHandler;

    @Value("${center_ip}")
    String host;
    @Value("${center_port}")
    int port;

    @Value("${park_id}")
    String parkId;

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
    @Value("${park_name}")
    String parkName;

    @PostConstruct
    public void init() {
        ParkNative.initialized(dbHost, dbName, user, pwd);
    }

    @PreDestroy
    public void destroy() {
        super.destroy();
        logger.info("un initialize park native");
        ParkNative.unitialized();
    }

    @Override
    protected void constructRegistryMessage(TLVMessage registryMessage) {
        registryMessage.setNext(parkId).setNext(parkName).setNext(ParkNative.getLeftCount()).setNext(version);
    }

    @Override
    protected InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    private byte[] picBuffer = new byte[2 * 1024 * 1024];
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
                    parkedCarInfo = parkHandler.queryCarInfo(QUERY_CAR, watchId, carNumber);
                } else {
                    byte carType = (byte) msg.getNextValue(3);
                    byte carOrder = (byte) msg.getNextValue(4);
                    String dbId = (String) msg.getNextValue(5);
                    String enterTime = (String) msg.getNextValue(6, "");
                    if (StringUtils.isBlank(dbId)) {
                        parkedCarInfo = ParkNative.getParkedCarInfo("",carType, carNumber, 15, 1, carOrder, "", "");
                    } else {
                        parkedCarInfo = ParkNative.getParkedCarInfo("", carType, carNumber, 15, 2, carOrder, dbId, enterTime);
                    }
                }
                if (parkedCarInfo != null && parkedCarInfo.iReturn != 6
                        && parkedCarInfo.iReturn != 11) {
                    float scale = (float) msg.getNextValue(7);

                    TLVMessage tmp = next.setNext(parkedCarInfo.sCarLicense).setNext(parkedCarInfo.fMoney
                    ).setNext(parkedCarInfo.sInTime
                    ).setNext(parkedCarInfo.iParkedTime).setNext(parkedCarInfo.sID);

                    File picFile;
                    if (scale > 0 && StringUtils.isNotBlank(parkedCarInfo.sInPic)
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
                String timeStamp = (String) msg.getNextValue(3, "");
                watchId = (String) msg.getNextValue(4);
                boolean success;
                if (StringUtils.isNotBlank(watchId)) {
                    success = parkHandler.payFee(PAY_FEE, watchId, carNumber, totalFee);
                } else {
                    byte carType = (byte) msg.getNextValue(5);
                    String sId = (String) msg.getNextValue(6, "");
                    String searchTime = (String) msg.getNextValue(7, "");

                    success = ParkNative.payParkCarFee("",carType, carNumber
                            ,timeStamp, totalFee, sId, searchTime, 1, "","","","",0,0,0) == 0;
                }
                next.setNext(success ? "OK" : "FAIL");
                response(msg);
                break;
            case QUERY_CAR2:
                carNumber = (String) msg.getNextValue(1);
                ViewCarportRoomInfo[] carportInfos = ParkNative.getCarportInfo(carNumber, null,0,0);
                if (carportInfos == null || carportInfos.length == 0) {
                    logger.info("there is no carportInfo for query:{}", carNumber);
                    next.setNext(NULL_MSG);
                    response(msg);
                    return;
                }
                next = next.setNext(parkId).setNext(carportInfos.length);
                for (ViewCarportRoomInfo info : carportInfos) {
                    next = next.setNext(safe(info.sCarportNum)).setNext(safe(info.sRoomNum))
                            .setNext(safe(info.sName))
                            .setNext(safe(info.sAddress))
                            .setNext(safe(info.sPhoneNumber))
                            .setNext(safe(info.sPosition))
                            .setNext(safe(info.sStartDate))
                            .setNext(safe(info.sEndDate))
                            .setNext(info.fDeposit)
                            .setNext(info.bTemporary)
                            .setNext(safe(info.sRemark))
                            .setNext(safe(info.sRentName))
                            .setNext(info.fRentMoney)
                            .setNext(safe(info.sParkName));
                }
                logger.info("return query_car-2 : {}", msg);
                response(msg);
                break;
            case CHARGE_NOTIFY:
                String outTradeNo = (String) msg.getNextValue(1);
                carNumber = (String) msg.getNextValue(2);
                String roomNumber = (String) msg.getNextValue(3);
                String carPorts = (String) msg.getNextValue(4);
                String payTime = (String) msg.getNextValue(5);
                int months = (Byte) msg.getNextValue(6);
                totalFee = (float) msg.getNextValue(7);

                String[] carPortArray = carPorts.split(",");
                success = ParkNative.payCarportRent(carNumber, roomNumber, carPortArray[0]
                        , carPortArray, payTime, outTradeNo, months, totalFee
                        , carPortArray.length > 1,0,0,0,0,0) == 0;

                next.setNext(success ? "OK" : "FAIL");
                response(msg);
                break;
            case NO_CARD_ENTRY:
                watchId = (String) msg.getNextValue(1);
                String userId = (String)msg.getNextValue(2);

                logger.info("no card: {}-{}", watchId, userId);
                success = parkHandler.noCardEntry(NO_CARD_ENTRY, watchId, userId);
                next.setNext(success ? "OK" : "OK");
                response(msg);
                break;
            default:
                throw new Exception("can not recognize the code:" + msg.getValue());
        }
    }

    String safe(String str) {
        return str == null ? "" : str;
    }
}

