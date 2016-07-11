package xd.fw.action;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.HTest;
import xd.fw.service.HService;

public class HTestAction extends BaseAction{
    static int count = 0;
    @Autowired
    HService hService;
    @Action("saveHTest")
    public String saveHTest(){
        HTest hTest = new HTest();
        hTest.setId(count++);
        hTest.setName("name:" + hTest.getId());
        hService.saveHTest(hTest);

        return FINISH;
    }

    @Action("saveOrUpdate")
    public String saveOrUpdate(){
        HTest hTest = hService.get(HTest.class, 1);
        hTest.setName("Modify");
        hService.saveOrUpdate(hTest);

        hTest = new HTest();
        hTest.setId(count++);
        hTest.setName("name:" + hTest.getId());
        hService.saveOrUpdate(hTest);

        return FINISH;
    }

    @Action("updateHTest")
    public String updateHTest(){
        HTest hTest = hService.get(HTest.class, 1);
        hTest.setName("Modify");
        hService.update(hTest);
        return FINISH;
    }
}
