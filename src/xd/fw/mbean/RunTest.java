package xd.fw.mbean;

import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

@Service
public class RunTest extends BaseMBeanImpl implements RunTestMBean{
    String hostName;
    int port;

    @Override
    public String runCase(String className, String method, String params) throws Exception{
        Object obj = Class.forName(className).newInstance();
        Method method1 = obj.getClass().getDeclaredMethod(method,String.class);
        return String.valueOf(method1.invoke(obj,params));
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
