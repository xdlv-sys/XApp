package xd.dl.bean;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by xd on 1/18/2017.
 */
@Entity
@Table(name = "t_park_group")
public class ParkGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "t_park_group")
    @TableGenerator(name = "t_park_group", table = "t_primary_key",
            pkColumnName = "table_name", valueColumnName = "current_id")
    private Integer id;
    private String parkId;
    private String name;
    private String ip;
    private Integer channelNumber;
    private Timestamp retrieveTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(Integer channelNumber) {
        this.channelNumber = channelNumber;
    }

    public Timestamp getRetrieveTime() {
        return retrieveTime;
    }

    public void setRetrieveTime(Timestamp retrieveTime) {
        this.retrieveTime = retrieveTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkGroup parkGroup = (ParkGroup) o;

        if (id != null ? !id.equals(parkGroup.id) : parkGroup.id != null) return false;
        if (parkId != null ? !parkId.equals(parkGroup.parkId) : parkGroup.parkId != null) return false;
        if (name != null ? !name.equals(parkGroup.name) : parkGroup.name != null) return false;
        if (ip != null ? !ip.equals(parkGroup.ip) : parkGroup.ip != null) return false;
        if (channelNumber != null ? !channelNumber.equals(parkGroup.channelNumber) : parkGroup.channelNumber != null)
            return false;
        if (retrieveTime != null ? !retrieveTime.equals(parkGroup.retrieveTime) : parkGroup.retrieveTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (parkId != null ? parkId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (channelNumber != null ? channelNumber.hashCode() : 0);
        result = 31 * result + (retrieveTime != null ? retrieveTime.hashCode() : 0);
        return result;
    }
}
