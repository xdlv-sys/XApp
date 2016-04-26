package xd.fw.action.handler;

import org.apache.log4j.Logger;
import xd.fw.action.BaseHandler;
import xd.fw.action.IHandler;
import xd.fw.action.ReceiveXmlEntity;
import xd.fw.service.MainSerivce;


public class UnSubscribeHandler extends BaseHandler implements IHandler {

	Logger logger = Logger.getLogger(UnSubscribeHandler.class);
	MainSerivce mainSerivce;
	@Override
	public String process(ReceiveXmlEntity msg) {
		logger.info("unsubscript: " + msg.getFromUserName());
		mainSerivce.deleteUserdzByOpenId(msg.getFromUserName());
		return "";
	}

	public void setMainSerivce(MainSerivce mainSerivce) {
		this.mainSerivce = mainSerivce;
	}
}
