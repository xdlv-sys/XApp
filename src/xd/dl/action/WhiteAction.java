package xd.dl.action;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
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
import java.util.ArrayList;
import java.util.Date;
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

        for (int i = 3; ; i++) {
            row = sheet.getRow(i);
            if (row == null || row.getCell(1) == null ||
                    StringUtils.isBlank(row.getCell(1).getStringCellValue())) {
                break;
            }
            GroupItem groupItem = new GroupItem();
            groupItem.setParkId(currentUser().getAddition());
            groupItemList.add(groupItem);

            for (int j = 1; j < 14; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                switch (j) {
                    case 1:
                        groupItem.setCarNumber(getTrimString(cell));
                        logger.info("{},{}", i, groupItem.getCarNumber());
                        break;
                    case 2:
                        groupItem.setStartDate(new Timestamp(getDate(cell).getTime()));
                        break;
                    case 3:
                        groupItem.setEndDate(new Timestamp(getDate(cell).getTime() + (24 * 3600 - 1) * 1000));
                        break;
                    case 4:
                        groupItem.setName(getTrimString(cell).replaceAll("-","杆"));
                        break;
                    case 5:
                        groupItem.setSex("男".equals(getTrimString(cell)) ? (byte) 1 : (byte) 0);
                        break;
                    case 6:
                        String group = getTelAndGroup(cell);
                        groupItem.setRoomNumber(group == null ? null : group.replaceAll("-","杆"));
                        break;
                    case 8:
                        groupItem.setTel(getTelAndGroup(cell));
                        break;
                    case 13:
                        groupItem.setGroupName(getTrimString(cell));
                        break;
                }
            }
        }

        int[] count = {0, 0, 0};

        //process all group
        List<GroupItem> otherGroupItems = new ArrayList<>();
        List<ParkGroup> allGroups = parkService.runInSession(new SessionProcessor<List<ParkGroup>>() {
            @Override
            public List<ParkGroup> process(HibernateTemplate htpl) {
                ParkGroup pgForGroups = new ParkGroup();
                pgForGroups.setParkId(currentUser().getAddition());
                return htpl.findByExample(pgForGroups);
            }
        });

        for (GroupItem item : groupItemList) {
            if (StringUtils.isBlank(item.getCarNumber())) {
                count[2]++;
                continue;
            }
            List<ParkGroup> groups = allGroups;
            if (!"全部".equals(item.getGroupName())) {
                groups = parkService.runInSession(new SessionProcessor<List<ParkGroup>>() {
                    @Override
                    public List<ParkGroup> process(HibernateTemplate htpl) {
                        ParkGroup parkGroup = new ParkGroup();
                        parkGroup.setParkId(currentUser().getAddition());
                        parkGroup.setName(item.getGroupName());
                        return htpl.findByExample(parkGroup);
                    }
                });
            }
            if (groups == null) {
                count[2]++;
                continue;
            }

            for (ParkGroup pg : groups) {
                GroupItem duplicate = new GroupItem();
                BeanUtils.copyProperties(duplicate, item);
                duplicate.setGroupId(pg.getId());
                duplicate.setChannelNumber(pg.getChannelNumber());
                duplicate.setId(null);
                otherGroupItems.add(duplicate);
            }
        }

        parkService.runSessionCommit(new SessionCommit() {
            @Override
            public void process(HibernateTemplate htpl) {
                for (GroupItem gi : otherGroupItems) {
                    if (StringUtils.isBlank(gi.getRoomNumber())) {
                        String virtual = "虚拟";
                        List<?> maxVmRecords = htpl.findByCriteria(DetachedCriteria.forClass(GroupItem.class
                        ).add(Expression.eq("groupId", gi.getGroupId())
                        ).add(Expression.like("roomNumber", virtual, MatchMode.ANYWHERE)));
                        int vm = 0;
                        if (maxVmRecords != null) {
                            vm = maxVmRecords.size() + 1;
                        }
                        gi.setRoomNumber(virtual + vm);
                    }
                    saveOrUpdateWhite(htpl, gi, count);
                }
            }
        });

        setRequestAttribute(
                "msg", String.format("新建%d条记录，更新%d条记录,出错%d条记录", count[0], count[1], count[2]));
        return FINISH;
    }

    private void saveOrUpdateWhite(HibernateTemplate htpl, GroupItem gi, int[] count) {
        List<?> records = htpl.findByCriteria(DetachedCriteria.forClass(GroupItem.class
        ).add(Expression.eq("groupId", gi.getGroupId())
        ).add(Expression.eq("carNumber", gi.getCarNumber())));

        if (records == null || records.size() < 1) {
            htpl.save(gi);
            count[0]++;
        } else {
            GroupItem record = (GroupItem) records.get(0);
            record.setStartDate(gi.getStartDate());
            record.setEndDate(gi.getEndDate());
            record.setTel(gi.getTel());
            record.setRoomNumber(gi.getRoomNumber());
            record.setName(gi.getName());
            record.setSex(gi.getSex());
            record.setChannelNumber(gi.getChannelNumber());
            htpl.update(record);
            count[1]++;
        }
    }

    Date getDate(Cell cell) {
        try {
            return cell.getDateCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    String getTrimString(Cell cell) {
        try {
            return cell.getStringCellValue() == null ? null : cell.getStringCellValue().trim();
        } catch (Exception e) {
            return null;
        }
    }

    public String getTelAndGroup(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                Double d = cell.getNumericCellValue();
                return d.longValue() + "";
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
        }
        return null;
    }

    public String saveParkGroup() {
        parkGroup.setParkId(currentUser().getAddition());
        parkService.runSessionCommit(new SessionCommit() {
            @Override
            public void process(HibernateTemplate htpl) {
                htpl.saveOrUpdate(parkGroup);
                GroupItem white = new GroupItem();
                white.setGroupId(parkGroup.getId());
                List<GroupItem> whites = htpl.findByExample(white);
                if (whites != null){
                    for (GroupItem item: whites){
                        item.setChannelNumber(parkGroup.getChannelNumber());
                        htpl.update(item);
                    }
                }
            }
        });
        return FINISH;
    }

    public String deleteParkGroup() {
        FwUtil.safeEach(groups, new FwUtil.SafeEachProcess<ParkGroup>() {
            @Override
            public void process(ParkGroup parkGroup) {
                parkService.runSessionCommit(new SessionCommit() {
                    @Override
                    public void process(HibernateTemplate htpl) {
                        htpl.delete(parkGroup);
                        GroupItem white = new GroupItem();
                        white.setGroupId(parkGroup.getId());
                        htpl.deleteAll(htpl.findByExample(white));
                    }
                });
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

        total += 1;
        ParkGroup allGroup = new ParkGroup();
        allGroup.setId(-1);
        allGroup.setChannelName("全部");
        groups.add(0, allGroup);
        return SUCCESS;
    }

    public String obtainWhites() throws Exception {
        if (white == null) {
            white = new GroupItem();
        }
        white.setParkId(currentUser().getAddition());

        total = parkService.getAllCount(GroupItem.class, white);
        whites = parkService.getList(GroupItem.class, white, null, start, limit);
        return SUCCESS;
    }

    public String saveWhite() throws Exception {
        if (StringUtils.isBlank(white.getParkId())) {
            white.setParkId(currentUser().getAddition());
        }
        List<Integer> groupIds = white.getGroupIds();
        for (int i = 0; groupIds != null && i < groupIds.size(); i++) {
            GroupItem duplicate = new GroupItem();
            BeanUtils.copyProperties(duplicate, white);
            duplicate.setGroupId(groupIds.get(i));
            duplicate.setId(white.getId());
            parkService.runSessionCommit(new SessionCommit(){
                @Override
                public void process(HibernateTemplate htpl) {
                    duplicate.setChannelNumber(
                            parkService.get(ParkGroup.class, duplicate.getGroupId()).getChannelNumber());
                    saveOrUpdateWhite(htpl, duplicate,new int[3]);
                }
            });
        }
        return FINISH;
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
