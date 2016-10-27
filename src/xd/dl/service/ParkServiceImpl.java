package xd.dl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.dl.bean.mapper.EnterOrOutRecordMapper;
import xd.fw.bean.EnterOrOutRecord;
import xd.fw.service.impl.HibernateServiceImpl;

import java.util.List;

@Service
public class ParkServiceImpl extends HibernateServiceImpl implements ParkService {

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
