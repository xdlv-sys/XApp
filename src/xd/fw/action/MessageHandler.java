package xd.fw.action;

import java.util.HashMap;
import java.util.Map;

public class MessageHandler{
	static Map<String, IHandler> handlerMap = new HashMap<String, IHandler>();
	
	static{
		
	}
	
	public static IHandler getMessageHandler(String msgType){
		return handlerMap.get(msgType);
	}
}
