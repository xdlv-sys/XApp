package xd.fw;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public abstract class AliClient {
    AlipayClient alipayClient;
    @Value("${ali_app_id}")
    String appId;

    @Value("${rsa_key}")
    String rsaKey;

    @PostConstruct
    public void init() {
        alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do"
                , getAppId(), getRsaKey(), "json", "GBK", null);
    }

    public String getAppId() {
        return appId;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public AlipayClient getAlipayClient() {
        return alipayClient;
    }

}
