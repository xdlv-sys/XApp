package xd.fw.service;

import xd.fw.bean.EnterOrOutRecord;
import xd.fw.bean.Mod;
import xd.fw.bean.Role;
import xd.fw.bean.User;

import java.util.List;

public interface ParkService extends BaseService{


    List<EnterOrOutRecord> geUploadRecords(int enterOrOut);

    void updateRecordStatus(String orderNum,int retStatus, String msg);

    int getFreeParkStation();

    int deleteFinishRecord();
}
