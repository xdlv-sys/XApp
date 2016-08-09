package xd.fw.job;

import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendSmsJob extends EventJob {
    @Value("${sms_template}")
    String smsTemplate;
    @Value("${serious}")
    String serious;
    @Value("${normal}")
    String normal;

    @Override
    protected byte process(JknEvent event) throws Exception {
        String url = "http://js.ums86.com:8899/sms/Api/Send.do";

        String[][] params = {
                {"SpCode", "235423"},
                {"LoginName", "nj_zjjkn"},
                {"Password","zjjkn_5177"},
                {"MessageContent",String.format(smsTemplate,event.getDbKey(), event.getDbInt() == SERIOUS ? serious : normal)},
                {"UserNumber", JKN.sms_telephones},
                {"SerialNumber",String.valueOf(System.currentTimeMillis())},
                {"f","1"}
        };
        String charset = "gb2312";
        String ret = (String)HttpClientTpl.execute(url,true,params,null, entity -> EntityUtils.toString(entity,charset),charset);
        logger.info(ret);
        return ES_DONE;
    }

    @Override
    protected byte[] processType() {
        return new byte[]{EV_SMS_SEND};
    }
}
