package xd.fw.job;

import org.springframework.stereotype.Controller;

@Controller
public class MemoryJob extends BaseJob {

    @Override
    public void doExecute() throws Exception {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        logger.info(new StringBuffer("memory usage(free/total):"
        ).append(free / 1024 / 1024).append("M/").append(total / 1024 / 1024).append("M"));
    }
}
