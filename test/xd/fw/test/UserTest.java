package xd.fw.test;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xd.fw.JKN;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;


public class UserTest extends BasicTest {

    int rootUserId;
    int secondaryUserId;
    int thirdUserId;

    @BeforeClass
    public void init() throws Exception{
        //create three level users
        rootUserId = assertAddUser(3);
        secondaryUserId = assertAddUser(rootUserId);
        thirdUserId = assertAddUser(secondaryUserId);

        assertAddTrade(thirdUserId,TR_TYPE_CONSUME,5900, 0);
    }

    @Test
    public void userUpgrade() throws Exception {
        int totalFeeOne = 5900, totalFeeTwo = 60000;
        int totalFee = totalFeeOne + totalFeeTwo;

        final int userId = assertAddUser(thirdUserId);
        assertAddTrade(userId,TR_TYPE_CONSUME,totalFeeOne, 0);

        sleep(2 * 1000);
        //check user
        checkUser(userId,(j)->j.getInt("userLevel") == UL_NORMAL);

        //check parent is gold
        checkUser(thirdUserId,(j)-> j.getInt("userLevel") == UL_GOLD);

        //vip
        assertAddTrade(userId, TR_TYPE_CONSUME,totalFeeTwo, 0);
        sleep(2 * 1000);
        //check user
        checkUser(userId,(j)-> j.getInt("vip") == VIP);

        // 用户 thirdUser取现,当前用户的金额为 (5900 + 59000) * 0.07
        //check cash 取现
        int cash = 200;
        assertAddTrade( thirdUserId,TR_TYPE_MONEY, cash, 0);
        sleep(5 * 1000);

        //check settlement
        checkUser(rootUserId,(j)-> j.getInt("count") == 0);
        checkUser(secondaryUserId,(j)-> j.getInt("count") == 0);
        checkUser(thirdUserId, (j)-> j.getInt("count") == (int)(totalFee * 0.07 - cash));

    }

    @Test(dependsOnMethods = "userUpgrade")
    public void userUpgrade2() throws Exception {
        /**
         * 首先创建根用户 300，然后在下面直推10人，302 下再推10人，
         * 再加312下加10个人 达到钻石标准
         */

        //add root user
        int userId = assertAddUser(thirdUserId);
        assertAddTrade(userId,0,5900, 0);
        sleep(2 * 1000);

        for (int i=1;i<=10; i++){
            int childId = assertAddUser(userId);
            //became to membership
            assertAddTrade(childId,TR_TYPE_CONSUME,5900, 0);
        }
        sleep(2 * 1000);
        // check user for white
        checkUser(userId, (j)-> j.getInt("userLevel") == UL_WHITE);

        //
        for (int i=11;i<=20; i++){
            int childId = assertAddUser(userId + 2);
            //became to membership
            assertAddTrade(childId,TR_TYPE_CONSUME,5900, 0);
        }

        for (int i=21;i<=30; i++){
            int childId = assertAddUser(userId + 10 +2);
            assertAddTrade(childId,TR_TYPE_CONSUME,5900, 0);
        }
        sleep(10 * 1000);
        // check user
        checkUser(userId, (j)-> j.getInt("userLevel") == UL_DIAMOND
        && j.getInt("count") == 10 * 5900 * 0.07 + 10 * 5900 * 0.09 + 5900 * 0.11);
    }

    @Test(dependsOnMethods = "userUpgrade2")
    public void userUpgrade3() throws Exception {

        /**
         * 1->2->3 , 3升级成会员后，2，3不成为黄金会员
         */
        final int userId = assertAddUser(thirdUserId);

        // create 10 customer
        for (int i=1;i<=10; i++){
            assertAddUser(userId);
        }

        int tmpUserId = assertAddUser(userId + 5);
        assertAddTrade(tmpUserId,TR_TYPE_CONSUME,5900, 0);

        sleep(5 * 1000);

        checkUser(tmpUserId, (j)-> j.getInt("userLevel") == UL_NORMAL);
        checkUser(userId + 5, (j)-> j.getInt("userLevel") == UL_NON);
        checkUser(userId, (j)-> j.getInt("userLevel") == UL_NON);
    }

    @Test(dependsOnMethods = "userUpgrade3")
    public void userAreaUpgrade() throws Exception {
        final int userId = assertAddUser(thirdUserId);
        assertAddTrade(userId,TR_TYPE_CONSUME,5900, 0);

        int regionOne = createRegionUser(userId);
        sleep(2000);
        //用户成为区代
        checkUser(regionOne, (j)-> j.getInt("userLevel") == UL_DIAMOND
                && j.getInt("areaLevel") == AL_REGION);

        int regionTwo = createRegionUser(userId);
        sleep(2000);
        checkUser(regionTwo, (j)-> j.getInt("userLevel") == UL_DIAMOND
                && j.getInt("areaLevel") == AL_REGION);

        int regionThree = createRegionUser(userId);
        sleep(2000);
        checkUser(regionThree, (j)-> j.getInt("userLevel") == UL_DIAMOND
                && j.getInt("areaLevel") == AL_REGION);

        //三个区代完成，验证市代，还要直推（35 -3) 32个，消费总额达到24w(60 - 12 * 3),每个用户消费7500
        for (int i=1;i<=32;i++){
            assertAddTrade(assertAddUser(userId),TR_TYPE_CONSUME,750000, 0);
        }
        sleep(5 * 1000);
        checkUser(userId, (j)-> j.getInt("userLevel") == UL_DIAMOND
                && j.getInt("areaLevel") == AL_CITY);

        //现在验证区代收益 以regionThree为标准，regionThree + 1 为该区代第一个子后代
        int[] counts = new int[2];
        checkUser(userId, (j)->{
            counts[0] = j.getInt("count");
            return true;
        });

        checkUser(regionThree, (j)->{
            counts[1] = j.getInt("count");
            return true;
        });

        assertAddTrade(regionThree + 1, TR_TYPE_CONSUME, 100, 0);
        sleep(5000);
        checkUser(userId, (j)->j.getInt("count") == (counts[0] + 100 * (0.09 + 0.04)));

        checkUser(regionThree, (j)->j.getInt("count") == (counts[1] + 100 * (0.07 + 0.03)));
    }

    @Test(dependsOnMethods = "userAreaUpgrade")
    public void userStoreUpgrade() throws Exception {
        /*
         *  创建一个根用户，在其下创建一个区代
         */
        final int userId = assertAddUser(thirdUserId);
        assertAddTrade(userId,TR_TYPE_CONSUME,5900, 0);
        sleep(1000);

        int regionOne = createRegionUser(userId);
        // approve store keeper
        assertApproveStore(regionOne, STORE_KEEPER_FIRST);

        sleep(2000);
        int[] counts = new int[]{0};
        checkUser(regionOne, (j)-> {
            counts[0] = j.getInt("count");
            return j.getInt("storeKeeper") == STORE_KEEPER_FIRST;
        });

        assertAddTrade(assertAddUser(userId),TR_TYPE_CONSUME,5900, regionOne);
        sleep(2000);

        checkUser(regionOne,(j)->j.getInt("count") == counts[0] + 5900 * JKN.store_order_first_settlement );

    }

    @Test(dependsOnMethods = "userStoreUpgrade")
    public void withdrawCount() throws Exception {
        /**
         * 创建用户 升级成VIP 验证返现
         */
    }


    private int createRegionUser(int referrer) throws Exception{
        final int userId = assertAddUser(referrer);
        assertAddTrade(userId,TR_TYPE_CONSUME,5900, 0);

        //create 20 child user and every child has 9 children
        for (int i=1;i<=20;i++){
            int childId = assertAddUser(userId);
            //一级用户（20人），每人消费1500 共3w
            assertAddTrade(childId,TR_TYPE_CONSUME,150000, 0);
            //二级级用户（180人），每人消费500 共9w
            for (int j =1 ; j<=9;j++){
                int grandsonId = assertAddUser(childId);
                assertAddTrade(grandsonId,TR_TYPE_CONSUME,50000, 0);
            }
        }
        return userId;
    }

}
