package xd.fw.action;


import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Controller;

@Controller()
public class WeiXingAction extends BaseAction {

	private String echostr;
	private String nonce;
	private String timestamp;
    private String signature;

	public String entry() {
		if (StringUtils.isNotBlank(echostr)) {
			log.info("entry:" + echostr);
			returnXml(echostr);
			return XML;
		}
		ReceiveXmlEntity msg = getMsgEntity();
		log.info("process:" + msg);
		
		String result;

		if (StringUtils.isNotBlank(msg.getEvent())) {
			result = processEvent(msg);
		} else {
			//common message process
			result = proceeMessage(msg);
		}
		
		returnXml(result);
		return XML;
	}

	private String proceeMessage(ReceiveXmlEntity msg) {
		IHandler handler = MessageHandler.getMessageHandler(msg.getMsgType());
		if (handler != null){
			return handler.process(msg);
		}
		log.info("miss message handler :" + msg.getCreateTime());
		return null;
	}

	private String processEvent(ReceiveXmlEntity msg) {
		IHandler handler = EventHandler.getEventHandler(msg.getEvent());
		if (handler != null) {
			return handler.process(msg);
		}
		log.info("miss event handler :" + msg.getCreateTime());
		return null;
	}

	private void returnXml(String xml) {
		ServletActionContext.getRequest().setAttribute("xml", xml);
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	public String getEchostr() {
		return echostr;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
