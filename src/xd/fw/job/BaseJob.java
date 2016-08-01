package xd.fw.job;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import xd.fw.service.IConst;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public abstract class BaseJob implements IConst{

    protected Logger logger = Logger.getLogger(getClass());

    private boolean destroyed = false;

    protected void destroy(){
        destroyed = true;
    }

    public final void execute() throws Exception{
        if (!destroyed){
            doExecute();
        }
    }

    public abstract void doExecute() throws Exception;
}
