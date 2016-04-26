package xd.fw.job;

import org.springframework.stereotype.Controller;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.I18n;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.ImportDzRecord;
import xd.fw.bean.Userdz;
import xd.fw.service.MainSerivce;

import java.util.List;

@Controller
public class NotifyMessage  extends BaseJob{
    @Autowired
    MainSerivce mainSerivce;

    public void doExecute() throws Exception{
        List<ImportDzRecord> importDzRecordList = mainSerivce.getImportDzRecordForSend();
        if (importDzRecordList == null || importDzRecordList.size() < 1){
            logger.info("there is no notify message to send");
            return;
        }
        // every time we only process a record
        ImportDzRecord importDzRecord = importDzRecordList.get(0);
        List<Userdz> userdzs = mainSerivce.getAllUserdzs(0, 5000000);
        if (userdzs == null){
            logger.info("there is no company to send");
            return;
        }
        String url = I18n.getI18n("detail_url");
        String retString;
        //construct message body:
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser","");//placeHold
        jsonObject.put("template_id", I18n.getI18n("notify_msg_tpl"));
        jsonObject.put("url","");//placeHold
        jsonObject.put("data",JSONObject.fromObject(String.format("{\"year\" : {\"value\":\"%d\", \"color\":\"#173177\"}, \"month\": {\"value\":\"%d\"" +
                ", \"color\":\"#173177\"},\"day\": {\"value\":\"%d\", \"color\":\"#173177\"}}"
                ,importDzRecord.getYear(),importDzRecord.getMonth(),FwUtil.getLastDayInMonth(importDzRecord.getYear(),
                importDzRecord.getMonth()))));

        for (Userdz userdz : userdzs){
            if (StringUtils.isBlank(userdz.getWxid())){
                logger.error("no wxid in userdz record:" + userdz.getPhone());
                continue;
            }
            logger.debug("start to notify:" + userdz.getPhone());
            jsonObject.put("touser",userdz.getWxid());
            jsonObject.put("url",url + "?openid=" + userdz.getWxid());
            retString = HttpClientTpl.postJson(String.format("https://api.weixin.qq.com/cgi-bin/message/template/send" +
                    "?access_token=%s", getAccessToken()), jsonObject.toString());
            logger.debug("end with ret:" + retString);
        }
        // finally we must set the status of ImportRecord
        importDzRecord.setNotification("E");
        mainSerivce.updateImportDzRecordStatus(importDzRecord);
    }

    private String getAccessToken() throws Exception {
        return ((RefreshAccessToken)FwUtil.getBean("refreshAccessToken")).getAccessToken();
    }


}
