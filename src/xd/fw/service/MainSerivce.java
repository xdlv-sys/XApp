package xd.fw.service;

import xd.fw.bean.*;

import java.util.Collection;
import java.util.List;

public interface MainSerivce extends BaseService {

	UserCompany[] getUserCompanyByPhone(String phone);
	
	UserValidate[] getUserValidateForNotSend();
	
	int updateUserValidateStatus(String phone,String status);

	int[] batchSaveUserCompany(List<UserCompany> userCompanyList);

	int getUserCompanysCount(UserCompany userCompany);

	List<UserCompany> getAllUserCompanys(UserCompany userCompany, int start, int limit);

	void saveUserValidate(UserValidate userValidate);
    
    int deleteUserValidateForUnuse();

    UserValidate getValidateCodeByPhone(String phone, String validateCode);

    void saveUserDz(Userdz userdz);

    Userdz getUserdzByPhone(String phone);

    int batchSaveDzlists(Collection<Dzlist> values, int year, int month);

	int getDzListCount(Dzlist dzlist);

    List<Dzlist> getAllDzlists(Dzlist dzlist, int start, int limit);

    void saveOrUpdateImportDzRecord(ImportDzRecord importDzRecord);

    List<ImportDzRecord> getAllImportDzRecords();

    ImportDzRecord getImportDzRecordByYearAndMonth(int year, int month);

    void updateImportDzRecordStatus(ImportDzRecord importDzRecord);

    List<ImportDzRecord> getImportDzRecordForSend();

    List<Userdz> getAllUserdzs(int start, int limit);

    ImportDzRecord getLastedNotifyImportDzRecord();

    List<Dzlist> getDzlist(String wxid, int year, int month);

    void upateDzlistStatus(Dzlist dzlist);

    Userdz getUserdzByOpenid(String openId);

    void deleteUserdzByOpenId(String fromUserName);

    List<UserCompany> getUnBindUserCompany();

    List<Dzlist> getAllDzlisWithUser(Integer year, Integer month);

    int updateDzListAutomatic();
}