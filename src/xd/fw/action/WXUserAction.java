package xd.fw.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xd.fw.FwException;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.I18n;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import xd.fw.bean.*;
import xd.fw.service.MainSerivce;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Results({
        @Result(name = "index", location = "../../wwt/index.jsp"),
})
@Controller
public class WXUserAction extends BaseAction {

    static Logger logger = Logger.getLogger(WXUserAction.class);
    public static final String RET_KEY = "RET_FOR_TOUCH";
    final String INDEX = "index";

    String phone;
    String validateCode;

    String code;
    String openid;
    String state;

    Dzlist dzlist;
    List<Dzlist> dzlists;

    @Autowired
    MainSerivce mainSerivce;
    final static String appID = I18n.getI18n("appID");
    final static String appsecret = I18n.getI18n("appsecret");

    public String userBindIndex() throws Exception {
        Userdz userdz = getUserDzFromWeixing();
        setRetAttribute("openId",openid);
        //check if is used already bind.
        if (userdz != null) {
            setRetAttribute("phone", userdz.getPhone());
            setRetAttribute("viewId", "Main");
        } else {
            setRetAttribute("viewId", "License");
        }
        return INDEX;
    }
    public String currentDzIndex()throws Exception{
        return checkBindAndView("CurrentDz");
    }

    public String historyQueryIndex()throws Exception{
        return checkBindAndView("HistoryQuery");
    }

    private String checkBindAndView(String viewId) throws Exception{
        Userdz userdz = getUserDzFromWeixing();
        setRetAttribute("openId",openid);
        if (userdz == null){
            setRetAttribute("viewId", "License");
        } else {
            setRetAttribute("viewId", viewId);
        }
        return INDEX;
    }
    private Userdz getUserDzFromWeixing()throws Exception{
        if (openid == null){
            // wei xing
            String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s" +
                    "&secret=%s&code=%s&grant_type=authorization_code", appID,appsecret,code);
            String retJson = HttpClientTpl.get(url);
            logger.info("ret openid :" + retJson);
            JSONObject json = JSONObject.fromObject(retJson);
            openid = (String) json.get("openid");
            if (openid == null) {
                throw new FwException("抱歉，网络出现问题，无法获取用户信息:openid，请重试。");
            }
        }
        return mainSerivce.getUserdzByOpenid(openid);
    }
    private void setRetAttribute(String key, String value){
        HttpServletRequest request = ServletActionContext.getRequest();
        JSONObject jsonObject = (JSONObject)request.getAttribute(RET_KEY);
        if (jsonObject == null){
            jsonObject = new JSONObject();
            request.setAttribute(RET_KEY, jsonObject);
        }
        jsonObject.put(key, value);
    }
	public String userBind(){
        UserValidate userValidate = mainSerivce.getValidateCodeByPhone(phone, validateCode);
        if (userValidate == null){
            throw new FwException("验证码无效，请重新获取");
        }
        Userdz userdz = new Userdz(phone, openid);
        mainSerivce.saveUserDz(userdz);
		return FINISH;
	}
	
	public String sendValidateCode(){
		//首先要判断是否为合法用户
		UserCompany[] userCompanys = mainSerivce.getUserCompanyByPhone(phone);
		if (userCompanys == null || userCompanys.length < 1){
			throw new FwException("不是合法客商用户");
		}
        if (mainSerivce.getUserdzByPhone(phone) != null){
            throw new FwException("该用户手机号己经绑定");
        }
        UserValidate userValidate = new UserValidate(phone, FwUtil.getValidateCode());
        mainSerivce.saveUserValidate(userValidate);
        return FINISH;
    }

    public String loadDzRecord() {
        int year, month;
        if (dzlist == null || dzlist.getYear() == 0 || dzlist.getMonth() == 0) {
            //get obtain dz record
            ImportDzRecord importDzRecord = mainSerivce.getLastedNotifyImportDzRecord();
            if (importDzRecord == null || "N".equals(importDzRecord.getNotification())) {
                throw new FwException("当前没有账单可供查询");
            }
            year = importDzRecord.getYear();
            month = importDzRecord.getMonth();
            logger.info("load dzlist from current:" + year + "-" + month);
        } else {
            //apply parameters from user
            year = dzlist.getYear();
            month = dzlist.getMonth();
            logger.info("load dzlist from history:" + year + "-" + month);
        }
        dzlists = mainSerivce.getDzlist(openid, year, month);
        return SUCCESS;
    }

    public String confirmDz() {
        mainSerivce.upateDzlistStatus(dzlist);
        return FINISH;
    }

    public void setMainSerivce(MainSerivce mainSerivce) {
        this.mainSerivce = mainSerivce;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Dzlist getDzlist() {
        return dzlist;
    }

    public void setDzlist(Dzlist dzlist) {
        this.dzlist = dzlist;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDzlists(List<Dzlist> dzlists) {
        this.dzlists = dzlists;
    }

    public List<Dzlist> getDzlists() {
        return dzlists;
    }
}
