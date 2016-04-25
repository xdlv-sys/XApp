package xd.fw.job;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseJob {

    protected Logger logger = Logger.getLogger(getClass());

    protected void init(){}
    protected void destroy(){}

    public final void execute() throws Exception{
        init();
        doExecute();
    }

    public abstract void doExecute() throws Exception;
}
