package xd.dl.mina;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.dl.DlConst;
import xd.dl.bean.CarParkInfo;
import xd.dl.bean.Charge;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.PayOrder;
import xd.dl.service.ParkService;
import xd.fw.mina.tlv.ReversedHandler;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ParkHandler extends ReversedHandler {

    //static Logger logger = LoggerFactory.getLogger(ParkHandler.class);
    final SimpleDateFormat sdfForPayParkingFee = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final SimpleDateFormat sdfForNotifyCharge = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static final int QUERY_CAR = 3, PAY_FEE = 4, QUERY_CAR2 = 13, CHARGE_NOTIFY = 15;
    @Autowired
    ParkService parkService;

    @PostConstruct
    public void init() {
        addProxyListeners(this);
    }

    @Override
    protected void handlerRegistry(TLVMessage msg, IoSession session) {
        String parkId = (String) msg.getNextValue(0);
        ParkInfo parkInfo = parkService.get(ParkInfo.class, parkId);
        if (parkInfo == null) {
            parkInfo = new ParkInfo();
            parkInfo.setParkId(parkId);
        }
        parkInfo.setParkName((String) msg.getNextValue(1));
        parkInfo.setFreeCount((int) msg.getNextValue(2));

        if (msg.getNextValue(3) != null) {
            int proxyVersion = (int) msg.getNextValue(3);
            parkInfo.setProxyVersion(proxyVersion);
            session.setAttribute(PROXY_VERSION, proxyVersion);
        }
        parkInfo.setProxyState(DlConst.PARK_PROXY_STATUS_NORMAL);
        parkInfo.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        parkService.saveOrUpdate(parkInfo);
    }

    public boolean payParkingFee(PayOrder order) {
        TLVMessage message = createRequest(PAY_FEE, order.getCarNumber(), order.getTotalFee(),
                sdfForPayParkingFee.format(order.getTimeStamp()),
                order.getWatchId() == null ? "" : order.getWatchId()
                , order.getCarType(), order.getsId(), order.getEnterTime());
        TLVMessage ret = request(order.getParkId(), message);
        return ret != null && "OK".equals(ret.getValue());
    }

    public boolean chargeNotify(Charge charge){
        TLVMessage notifyCharge = createRequest(CHARGE_NOTIFY, charge.getOutTradeNo()
                , charge.getCarNumber()
                , charge.getRoomNumber(), charge.getCarPorts()
                , sdfForNotifyCharge.format(new Date(charge.getTimeStamp().getTime()))
                , charge.getMonths(), charge.getTotalFee());
        TLVMessage ret = request(charge.getParkId(), notifyCharge);
        return ret != null && "OK".equals(ret.getValue());
    }

    public CarParkInfo getCarParkInfo(String carNumber, String parkId, String watchId
            , byte carType, byte carOrder, String dbId, String enterTime, float scale) {
        TLVMessage queryMsg = createRequest(QUERY_CAR, carNumber
                , watchId == null ? "" : watchId, carType, carOrder, dbId, enterTime, scale);
        TLVMessage ret = request(parkId, queryMsg);

        if (ret != null) {
            return new CarParkInfo((String) ret.getValue(), (float) ret.getNextValue(0)
                    , (String) ret.getNextValue(1), (int) ret.getNextValue(2), parkId
                    , (byte[]) ret.getNextValue(4), carOrder, (String) ret.getNextValue(3));
        }
        return null;
    }

    public List<TLVMessage> queryCarInfoFromAllProxy(String carNumber) {
        TLVMessage message = createRequest(QUERY_CAR2, carNumber);
        return super.notifyAllId(message, 2);
    }
}
