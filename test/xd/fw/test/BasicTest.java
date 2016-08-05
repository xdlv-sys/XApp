package xd.fw.test;

import net.sf.json.JSONObject;
import xd.fw.HttpClientTpl;
import xd.fw.service.IConst;

import java.util.Iterator;

public class BasicTest implements IConst{

    protected JSONObject send(String url, String[][] params) throws Exception {
        return JSONObject.fromObject(HttpClientTpl.post(url, params));
    }
    protected  boolean sendF(String url, String[][] params) throws Exception {
        return send(url, params).getInt("code") == 200;
    }

    protected boolean checkUser(String getUserUrl, UserChecker checker) throws Exception {
        Iterator<JSONObject> iterator = send(getUserUrl, null).getJSONArray("jknUsers").iterator();
        boolean success = false;
        while (iterator.hasNext()) {
            if (checker.process(iterator.next())) {
                success = true;
                break;
            }
        }
        return success;
    }
    interface UserChecker{
        boolean process(JSONObject i);
    }
}
