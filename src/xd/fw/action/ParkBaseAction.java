package xd.fw.action;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.mbean.SystemConfig;
import xd.fw.service.ParkService;
import xd.fw.service.PayService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Results({
        @Result(name = "weixin", type = "redirect", location = "${weixinUrl}"),
        @Result(name = "alipay", type = "redirect", location = "${alipayUrl}"),
        @Result(name = "index", location = "../../wwt/index.jsp"),
        @Result(name = "payapi", location = "../../wwt/alipayapi.jsp"),
        @Result(name = "pay", location = "../../wwt/pay.jsp")
})
public class ParkBaseAction extends BaseAction {
    protected Logger logger = Logger.getLogger(getClass());
    final String RET_KEY = "RET_FOR_TOUCH" ,INDEX = "index", PAY = "pay";

    @Autowired
    PayService payService;
    @Autowired
    ParkService parkService;
    final static SimpleDateFormat sdf = new SimpleDateFormat("HHmmssyyyyMMddSSS");

    protected void setRetAttribute(String key, String value) {
        HttpServletRequest request = ServletActionContext.getRequest();
        JSONObject jsonObject = (JSONObject) request.getAttribute(RET_KEY);
        if (jsonObject == null) {
            jsonObject = new JSONObject();
            request.setAttribute(RET_KEY, jsonObject);
        }
        jsonObject.put(key, value);
    }
    protected void setRequestAttribute(String key, Object value){
        ServletActionContext.getRequest().setAttribute(key,value);
    }

    protected void returnXml(String xml) {
        setRequestAttribute("xml", xml);
    }

    static synchronized String createOutTradeNo(){
        return String.valueOf(sdf.format(new Date()));
    }
    public static void main(String[] args){
        System.out.println(createOutTradeNo());
    }
}
