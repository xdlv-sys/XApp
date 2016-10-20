package xd.fw.scheduler;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.XmlUtils;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.apache.http.Consts;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import xd.fw.WxUtil;
import xd.fw.bean.Const;
import xd.fw.bean.ParkInfo;
import xd.fw.bean.PayOrder;
import xd.fw.mina.ParkHandler;
import xd.fw.service.ParkService;
import xd.fw.service.PayService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Async
public class UpgradeProxyListener implements ApplicationListener<UpgradeProxyEvent>, Const {
    static Logger logger = LoggerFactory.getLogger(UpgradeProxyListener.class);
    @Autowired
    ParkHandler parkHandler;

    @Override
    public void onApplicationEvent(UpgradeProxyEvent upgradeProxyEvent){
        ParkInfo parkInfo = (ParkInfo)upgradeProxyEvent.getSource();
        try {
            logger.info("start to upgrade {} from {}", parkInfo.getParkId(),parkInfo.getProxyVersion());
            parkHandler.upgrade(parkInfo.getParkId(), parkInfo.getProxyVersion());
            logger.info("end to upgrade {} from {}", parkInfo.getParkId(),parkInfo.getProxyVersion());
        } catch (Exception e) {
            logger.error("",e);
        }
    }
}
