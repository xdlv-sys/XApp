package xd.fw.test;


import org.testng.annotations.Test;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;


public class UserTest extends BasicTest {

    @Test
    public void userRegistry() throws Exception {
        int userId = 100;
        assertAddUser(userId,3);
        //check user
        assertTrue(checkUser((j)-> j.getInt("userId") == userId));
    }

    @Test
    public void userUpgrade() throws Exception {
        int userId = 200001;
        int tradeId = 100001;
        int totalFeeOne = 5900, totalFeeTwo = 60000;
        int totalFee = totalFeeOne + totalFeeTwo;
        assertAddUser(userId,3);
        assertAddTrade(tradeId,userId,TR_TYPE_CONSUME,totalFeeOne);

        sleep(5 * 1000);
        //check user
        assertTrue(checkUser((j)-> j.getInt("userId") == userId
                && j.getInt("userLevel") == UL_NORMAL));

        //check parent is gold
        assertTrue(checkUser((j)-> j.getInt("userId") == 2
                && j.getInt("userLevel") == UL_GOLD));

        //vip
        assertAddTrade(tradeId + 1,userId, TR_TYPE_CONSUME,totalFeeTwo);
        sleep(5 * 1000);
        //check user
        assertTrue(checkUser((j)-> j.getInt("userId") == userId
                && j.getInt("vip") == VIP));

        // 用户 3取现
        //check cash 取现
        int cash = 200;
        assertAddTrade(tradeId + 2, 3,TR_TYPE_MONEY, cash);
        sleep(5 * 1000);

        //check settlement
        assertTrue(checkUser((j)-> (j.getInt("userId") == 1
                && j.getInt("count") == (int)(totalFee * 0.11)) ));
        assertTrue(checkUser((j)-> (j.getInt("userId") == 2
                && j.getInt("count") == (int)(totalFee * 0.09)) ));
        assertTrue(checkUser((j)-> (j.getInt("userId") == 3
                && j.getInt("count") == (int)(totalFee * 0.07 - cash)) ));

    }

    @Test(dependsOnMethods = "userUpgrade")
    public void userUpgrade2() throws Exception {
        /**
         * 首先创建根用户 300，然后在下面直推10人，302，
         * 再加312下加10个人 达到钻石标准
         */
        final int userId = 300;
        final int tradeId = 200;

        //add root user
        assertAddUser(userId, 3);
        assertAddTrade(tradeId,userId,0,5900);
        sleep(2 * 1000);

        // referrer = default
        for (int i=1;i<=10; i++){
            assertAddUser(userId + i, userId);
            //became to membership
            assertAddTrade(tradeId + i,userId + i,TR_TYPE_CONSUME,5900);
            sleep(2 * 1000);
        }
        // check user for white
        checkUser((j)-> j.getInt("userId") == userId
                && j.getInt("userLevel") == UL_WHITE);

        //
        for (int i=11;i<=20; i++){
            assertAddUser(userId + i, userId + 2);
            //became to membership
            assertAddTrade(tradeId + i,userId + i,TR_TYPE_CONSUME,5900);
            sleep(2 * 1000);
        }

        for (int i=21;i<=30; i++){
            assertAddUser(userId + i, userId + 10 +2);
            //became to membership
            assertAddTrade(tradeId + i,userId + i,TR_TYPE_CONSUME,5900);
            sleep(2 * 1000);
        }
        // check user
        checkUser((j)-> j.getInt("userId") == 3
                && j.getInt("userLevel") == UL_DIAMOND);
    }


}
