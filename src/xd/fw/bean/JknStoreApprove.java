package xd.fw.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "t_jkn_store_approve")
public class JknStoreApprove {
    @Id
    private int approveId;
    private int userId;
    private byte approveType;
    private byte approveStatus;
    private Timestamp createDate;
    private Timestamp approveDate;

    public int getApproveId() {
        return approveId;
    }

    public void setApproveId(int approveId) {
        this.approveId = approveId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte getApproveType() {
        return approveType;
    }

    public void setApproveType(byte approveType) {
        this.approveType = approveType;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Timestamp approveDate) {
        this.approveDate = approveDate;
    }

    public void setApproveStatus(byte approveStatus) {
        this.approveStatus = approveStatus;
    }

    public byte getApproveStatus() {
        return approveStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JknStoreApprove that = (JknStoreApprove) o;

        if (approveId != that.approveId) return false;
        if (userId != that.userId) return false;
        if (approveType != that.approveType) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (approveDate != null ? !approveDate.equals(that.approveDate) : that.approveDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = approveId;
        result = 31 * result + userId;
        result = 31 * result + (int) approveType;
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (approveDate != null ? approveDate.hashCode() : 0);
        return result;
    }
}
