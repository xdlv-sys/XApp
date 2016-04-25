package xd.fw.action;

import com.opensymphony.xwork2.ActionSupport;
import xd.fw.FwException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;


public class ResultAction extends BaseAction{

    static Logger logger = Logger.getLogger(ResultAction.class);
    String msg = "操作成功";
    boolean success = true;

    public String result(){
        HttpServletRequest request = ServletActionContext.getRequest();
        Throwable exception = (Throwable) request.getAttribute("exception");
        Throwable temp = exception;
        int deep = 10;
        while (temp != null && temp.getCause() != null && deep-- > 0){
            temp = temp.getCause();
        }
        if (temp != null){
            msg = temp.getMessage();
            success = false;
            if (!(exception instanceof FwException)){
                logger.warn("business exception", exception);
            } else {
                logger.info("bussiness:" + exception.getMessage());
            }
        } else {
            String msgSpecialized = (String)request.getAttribute("msg");
            if (msgSpecialized != null){
                msg = msgSpecialized;
            }
        }
        return SUCCESS;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
