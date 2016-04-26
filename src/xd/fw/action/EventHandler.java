package xd.fw.action;


import xd.fw.FwUtil;

import java.util.HashMap;
import java.util.Map;

public class EventHandler{

	static Map<String, IHandler> handlerMap = new HashMap<String, IHandler>();
	
	static{
		handlerMap.put("subscribe", (IHandler) FwUtil.getBean("subscribeHandler"));
		handlerMap.put("unsubscribe",(IHandler)FwUtil.getBean("unSubscribeHandler"));

	}
	
	public static IHandler getEventHandler(String event){
		return handlerMap.get(event);
	}
	
}
