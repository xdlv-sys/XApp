package xd.fw.service;

public interface PayService extends BaseService{

    boolean updateInitialPayOrderStatus(String out_trade_no,String trade_no, short payOrderStatus);

    boolean updatePayOrderStatus(String outTradeNo, short payOrderStatus);

    boolean updateNotifyStatus(String outTradeNo, short notifyStatus);
}
