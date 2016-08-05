package xd.fw.test;


import net.sf.json.JSONObject;
import org.testng.annotations.Test;
import xd.fw.FwUtil;

import java.util.Date;
import java.util.Iterator;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;


public class UserTest extends BasicTest {
    String addUserUrl = "http://localhost:8080/an/syncUser.cmd";
    String addTrade = "http://localhost:8080/an/addTrade.cmd";

    String getUserUrl = "http://localhost:8080/an/jkn_user!obtainUsers.cmd?start=0&limit=20";
    int userId = 10000;

    @Test
    public void userRegistry() throws Exception {

        boolean add = sendF(addUserUrl, new String[][]{
                {"jknUser.userId", String.valueOf(userId)},
                {"jknUser.userName", "a"},
                {"jknUser.referrer", "3"},
                {"jknUser.telephone", "15951928975"}
        });
        if (!add) {
            fail();
        }
        //check user
        assertTrue(checkUser(getUserUrl,(j)-> j.getInt("userId") == userId));
    }

    @Test(dependsOnMethods = "userRegistry")
    public void userUpgrade() throws Exception {
        int tradeId = 10001;
        int totalFeeOne = 5900, totalFeeTwo = 60000;
        int totalFee = totalFeeOne + totalFeeTwo;

        assertAddTrade(tradeId,userId,TR_TYPE_CONSUME,totalFeeOne);

        sleep(10 * 1000);
        //check user
        assertTrue(checkUser(getUserUrl,(j)-> j.getInt("userId") == userId
                && j.getInt("userLevel") == UL_NORMAL));

        //check parent is gold
        assertTrue(checkUser(getUserUrl,(j)-> j.getInt("userId") == 2
                && j.getInt("userLevel") == UL_GOLD));

        //vip
        assertAddTrade(tradeId + 1,userId, TR_TYPE_CONSUME,totalFeeTwo);
        sleep(15 * 1000);
        //check user
        assertTrue(checkUser(getUserUrl,(j)-> j.getInt("userId") == userId
                && j.getInt("vip") == VIP));

        //check settlement
        assertTrue(checkUser(getUserUrl,(j)-> (j.getInt("userId") == 1
                && j.getInt("count") == (int)(totalFee * 0.11)) ));
        assertTrue(checkUser(getUserUrl,(j)-> (j.getInt("userId") == 2
                && j.getInt("count") == (int)(totalFee * 0.09)) ));
        assertTrue(checkUser(getUserUrl,(j)-> (j.getInt("userId") == 3
                && j.getInt("count") == (int)(totalFee * 0.07)) ));
    }

    @Test
    public void userUpgrade2() throws Exception {

        int userId = 20000;
        int tradeId = 20000;

        // referrer = default
        for (int i=0;i<10; i++){
            userId++;
            boolean add = sendF(addUserUrl, new String[][]{
                    {"jknUser.userId", String.valueOf(userId)},
                    {"jknUser.userName", "a"},
                    {"jknUser.referrer", "3"},
                    {"jknUser.telephone", "15951928975"}
            });
            if (!add) {
                fail();
            }
            //became to membership
            assertAddTrade(tradeId++,userId,TR_TYPE_CONSUME,5900);

            sleep(5 * 1000);
        }
        // check user
        checkUser(getUserUrl,(j)-> j.getInt("userId") == 3
                && j.getInt("userLevel") == UL_WHITE);

        for (int i=0;i<10; i++){

            boolean add = sendF(addUserUrl, new String[][]{
                    {"jknUser.userId", String.valueOf(userId++)},
                    {"jknUser.userName", "a"},
                    {"jknUser.referrer", String.valueOf(20001)},
                    {"jknUser.telephone", "15951928975"}
            });
            if (!add) {
                fail();
            }
            //became to membership
            assertAddTrade(tradeId,userId,TR_TYPE_CONSUME,5900);

            sleep(2 * 1000);
        }
        // check user
        checkUser(getUserUrl,(j)-> j.getInt("userId") == 3
                && j.getInt("userLevel") == UL_DIAMOND);
    }

    private void assertAddTrade(int tradeId, int userId,int tradeType, int totalFee) throws Exception {
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
}
