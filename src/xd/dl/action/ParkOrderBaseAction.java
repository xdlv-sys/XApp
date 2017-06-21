package xd.dl.action;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.action.BaseAction;

import javax.annotation.PostConstruct;

/**
 * Created by xd on 6/21/2017.
 * base class for pay order action
 */
public class ParkOrderBaseAction extends BaseAction{
    @Value("${success}")
    String success;
    @Value("${fail}")
    String fail;
    @Value("${park_no}")
    String parkNo;

    String state, msg;

    JSONObject result;
    @PostConstruct
    public void init(){
        state = "200";
        msg = success;
    }

    public String getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject getResult() {
        return result;
    }

    void put(Object k, Object v){
        if (result == null){
            result = new JSONObject();
        }
        result.put(k, v);
    }
}
