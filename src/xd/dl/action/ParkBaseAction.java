package xd.dl.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import xd.dl.service.ParkService;
import xd.dl.service.PayService;
import xd.fw.action.BaseAction;

import java.text.SimpleDateFormat;
import java.util.Date;

@Results({
        @Result(name = "wx", type = "redirect", location = "${wxUrl}"),
        @Result(name = "index", location = "../../wwt/index.jsp")
})
public class ParkBaseAction extends BaseAction {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    final String INDEX = "index", PIC_KEY="PIC_KEY";
    final String WX = "wx";

    @Autowired
    PayService payService;
    @Autowired
    ParkService parkService;
    final static SimpleDateFormat sdf = new SimpleDateFormat("HHmmssyyyyMMddSSS");

    @Autowired
    ApplicationContext applicationContext;

    static synchronized String createOutTradeNo(){
        return String.valueOf(sdf.format(new Date()));
    }
    public static void main(String[] args){
        System.out.println(createOutTradeNo());
    }
}
