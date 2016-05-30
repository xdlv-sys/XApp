package xd.fw.action;

/**
 * Created by xd on 2016/4/27.
 */
public class AcceptAction extends BaseAction{

    String Carnumber;
    public String accept(){
        return SUCCESS;
    }

    public void setCarnumber(String carnumber) {
        Carnumber = carnumber;
    }
}
