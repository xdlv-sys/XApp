package xd.fw.action;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xd.fw.FwUtil;
import xd.fw.I18n;
import xd.fw.bean.Dzlist;
import xd.fw.service.MainSerivce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by xd on 2015/10/31.
 */
@Controller
public class DzlistAction extends BaseAction{

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Dzlist> dzlists;
    @Autowired
    MainSerivce mainSerivce;

    Dzlist dzlist = new Dzlist();
    private InputStream excelFile;
    private String fileName;
    public String obtainDzlists()throws Exception{
        if (dzlist != null && StringUtils.isNotBlank(dzlist.getUsername())){
            dzlist.setUsername(URLDecoder.decode(dzlist.getUsername(), FwUtil.UTF8));
        }
        total = mainSerivce.getDzListCount(dzlist);
        dzlists = mainSerivce.getAllDzlists(dzlist,start, limit);
        return SUCCESS;
    }

    public String downLoadDzlists() throws Exception{
        obtainDzlists();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = createDzlistSheet(wb, "sheet1");
        HSSFRow row;
        for (int i=0;dzlists != null && i<dzlists.size(); i++){
            row = sheet.createRow(i+1);
            writeRowValue(row,dzlists.get(i));
        }
        writeExcelFile(wb,"对账结果导出.xls");
        return EXCEL;
    }

    public String exportGroupData()throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet noTelephoneSheet = createDzlistSheet(wb, "无对账手机号码");
        HSSFSheet noBindSheet = createDzlistSheet(wb, "未绑定");
        HSSFSheet bindSheet = createDzlistSheet(wb, "己绑定");
        dzlists = mainSerivce.getAllDzlisWithUser(dzlist.getYear(), dzlist.getMonth());
        Dzlist tmp;
        HSSFRow tmpRow;
        int noTelephoneCount = 1, noBindCount = 1, bindCount = 1;

        for (int i=0; dzlists != null && i<dzlists.size();i++){
            tmp = dzlists.get(i);
            if (tmp.getUserCompany() == null || StringUtils.isBlank(tmp.getUserCompany().getWxPhone())){
                tmpRow = noTelephoneSheet.createRow(noTelephoneCount++);
            } else if (tmp.getUserdz() == null || StringUtils.isBlank(tmp.getUserdz().getWxid())){
                tmpRow = noBindSheet.createRow(noBindCount++);
            } else {
                tmpRow = bindSheet.createRow(bindCount++);
            }
            writeRowValue(tmpRow, tmp);
        }
        writeExcelFile(wb, "对账分类文件.xls");
        return EXCEL;
    }
    private void writeExcelFile(HSSFWorkbook wb, String fileName) throws Exception{
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        wb.write(output);
        excelFile = new ByteArrayInputStream(output.toByteArray());
        output.flush();
        output.close();
        wb.close();
        this.fileName = writeDownloadFile(fileName);
    }
    private void writeRowValue(HSSFRow row, Dzlist dzlist){
        row.createCell(0).setCellValue(dzlist.getYear());
        row.createCell(1).setCellValue(dzlist.getMonth());
        row.createCell(2).setCellValue(sdf.format(dzlist.getImpdate()));
        row.createCell(3).setCellValue(dzlist.getUserid());
        row.createCell(4).setCellValue(dzlist.getUsername());
        row.createCell(5).setCellValue(dzlist.getIsok());
        row.createCell(6).setCellValue(dzlist.getQmye());
        row.createCell(7).setCellValue(dzlist.getZdxsk1());
        row.createCell(8).setCellValue(dzlist.getYsdsk1());
        row.createCell(9).setCellValue(dzlist.getZdfwk1());
        row.createCell(10).setCellValue(dzlist.getJb1());
        row.createCell(11).setCellValue(dzlist.getFl1());
        row.createCell(12).setCellValue(dzlist.getZdxsk2());
        row.createCell(13).setCellValue(dzlist.getJb2());
        row.createCell(14).setCellValue(dzlist.getFl2());
        row.createCell(15).setCellValue(dzlist.getZdfwk2());
        row.createCell(16).setCellValue(dzlist.getQtyfdk2());
        row.createCell(17).setCellValue(dzlist.getCreditScope());
    }
    private HSSFSheet createDzlistSheet(HSSFWorkbook wb, String name){
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFSheet sheet = wb.createSheet(name);
        HSSFRow row = sheet.createRow(0);
        row.setRowStyle(style);
        HSSFCell cell;
        String[] titles = I18n.getI18n("export_dz_titles").split(" ");
        for (int i=0;i<titles.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        return sheet;
    }

    public InputStream getExcelFile() {
        return excelFile;
    }

    public String getFileName() throws Exception {
        return fileName;
    }

    public void setDzlists(List<Dzlist> dzlists) {
        this.dzlists = dzlists;
    }

    public List<Dzlist> getDzlists() {
        return dzlists;
    }

    public Dzlist getDzlist() {
        return dzlist;
    }

    public void setDzlist(Dzlist dzlist) {
        this.dzlist = dzlist;
    }

}
