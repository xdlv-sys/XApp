package xd.fw.action;

import xd.fw.I18n;

public class BaseHandler {
	static String TEXT_XML = I18n.getI18n("text_xml");

	protected String fillTextTpl(String content, ReceiveXmlEntity msg) {
		return String.format(TEXT_XML, msg.getFromUserName(), msg.getToUserName(),
				System.currentTimeMillis(), "text", content);
	}
}
