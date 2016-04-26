package xd.fw.action.handler;

import xd.fw.I18n;
import xd.fw.action.BaseHandler;
import xd.fw.action.IHandler;
import xd.fw.action.ReceiveXmlEntity;

public class SubscribeHandler extends BaseHandler implements IHandler {

	static String welcome = I18n.getI18n("welcome_msg");
	@Override
	public String process(ReceiveXmlEntity msg) {
		return fillTextTpl(welcome,msg);
	}

}
