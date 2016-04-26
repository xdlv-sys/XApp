package xd.fw.job;

import org.springframework.stereotype.Controller;
import xd.fw.I18n;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.UserValidate;
import xd.fw.service.MainSerivce;
@Controller
public class SendSmsMessage extends BaseJob{
	@Autowired
	MainSerivce mainSerivce;
	Logger logger = Logger.getLogger(SendSmsMessage.class);
	static String smsTpl = I18n.getI18n("sms_tpl");

	public void doExecute(){
        int count = mainSerivce.deleteUserValidateForUnuse();
        if (count > 0){
            logger.debug("delete " + count + " items from uservalidate.");
        }
        UserValidate[] userValidates = mainSerivce.getUserValidateForNotSend();
		if (userValidates == null || userValidates.length < 1){
			logger.info("there is no validate sms to send.");
			return;
		}

		String status = null;
		for (UserValidate userValidate : userValidates){
			if (StringUtils.isEmpty(userValidate.getPhone())){
				logger.error("phone number is empty.");
				continue;
			}
			if (StringUtils.isEmpty(userValidate.getValidateCode())){
				logger.error("validate code is empty.");
				continue;
			}
			try {
				logger.info("start to send sms :" + userValidate.getPhone());
				String ret = HttpDemo.sendTextSms2(userValidate.getPhone()
						, String.format("@1@=%s", userValidate.getValidateCode()));
				if (ret == null || ret.indexOf("<status>0</status>") == -1){
					logger.error("return sms error:" + ret);
				} else {
					status = UserValidate.SENDED;
				}
			} catch (Exception e) {
				logger.error("send sms error", e);
				status = UserValidate.ERROR;
			} finally {
				mainSerivce.updateUserValidateStatus(userValidate.getPhone(),status);
			}
		}
	}

	public void notifyMessage(){

	}
}
