package xd.fw.mbean;

import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Service
public class TlvTest extends BaseMBeanImpl implements TlvTestMBean{
    String hostName;
    int port;

    @Override
    public String hello(String s) {
        return "hello " + s;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
