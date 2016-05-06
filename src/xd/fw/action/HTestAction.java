package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.HTest;
import xd.fw.service.HService;

public class HTestAction extends BaseAction{
    static int count = 0;
    @Autowired
    HService hService;
    public String saveHTest(){
        HTest hTest = new HTest();
        hTest.setId(count++);
        hTest.setName("name:" + hTest.getId());
        hService.saveHTest(hTest);

        return FINISH;
    }
}
