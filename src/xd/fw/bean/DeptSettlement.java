package xd.fw.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

/**
 * Created by xd lv on 10/31/2016.
 */
@Entity
@Table(name = "t_jkn_dept_settlement")
public class DeptSettlement {
    @Id
    private int deptId;
    private String deptName;
    private byte percent;
    private Timestamp updateDate;
    @Transient
    private int sumFee;

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public byte getPercent() {
        return percent;
    }

    public void setPercent(byte percent) {
        this.percent = percent;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public int getSumFee() {
        return sumFee;
    }

    public void setSumFee(int sumFee) {
        this.sumFee = sumFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeptSettlement that = (DeptSettlement) o;

        if (deptId != that.deptId) return false;
        if (percent != that.percent) return false;
        if (deptName != null ? !deptName.equals(that.deptName) : that.deptName != null) return false;
        if (updateDate != null ? !updateDate.equals(that.updateDate) : that.updateDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deptId;
        result = 31 * result + (deptName != null ? deptName.hashCode() : 0);
        result = 31 * result + (int) percent;
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        return result;
    }
}
