package xd.fw.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.bean.*;
import xd.fw.bean.mapper.*;
import xd.fw.service.MainSerivce;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class MainSerivceImpl extends BaseServiceImpl implements MainSerivce {

    @Autowired
	UserCompanyMapper userCompanyMapper;
    @Autowired
    UserValidateMapper userValidateMapper;
    @Autowired
    UserdzMapper userdzMapper;
    @Autowired
    DzlistMapper dzlistMapper;
    @Autowired
    ImportDzRecordMapper importDzRecordMapper;

    Logger logger = Logger.getLogger(MainSerivceImpl.class);
	@Override
	public UserCompany[] getUserCompanyByPhone(String phone) {
		return userCompanyMapper.getUserCompanyByPhone(phone);
	}
	
	@Override
	public UserValidate[] getUserValidateForNotSend() {
		return userValidateMapper.selectUserValidateForNotSend();
	}
	
	@Override
	public int updateUserValidateStatus(String phone, String status){
		return userValidateMapper.updateStatusByPhone(phone,status);
	}

	@Override
	public int[] batchSaveUserCompany(List<UserCompany> userCompanyList) {
		int insertCount = 0, deleteCount = 0, updateCount = 0;
        for (UserCompany userCompany : userCompanyList){
            deleteCount = userCompanyMapper.deleteForImport(userCompany);
            if (deleteCount > 0){
                logger.info("replace one more user company record:" + deleteCount);
                updateCount ++;
            } else {
                insertCount ++;
            }
			userCompanyMapper.insert(userCompany);
		}
		return new int[]{insertCount,updateCount};
	}

    @Override
    public int getUserCompanysCount(UserCompany userCompany){
        return userCompanyMapper.getUserCompanysCount(userCompany);
    }

    @Override
    public List<UserCompany> getAllUserCompanys(UserCompany userCompany, int start, int limit){
        return userCompanyMapper.selectUserCompanys(userCompany,start, limit);
    }

	@Override
	public void saveUserValidate(UserValidate userValidate) {
		userValidateMapper.insert(userValidate);
	}

    @Override
    public int deleteUserValidateForUnuse() {
        return userValidateMapper.deleteUserValidateForUnuse();
    }

    @Override
    public UserValidate getValidateCodeByPhone(String phone, String validateCode) {
        return userValidateMapper.getValidateCodeByPhone(phone,validateCode);
    }

    @Override
    public void saveUserDz(Userdz userdz) {
        userdzMapper.insert(userdz);
    }

    @Override
    public Userdz getUserdzByPhone(String phone) {
        return userdzMapper.selectUserdzByKey(phone);
    }

    @Override
    public int batchSaveDzlists(Collection<Dzlist> values, int year, int month) {
        int deleteCount = dzlistMapper.deleteDzlitByYearAndMonth(year, month);
        logger.info(String.format("delete dzlist for %d-%d cout:%d",year,month,deleteCount));
        Date date = new Date();
        for (Dzlist value : values){
            value.setImpdate(date);
            dzlistMapper.insert(value);
        }
        return values.size();
    }

	@Override
	public List<Dzlist> getAllDzlists(Dzlist dzlist,int start, int limit) {
		return dzlistMapper.selectDzlists(dzlist, start, limit);
	}

    @Override
    public void saveOrUpdateImportDzRecord(ImportDzRecord importDzRecord) {
        importDzRecordMapper.deleteImportDzRecordByYearAndMonth(importDzRecord.getYear(),importDzRecord.getMonth());
        importDzRecordMapper.insert(importDzRecord);
    }

    @Override
    public int getDzListCount(Dzlist dzlist) {
        return dzlistMapper.getDzListCount(dzlist);
    }

    @Override
    public List<ImportDzRecord> getAllImportDzRecords() {
        return importDzRecordMapper.getAllImportDzRecords();
    }

    @Override
    public ImportDzRecord getImportDzRecordByYearAndMonth(int year, int month) {
        return importDzRecordMapper.getImportDzRecordByYearAndMonth(year,month);
    }

    @Override
    public void updateImportDzRecordStatus(ImportDzRecord importDzRecord) {
        importDzRecordMapper.updateImportDzRecordStatus(importDzRecord);
    }

    @Override
    public List<ImportDzRecord> getImportDzRecordForSend() {
        return importDzRecordMapper.selectDzRecordByStatus("Y");
    }

    @Override
    public List<Userdz> getAllUserdzs(int start, int limit) {
        return userdzMapper.getAllUserdzs(start, limit);
    }

    @Override
    public ImportDzRecord getLastedNotifyImportDzRecord() {
        int[] yearAndLastMonth = FwUtil.getLastMonth();
        return importDzRecordMapper.getImportDzRecordByYearAndMonth(
                yearAndLastMonth[0], yearAndLastMonth[1]
        );
    }

    @Override
    public List<Dzlist> getDzlist(String wxid, int year, int month) {
        return dzlistMapper.selectDzlistsByYearAndMonth(wxid, year, month);
    }

    @Override
    public void upateDzlistStatus(Dzlist dzlist) {
        dzlistMapper.updateDzlistStatus(dzlist);
    }

    public Userdz getUserdzByOpenid(String openid)
    {
        return userdzMapper.selectUserdzByOpenid(openid);
    }

    @Override
    public void deleteUserdzByOpenId(String fromUserName) {
        userdzMapper.deleteUserdzByOpenid(fromUserName);
    }

    @Override
    public List<UserCompany> getUnBindUserCompany() {
        return userCompanyMapper.selectUnBindUserCompany();
    }

    @Override
    public List<Dzlist> getAllDzlisWithUser(Integer year, Integer month) {
        return dzlistMapper.selectAllDzlisWithUser(year,month);
    }

    @Override
    public int updateDzListAutomatic() {
        return dzlistMapper.updateDzListAutomatic();
    }
}
