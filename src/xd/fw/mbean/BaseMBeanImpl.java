package xd.fw.mbean;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.management.*;
import java.lang.management.ManagementFactory;

@Service
public abstract class BaseMBeanImpl {
    protected Logger logger = Logger.getLogger(getClass());

    public BaseMBeanImpl() {
        try {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this
                    , new ObjectName("xapp:name=" + getName()));
        } catch (Exception e) {
            logger.error("fail to registry mbean:" + getName(),e);
        }
    }

    public String getName(){
        return getClass().getSimpleName();
    }
}
