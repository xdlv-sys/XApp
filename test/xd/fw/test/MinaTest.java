package xd.fw.test;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import xd.fw.mina.tlv.ReversedProxy;
import xd.fw.mina.tlv.TLVMessage;

import java.net.InetSocketAddress;

public class MinaTest {
    @Test
    public void testProxy() throws Exception{
        int[] count = new int[]{0};
        ReversedProxy reversedProxy = new ReversedProxy() {
            @Override
            protected void constructRegistryMessage(TLVMessage registryMessage) {
                StringBuffer buffer = new StringBuffer();
                for (int i=0;i<400;i++){
                    buffer.append("INDEX:").append(i);
                }
                registryMessage.setNext(buffer.toString());
            }

            @Override
            protected InetSocketAddress inetSocketAddress() {
                return new InetSocketAddress("localhost", 18080);
            }

            @Override
            protected void handlerQuery(TLVMessage msg) throws Exception {
                assertEquals(msg.getValue(), "OK");
                count[0]++;
            }
        };

        int number = 20000;
        for (int i=0;i<number;i++){
            reversedProxy.doExecute();
        }
        Thread.sleep(10 * 1000);

        assertEquals(count[0], number);
    }
}
