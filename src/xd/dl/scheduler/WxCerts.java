package xd.dl.scheduler;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.I18n;
import xd.dl.bean.ParkInfo;
import xd.dl.service.ParkService;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxCerts {
    Map<String, SSLConnectionSocketFactory> sslMap = new HashMap<>();
    @Autowired
    ParkService parkService;

    Logger logger = LoggerFactory.getLogger(WxCerts.class);

    @PostConstruct
    public void loadCert() throws Exception {
        File[] files = new File(I18n.getWebInfDir(), "cert").listFiles(f -> f.isDirectory());
        for (File file : files) {
            String parkId = file.getName();
            ParkInfo parkInfo = parkService.get(ParkInfo.class, parkId);
            if (parkInfo == null) {
                continue;
            }

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream stream = new FileInputStream(
                    new File(file, "apiclient_cert.p12"))) {
                keyStore.load(stream, parkInfo.getMchId().toCharArray());
            }
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, parkInfo.getMchId().toCharArray())
                    .build();
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            sslMap.put(parkId, ssf);
            logger.info("loaded cert {}", parkId);
        }
    }

    public CloseableHttpClient getClientByParkId(String parkId){
        SSLConnectionSocketFactory ssf = sslMap.get(parkId);
        return HttpClients.custom()
                .setSSLSocketFactory(ssf)
                .build();
    }
}
