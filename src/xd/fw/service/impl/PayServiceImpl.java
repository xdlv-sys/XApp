package xd.fw.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.bean.Const;
import xd.fw.bean.PayOrder;
import xd.fw.scheduler.NotifyProxyEvent;
import xd.fw.service.PayService;

@Service
public class PayServiceImpl extends HibernateServiceImpl implements PayService {

    @Autowired
    ApplicationContext applicationContext;

    @Transactional("transactionManager")
    @Override
    public boolean updateInitialPayOrderStatus(String out_trade_no,String trade_no, short payOrderStatus) {
        PayOrder order = htpl.load(PayOrder.class, out_trade_no);
        //just return if the status of order was handled before
        if (order.getPayStatus() != Const.STATUS_INITIAL){
            return false;
        }
        order.setPayStatus(payOrderStatus);
        if (StringUtils.isNotBlank(trade_no)){
            order.setTradeNo(trade_no);
        }
        htpl.update(order);

        if (payOrderStatus == Const.STATUS_SUCCESS){
            //insert notify task if the status is success
            applicationContext.publishEvent(new NotifyProxyEvent(order.getOutTradeNo()));
        }
        return true;
    }

    @Transactional("transactionManager")
    @Override
    public boolean updatePayOrderStatus(String outTradeNo, short payOrderStatus) {
        PayOrder order = htpl.load(PayOrder.class, outTradeNo);

        order.setPayStatus(payOrderStatus);

        htpl.update(order);

        return true;
    }

    @Transactional("transactionManager")
    @Override
    public boolean updateNotifyStatus(String outTradeNo, short notifyStatus) {
        PayOrder order = htpl.load(PayOrder.class, outTradeNo);

        order.setNotifyStatus(notifyStatus);

        htpl.update(order);

        return true;
    }
}
