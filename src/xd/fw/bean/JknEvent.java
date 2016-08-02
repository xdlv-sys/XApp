package xd.fw.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "t_jkn_event")
public class JknEvent {
    @Id
    private int eventId;
    private byte eventType;
    private Integer dbKey;
    private Integer dbInt;
    private String dbContent;
    private Byte eventStatus;
    private Timestamp triggerDate;

    public JknEvent() {
    }

    public JknEvent(Byte eventType, Integer dbKey, String dbContent) {
        this.eventType = eventType;
        this.dbKey = dbKey;
        this.dbContent = dbContent;
    }

    public JknEvent(Byte eventType, Integer dbKey, int dbInt) {
        this.eventType = eventType;
        this.dbKey = dbKey;
        this.dbInt = dbInt;
    }

    public JknEvent(Byte eventType, Byte eventStatus) {
        this.eventType = eventType;
        this.eventStatus = eventStatus;
    }


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public byte getEventType() {
        return eventType;
    }

    public void setEventType(byte eventType) {
        this.eventType = eventType;
    }

    public Integer getDbKey() {
        return dbKey;
    }

    public void setDbKey(Integer dbKey) {
        this.dbKey = dbKey;
    }

    public Integer getDbInt() {
        return dbInt;
    }

    public void setDbInt(Integer dbInt) {
        this.dbInt = dbInt;
    }

    public String getDbContent() {
        return dbContent;
    }

    public void setDbContent(String dbContent) {
        this.dbContent = dbContent;
    }

    public Byte getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Byte eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Timestamp getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Timestamp triggerDate) {
        this.triggerDate = triggerDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JknEvent event = (JknEvent) o;

        if (eventId != event.eventId) return false;
        if (eventType != event.eventType) return false;
        if (dbKey != null ? !dbKey.equals(event.dbKey) : event.dbKey != null) return false;
        if (dbInt != null ? !dbInt.equals(event.dbInt) : event.dbInt != null) return false;
        if (dbContent != null ? !dbContent.equals(event.dbContent) : event.dbContent != null) return false;
        if (eventStatus != null ? !eventStatus.equals(event.eventStatus) : event.eventStatus != null) return false;
        return triggerDate != null ? triggerDate.equals(event.triggerDate) : event.triggerDate == null;

    }

    @Override
    public int hashCode() {
        int result = eventId;
        result = 31 * result + (int) eventType;
        result = 31 * result + (dbKey != null ? dbKey.hashCode() : 0);
        result = 31 * result + (dbInt != null ? dbInt.hashCode() : 0);
        result = 31 * result + (dbContent != null ? dbContent.hashCode() : 0);
        result = 31 * result + (eventStatus != null ? eventStatus.hashCode() : 0);
        result = 31 * result + (triggerDate != null ? triggerDate.hashCode() : 0);
        return result;
    }
}
