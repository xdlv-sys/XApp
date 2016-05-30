package xd.fw.job;

/**
 * Created by xd on 2016/4/23.
 */
public class DeleteJob extends EnterJob{
    @Override
    public void doExecute() throws Exception {
        logger.info("execute delete job");
        int rows = parkService.deleteFinishRecord();
        logger.info("delete " + rows);
    }
}
