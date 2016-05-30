package xd.fw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.bean.*;
import xd.fw.bean.mapper.*;
import xd.fw.service.FwService;
import xd.fw.service.ParkService;

import java.util.List;

@Service
public class ParkServiceImpl extends BaseServiceImpl implements ParkService {

    @Autowired
    EnterOrOutRecordMapper enterOrOutRecordMapper;

    public List<EnterOrOutRecord> geUploadRecords(int enterOrOut){
        return enterOrOutRecordMapper.selectUploadRecords(enterOrOut);
    }

    @Override
    public int deleteFinishRecord() {
        return enterOrOutRecordMapper.deleteFinishRecords();
    }

    @Override
    public void updateRecordStatus(String orderNum, int retStatus, String msg) {
        enterOrOutRecordMapper.updateRecordStatus(orderNum,retStatus, msg);
    }

    @Override
    public int getFreeParkStation() {
        return enterOrOutRecordMapper.getFreeParkStation();
    }
}
