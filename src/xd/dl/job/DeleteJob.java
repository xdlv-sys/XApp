package xd.dl.job;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.job.BaseJob;
import xd.dl.service.ParkService;


public class DeleteJob extends BaseJob {
    @Autowired
    ParkService parkService;
    @Override
    public void doExecute() throws Exception {
        logger.info("execute delete job");
        int rows = parkService.deleteFinishRecord();
        logger.info("delete " + rows);
    }
}
