package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.Const;
import xd.fw.bean.ParkInfo;
import xd.fw.bean.PayOrder;
import xd.fw.mina.ParkHandler;
import xd.fw.scheduler.UpgradeProxyEvent;
import xd.fw.service.ParkService;

import java.util.List;

public class ParkAction extends ParkBaseAction {

    List<ParkInfo> parkInfos;

    List<PayOrder> payOrders;

    ParkInfo parkInfo;

    PayOrder payOrder;
    String command;
    String directory;
    String prefix;
    @Autowired
    ParkHandler parkHandler;

    public String obtainParkInfos() {
        total = parkService.getAllCount(ParkInfo.class);
        parkInfos = parkService.getList(ParkInfo.class, "parkId", start, limit);
        //reset some data for security
        for (ParkInfo parkInfo : parkInfos) {
            parkInfo.setAliShaRsaKey(null);
            parkInfo.setMchId(null);
            parkInfo.setSecret(null);
            parkInfo.setWxKey(null);
        }
        return SUCCESS;
    }

    public String obtainPayOrders() {
        total = parkService.getAllCount(PayOrder.class, payOrder);
        payOrders = parkService.getList(PayOrder.class, payOrder, "timeStamp asc", start, limit);
        return SUCCESS;
    }

    public String refreshStatus() {
        for (int i = 0; parkInfos != null && i < parkInfos.size(); i++) {
            ParkInfo parkInfo = parkService.get(ParkInfo.class, parkInfos.get(i).getParkId());
            parkInfo.setProxyState(Const.PARK_PROXY_STATUS_DISCONNECT);
            parkInfo.setFreeCount(-1);
            parkService.saveOrUpdate(parkInfo);
        }
        return FINISH;
    }

    public String executeCommand() throws Exception{
        String[] result = parkHandler.executeCmd(parkInfo.getParkId(),directory,prefix, command);
        directory = result[0];
        command = result[1];
        return SUCCESS;
    }

    public String upgradeProxy() throws Exception{
        for (int i = 0; parkInfos != null && i < parkInfos.size(); i++) {
            ParkInfo parkInfo = parkService.get(ParkInfo.class, parkInfos.get(i).getParkId());
            applicationContext.publishEvent(new UpgradeProxyEvent(parkInfo));
        }
        return FINISH;
    }

    public String saveParkInfo() {
        parkService.save(parkInfo);
        return FINISH;
    }

    public void setParkInfos(List<ParkInfo> parkInfos) {
        this.parkInfos = parkInfos;
    }

    public List<ParkInfo> getParkInfos() {
        return parkInfos;
    }

    public List<PayOrder> getPayOrders() {
        return payOrders;
    }

    public void setParkInfo(ParkInfo parkInfo) {
        this.parkInfo = parkInfo;
    }

    public ParkInfo getParkInfo() {
        return parkInfo;
    }

    public PayOrder getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(PayOrder payOrder) {
        this.payOrder = payOrder;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
