package xd.dl.action;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import xd.dl.DlConst;
import xd.dl.bean.GroupItem;
import xd.dl.bean.ParkGroup;
import xd.dl.mina.ParkHandler;
import xd.fw.FwUtil;
import xd.fw.bean.User;
import xd.fw.service.SessionCommit;
import xd.fw.service.SessionProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class WhiteAction extends ParkBaseAction implements DlConst {

    @Override
    public void validate() {
        if (StringUtils.isBlank(currentUser().getAddition())) {
            throw new IllegalArgumentException("");
        }
    }

    @Autowired
    ParkHandler parkHandler;

    List<GroupItem> whites;
    GroupItem white;
    ParkGroup parkGroup;
    List<ParkGroup> groups;

    File groupFile;

    public String importGroup() throws Exception {
        Workbook wb = parseFile(groupFile);
        Sheet sheet = wb.getSheetAt(0);
        Cell cell;
        Row row;
        List<GroupItem> groupItemList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<ParkGroup> allGroups = parkService.runInSession(new SessionProcessor<List<ParkGroup>>() {
            @Override
            public List<ParkGroup> process(HibernateTemplate htpl) {
                ParkGroup pgForGroups = new ParkGroup();
                pgForGroups.setParkId(currentUser().getAddition());
                return htpl.findByExample(pgForGroups);
            }
        });

        int ALL_GROUP = -10010;

;        for (int i = 3; ; i++) {
            row = sheet.getRow(i);
            if (row == null || row.getCell(1) == null ||
                    StringUtils.isBlank(row.getCell(1).getStringCellValue())) {
                break;
            }
            GroupItem groupItem = new GroupItem();
            groupItemList.add(groupItem);

            for (int j = 1; j < 14; j++) {
                cell = row.getCell(j);
                if (cell == null){
                    continue;
                }
                switch (j) {
                    case 1:
                        groupItem.setCarNumber(cell.getStringCellValue());
                        break;
                    case 2:
                        groupItem.setStartDate(new Timestamp(cell.getDateCellValue().getTime()));
                        break;
                    case 3:
                        groupItem.setEndDate(new Timestamp(cell.getDateCellValue().getTime()));
                        break;
                    case 4:
                        groupItem.setName(cell.getStringCellValue());
                        break;
                    case 5:
                        groupItem.setSex("男".equals(cell.getStringCellValue()) ? (byte) 1 : (byte) 0);
                        break;
                    case 6:
                        groupItem.setRoomNumber(cell.getStringCellValue());
                        break;
                    case 8:
                        groupItem.setTel(cell.getStringCellValue());
                        break;
                    case 13:
                        String value = cell.getStringCellValue();

                        if (value.equals("全部")) {
                            //duplicate groupItem
                            groupItem.setGroupId(ALL_GROUP);
                        } else {
                            // find the group
                            final String groupName = value;
                            Integer groupId = parkService.runInSession(new SessionProcessor<Integer>() {
                                @Override
                                public Integer process(HibernateTemplate htpl) {
                                    ParkGroup parkGroup = new ParkGroup();
                                    parkGroup.setParkId(currentUser().getAddition());
                                    parkGroup.setName(groupName);
                                    List<ParkGroup> gs = htpl.findByExample(parkGroup);
                                    if (gs == null || gs.size() < 1) {
                                        return null;
                                    }
                                    return gs.get(0).getId();
                                }
                            });
                            groupItem.setGroupId(groupId);
                        }
                        break;
                }
            }
        }
        int[] count = {0, 0};

        //process all group
        List<GroupItem> otherGroupItems = new ArrayList<>();
        for (GroupItem item : groupItemList) {
            if (item.getGroupId() != ALL_GROUP) {
                otherGroupItems.add(item);
                continue;
            }
            for (ParkGroup pg : allGroups) {
                GroupItem duplicate = new GroupItem();
                BeanUtils.copyProperties(duplicate, item);
                duplicate.setGroupId(pg.getId());
                otherGroupItems.add(duplicate);
            }
        }

        parkService.runSessionCommit(new SessionCommit() {
            @Override
            public void process(HibernateTemplate htpl) {
                GroupItem example = new GroupItem();
                List<GroupItem> records;
                GroupItem record;
                for (GroupItem gi : otherGroupItems) {
                    example.setCarNumber(gi.getCarNumber());
                    example.setGroupId(gi.getGroupId());
                    records = htpl.findByExample(example);
                    if (records == null || records.size() < 1) {
                        htpl.save(gi);
                        count[0]++;
                    } else {
                        record = records.get(0);
                        record.setStartDate(gi.getStartDate());
                        record.setEndDate(gi.getEndDate());
                        record.setTel(gi.getTel());
                        record.setRoomNumber(gi.getRoomNumber());
                        record.setName(gi.getName());
                        record.setSex(gi.getSex());
                        htpl.update(record);
                        count[1]++;
                    }
                }
            }
        });

        setRequestAttribute(
                "msg", String.format("新建%d条记录，更新%d条记录.", count[0], count[1]));
        return FINISH;
    }

    public String saveParkGroup() {
        parkGroup.setParkId(currentUser().getAddition());
        parkService.saveOrUpdate(parkGroup);
        return FINISH;
    }

    public String deleteParkGroup() {
        FwUtil.safeEach(groups, new FwUtil.SafeEachProcess<ParkGroup>() {
            @Override
            public void process(ParkGroup parkGroup) {
                parkService.delete(ParkGroup.class, parkGroup.getId());
            }
        });
        return FINISH;
    }

    public String deleteWhite() {
        FwUtil.safeEach(whites, new FwUtil.SafeEachProcess<GroupItem>() {
            @Override
            public void process(GroupItem item) {
                parkService.delete(GroupItem.class, item.getId());
            }
        });
        return FINISH;
    }

    public String obtainGroups() throws Exception {
        User user = currentUser();
        if (StringUtils.isNotBlank(user.getAddition())) {
            if (parkGroup == null) {
                parkGroup = new ParkGroup();
                parkGroup.setParkId(user.getAddition());
            }
        }
        total = parkService.getAllCount(ParkGroup.class, parkGroup);
        groups = parkService.getList(ParkGroup.class, parkGroup, null, start, limit);
        return SUCCESS;
    }

    public String obtainWhites() throws Exception {
        User user = currentUser();
        if (white == null) {
            white = new GroupItem();
        }

        if (white.getGroupId() == null && StringUtils.isNotBlank(user.getAddition())) {
            //set first group to retrieve white lists
            ParkGroup parkGroup = new ParkGroup();
            parkGroup.setParkId(user.getAddition());
            List<ParkGroup> parkGroupList = parkService.getList(ParkGroup.class, parkGroup, null, -1, -1);
            if (parkGroupList != null && parkGroupList.size() > 0) {
                white.setGroupId(parkGroupList.get(0).getId());
            }
        }
        total = parkService.getAllCount(GroupItem.class, white);
        whites = parkService.getList(GroupItem.class, white, null, start, limit);
        return SUCCESS;
    }

    public void setWhite(GroupItem white) {
        this.white = white;
    }

    public GroupItem getWhite() {
        return white;
    }

    public ParkGroup getParkGroup() {
        return parkGroup;
    }

    public List<GroupItem> getWhites() {
        return whites;
    }

    public void setWhites(List<GroupItem> whites) {
        this.whites = whites;
    }

    public void setParkGroup(ParkGroup parkGroup) {
        this.parkGroup = parkGroup;
    }

    public List<ParkGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ParkGroup> groups) {
        this.groups = groups;
    }

    public void setGroupFile(File groupFile) {
        this.groupFile = groupFile;
    }

    private static Workbook parseFile(File excelFile) throws Exception {
        Workbook book = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(excelFile);
            book = new HSSFWorkbook(inputStream);
        } catch (Exception ex) {
            book = new XSSFWorkbook(excelFile);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return book;
    }
}
