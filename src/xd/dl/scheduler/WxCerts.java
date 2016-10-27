package xd.dl.scheduler;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xd.fw.I18n;
<<<<<<< ad674155dd3266a10bf86d9535f55c7cf32768c3:src/xd/fw/scheduler/WxCerts.java
=======
import xd.dl.bean.ParkInfo;
import xd.dl.service.ParkService;
>>>>>>> v1.0:src/xd/dl/scheduler/WxCerts.java

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

    Logger logger = LoggerFactory.getLogger(WxCerts.class);

    @PostConstruct
    public void loadCert() throws Exception {
        File[] files = new File(I18n.getWebInfDir(), "cert").listFiles(File::isDirectory);
        if (files == null) {
            return;
        }
        String fileName, id, pwd;
        int index;
        for (File file : files) {
            fileName = file.getName();
            index = fileName.indexOf("@");
            id = fileName.substring(0, index);
            pwd = fileName.substring(index + 1);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream stream = new FileInputStream(
                    new File(file, "apiclient_cert.p12"))) {
                keyStore.load(stream, pwd.toCharArray());
            }
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, pwd.toCharArray())
                    .build();
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            sslMap.put(id, ssf);
            logger.info("loaded cert {}", id);
        }
    }

    public CloseableHttpClient getClientById(String id) {
        SSLConnectionSocketFactory ssf = sslMap.get(id);
        return HttpClients.custom()
                .setSSLSocketFactory(ssf)
                .build();
    }
}