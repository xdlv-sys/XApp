package xd.dl.bean;

import org.apache.struts2.json.annotations.JSON;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by xd on 1/18/2017.
 */
@Entity
@Table(name = "t_group_item")
public class GroupItem {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "t_group_item")
    @TableGenerator(name = "t_group_item", table = "t_primary_key",
            pkColumnName = "table_name", valueColumnName = "current_id")
    private Integer id;
    private Integer groupId;
    private String carNumber;
    private String name;
    private Byte sex;
    private String roomNumber;
    private String tel;
    private Timestamp startDate;
    private Timestamp endDate;
    private Byte color;
    private Byte plateType;

    public String getCarNumber() {
        return carNumber;
    }
    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Byte getColor() {
        return color;
    }

    public void setColor(Byte color) {
        this.color = color;
    }

    public Byte getPlateType() {
        return plateType;
    }

    public void setPlateType(Byte plateType) {
        this.plateType = plateType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupItem groupItem = (GroupItem) o;

        if (id != null ? !id.equals(groupItem.id) : groupItem.id != null) return false;
        if (groupId != null ? !groupId.equals(groupItem.groupId) : groupItem.groupId != null) return false;
        if (startDate != null ? !startDate.equals(groupItem.startDate) : groupItem.startDate != null) return false;
        if (endDate != null ? !endDate.equals(groupItem.endDate) : groupItem.endDate != null) return false;
        if (color != null ? !color.equals(groupItem.color) : groupItem.color != null) return false;
        if (plateType != null ? !plateType.equals(groupItem.plateType) : groupItem.plateType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (plateType != null ? plateType.hashCode() : 0);
        return result;
    }
}
