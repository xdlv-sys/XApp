package xd.fw.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xd.fw.mina.tlv.ReversedProxy;
import xd.fw.mina.tlv.TLVMessage;

import java.net.InetSocketAddress;

/**
 * Created by xd on 2017/7/23.
 */
public class CarEnter {
    ReversedProxy reversedProxy;
    @BeforeClass
    public void init() throws Exception {
        reversedProxy = new ReversedProxy() {
            @Override
            protected void constructRegistryMessage(TLVMessage registryMessage) {
            }

            @Override
            protected InetSocketAddress inetSocketAddress() {
                return new InetSocketAddress("localhost", 48011);
            }

            @Override
            protected void handlerQuery(TLVMessage msg) throws Exception {
            }
        };
        reversedProxy.heartBeat();
    }
    @AfterClass
    public void destroy(){
        reversedProxy.destroy();
    }
    @Test
    public void carEnter(){
        TLVMessage message = new TLVMessage(10);
        message.setNext("HA12341").setNext("20170723144101");
        reversedProxy.response(message);
    }

    @Test
    public void carOut(){
        TLVMessage message = new TLVMessage(11);
        message.setNext("HA12341").setNext("").setNext("").setNext("20170723144101").setNext("20170723144102");
        reversedProxy.response(message);
    }

}
