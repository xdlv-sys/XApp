package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xd.fw.FwException;
import xd.fw.bean.ImportDzRecord;
import xd.fw.service.MainSerivce;

import java.util.Calendar;
import java.util.List;
@Controller
public class ImportDzRecordAction extends BaseAction{

    List<ImportDzRecord> importDzRecords;
    ImportDzRecord importDzRecord;
    @Autowired
    MainSerivce mainSerivce;

    public String obtainImportDzRecord(){
        importDzRecords = mainSerivce.getAllImportDzRecords();
        return SUCCESS;
    }

    public String notifyMessage(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        ImportDzRecord importDzRecord = mainSerivce.getImportDzRecordByYearAndMonth(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        if (importDzRecord == null || !"N".equals(importDzRecord.getNotification())){
            throw new FwException("上月对账信息己下发或者还没有上传，无法进行下发");
        }
        importDzRecord.setNotification("Y");
        mainSerivce.updateImportDzRecordStatus(importDzRecord);
        return FINISH;
    }

    public List<ImportDzRecord> getImportDzRecords() {
        return importDzRecords;
    }

    public void setImportDzRecords(List<ImportDzRecord> importDzRecords) {
        this.importDzRecords = importDzRecords;
    }

    public void setMainSerivce(MainSerivce mainSerivce) {
        this.mainSerivce = mainSerivce;
    }

    public void setImportDzRecord(ImportDzRecord importDzRecord) {
        this.importDzRecord = importDzRecord;
    }

    public ImportDzRecord getImportDzRecord() {
        return importDzRecord;
    }
}
