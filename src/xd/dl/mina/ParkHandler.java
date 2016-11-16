package xd.dl.mina;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import xd.dl.DlConst;
import xd.dl.bean.CarParkInfo;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.PayOrder;
import xd.dl.service.ParkService;
import xd.fw.FwUtil;
import xd.fw.mina.tlv.ReversedHandler;
import xd.fw.mina.tlv.TLVMessage;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

@Service
public class ParkHandler extends ReversedHandler {

    //static Logger logger = LoggerFactory.getLogger(ParkHandler.class);

    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static final int QUERY_CAR = 3, PAY_FEE = 4;
    @Autowired
    ParkService parkService;
    @PostConstruct
    public void init(){
        addProxyListeners(this);
    }

    @Override
    protected void handlerRegistry(TLVMessage msg, IoSession session) {
        String parkId = (String)msg.getNextValue(0);
        ParkInfo parkInfo = parkService.get(ParkInfo.class,parkId);
        if (parkInfo == null){
            parkInfo = new ParkInfo();
            parkInfo.setParkId(parkId);
        }
        parkInfo.setParkName((String)msg.getNextValue(1));
        parkInfo.setFreeCount((int)msg.getNextValue(2));

        if (msg.getNextValue(3) != null){
            parkInfo.setProxyVersion((int)msg.getNextValue(3));
        }
        parkInfo.setProxyState(DlConst.PARK_PROXY_STATUS_NORMAL);
        parkInfo.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        parkService.saveOrUpdate(parkInfo);
    }

    public boolean payParkingFee(PayOrder order){
        TLVMessage message = createRequest(PAY_FEE,order.getCarNumber(),order.getTotalFee(),
                sdf.format(order.getTimeStamp()),
                order.getWatchId() == null ? "" : order.getWatchId(), order.getCarType());
        TLVMessage ret = request(order.getParkId(),message);
        return ret != null && "OK".equals(ret.getValue());
    }

    public CarParkInfo getCarParkInfo(String carNumber, String parkId, String watchId
            , byte carType, byte carOrder){
        TLVMessage queryMsg = createRequest(QUERY_CAR, carNumber
                , watchId == null ? "" : watchId , carType,carOrder);
        TLVMessage ret = request(parkId, queryMsg);

        if (ret != null){
            return new CarParkInfo((String)ret.getValue(), (float) ret.getNextValue(0)
                    , (String) ret.getNextValue(1), (int) ret.getNextValue(2), parkId
                    , (byte[])ret.getNextValue(3),carOrder);
        }
        return null;
    }
}
