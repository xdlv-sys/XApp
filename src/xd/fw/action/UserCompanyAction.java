package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.FwUtil;
import xd.fw.I18n;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Controller;
import xd.fw.bean.UserCompany;
import xd.fw.service.MainSerivce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
@Controller
public class UserCompanyAction extends BaseAction{

    List<UserCompany> userCompanys;
    UserCompany userCompany = new UserCompany();
    @Autowired
    MainSerivce mainSerivce;

    //export
    private InputStream excelFile;
    private String fileName;

    public String obtainUserCompanys()throws Exception{
        if (userCompany != null && StringUtils.isNotBlank(userCompany.getCompanyName())){
            userCompany.setCompanyName(URLDecoder.decode(userCompany.getCompanyName(), FwUtil.UTF8));
        }
        total = mainSerivce.getUserCompanysCount(userCompany);
        userCompanys = mainSerivce.getAllUserCompanys(userCompany,start, limit);
        return SUCCESS;
    }

    public String exportUnBind()throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet1");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        String[] titles = I18n.getI18n("export_telephone_titles").split(" ");
        HSSFCell cell;
        for (int i=0;i<titles.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        List<UserCompany> unBinds = mainSerivce.getUnBindUserCompany();
        UserCompany tmp;
        for (int i=0;unBinds != null && i<unBinds.size(); i++){
            tmp = unBinds.get(i);
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(tmp.getCode());
            row.createCell(1).setCellValue(tmp.getCompanyName());
            row.createCell(2).setCellValue(tmp.getArea());
            row.createCell(3).setCellValue(tmp.getBusiness() == "Y" ? "是": "否");
            row.createCell(4).setCellValue(tmp.getWxContractName1());
            row.createCell(5).setCellValue(tmp.getWxContractPhone1());
            row.createCell(6).setCellValue(tmp.getWxContractName2());
            row.createCell(7).setCellValue(tmp.getWxContractPhone2());
            row.createCell(8).setCellValue(tmp.getManagerName());
            row.createCell(9).setCellValue(tmp.getRemarkContent());
            row.createCell(10).setCellValue(tmp.getCreditScope());
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        wb.write(output);
        excelFile = new ByteArrayInputStream(output.toByteArray());
        output.flush();
        output.close();
        wb.close();
        this.fileName = writeDownloadFile("未绑定用户.xls");
        return EXCEL;
    }

    public void setUserCompanys(List<UserCompany> userCompanys) {
        this.userCompanys = userCompanys;
    }

    public List<UserCompany> getUserCompanys() {
        return userCompanys;
    }

    public void setMainSerivce(MainSerivce mainSerivce) {
        this.mainSerivce = mainSerivce;
    }

    public UserCompany getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(UserCompany userCompany) {
        this.userCompany = userCompany;
    }

    public InputStream getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(InputStream excelFile) {
        this.excelFile = excelFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
