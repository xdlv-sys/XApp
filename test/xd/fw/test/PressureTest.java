package xd.fw.test;


import org.testng.annotations.Test;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class PressureTest extends BasicTest {
    String testUrl = "http://localhost:8080/an/testTransactional.cmd";

    //@Test
    public void testTransactional() throws Exception{
        for (int i=0;i<400;i++){
            new Thread(() -> {
                for (int j =0;j<10;j++){
                    try {
                        assertTrue(sendF(testUrl, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        System.in.read();
    }

    @Test
    public void testTransactional2() throws Exception{
        int thread = 100;
        int each = 100;
        int totalFee = 10000;
        int start = 30000;
        Thread[] threads = new Thread[thread];

        for (int i=0;i<thread;i++){
            final int count = i * each + start;
            Thread tmpThread = new Thread(() -> {
                for (int j = 0; j < each; j++) {
                    try {
                        logger.info("start:" + (count + j));
                        assertAddTrade(assertAddUser(3), TR_TYPE_CONSUME, totalFee, 0, 0, -1);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            });
            threads[i] = tmpThread;
            tmpThread.start();
        }

        long startTIme = System.currentTimeMillis();
        for (Thread t : threads){
            t.join();
        }
        logger.info("consumed:" + (System.currentTimeMillis() -startTIme));

        sleep(3 * 60 * 1000);

        checkUser(1, (j)->j.getInt("count") == thread * each * totalFee * 0.11);
        checkUser(2,(j)->j.getInt("count") == thread * each * totalFee * 0.09);
        checkUser(3, (j)-> j.getInt("count") == thread * each * totalFee * 0.07);
    }
}
