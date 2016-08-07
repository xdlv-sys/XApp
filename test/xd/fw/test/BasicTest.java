package xd.fw.test;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.service.IConst;

import java.util.Date;
import java.util.Iterator;

import static org.testng.Assert.fail;

public class BasicTest implements IConst{

    Logger logger = LoggerFactory.getLogger(getClass());

    String addUserUrl = "http://localhost:8080/an/syncUser.cmd";
    String addTrade = "http://localhost:8080/an/addTrade.cmd";

    String getUserUrl = "http://localhost:8080/an/jkn_user!obtainUsers.cmd?start=0&limit=20";


    protected JSONObject send(String url, String[][] params) throws Exception {
        return JSONObject.fromObject(HttpClientTpl.post(url, params));
    }
    protected  boolean sendF(String url, String[][] params) throws Exception {
        JSONObject ret = send(url, params);
        if (ret.getInt("code") == 200){
            return true;
        }
        return false;
    }

    protected boolean checkUser(UserChecker checker) throws Exception {
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

    void assertAddTrade(int tradeId, int userId,int tradeType, int totalFee) throws Exception {
        boolean add = sendF(addTrade, new String[][]{
                {"order.orderId", String.valueOf(tradeId)},
                {"order.userId", String.valueOf(userId)},
                {"order.payType", String.valueOf(1)},
                {"order.tradeType", String.valueOf(tradeType)},
                {"order.totalFee", String.valueOf(totalFee)},
                {"order.balanceFee", String.valueOf(0)},
                {"order.lastUpdateS", FwUtil.sdf.format(new Date())}
        });
        if (!add) {
            fail();
        }
    }

    void assertAddUser(int userId, int referrer) throws Exception {
        boolean add = sendF(addUserUrl, new String[][]{
                {"jknUser.userId", String.valueOf(userId)},
                {"jknUser.userName", "a"},
                {"jknUser.referrer", String.valueOf(referrer)},
                {"jknUser.telephone", "15951928975"}
        });
        if (!add) {
            fail();
        }
    }
}
