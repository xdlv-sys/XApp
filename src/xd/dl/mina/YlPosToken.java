package xd.dl.mina;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.dl.job.IDongHui;
import xd.fw.FwException;
import xd.fw.HttpClientTpl;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class YlPosToken implements IDongHui {
    @Value("${yl_pos_flag}")
    boolean ylFlag;

    @Value("${pos_token_url}")
    String tokenUrl;

    @Value("${yl_tid_map}")
    String ylTidValue;
    Map<String, String> ylTidMap = new HashMap<>();

    Logger logger = LoggerFactory.getLogger(YlPosToken.class);

    @PostConstruct
    void init() {
        if (!ylFlag) {
            logger.info("yl configure false");
            return;
        }
        if (StringUtils.isNotBlank(ylTidValue)) {
            // yl_tid_map=0001-00810001;0002-00810001
            Arrays.stream(ylTidValue.split(";")).forEach(s -> ylTidMap.put(s.substring(0, s.indexOf("-"))
                    , s.substring(s.indexOf("-") + 1)));
        }
    }

    String getAccessToken() {
        try {
            return HttpClientTpl.get(tokenUrl);
        } catch (Exception e) {
            throw new FwException("", e);
        }
    }


    String getTidBySessionId(String sessionId) {
        return this.ylTidMap.get(sessionId);
    }
}
