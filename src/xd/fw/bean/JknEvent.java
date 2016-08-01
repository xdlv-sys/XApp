package xd.fw.bean;

import xd.fw.service.JknService;

import javax.persistence.*;
import javax.print.attribute.IntegerSyntax;

@Entity
@Table(name = "t_jkn_event")
public class JknEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    private Byte eventType;
    private Integer dbKey;
    private Integer dbInt;
    private String dbContent;
    private Byte eventStatus;

    //-------------
    public JknEvent(){}

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
    //---------

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Byte getEventType() {
        return eventType;
    }

    public void setEventType(Byte eventType) {
        this.eventType = eventType;
    }

    public Integer getDbKey() {
        return dbKey;
    }

    public void setDbKey(Integer dbKey) {
        this.dbKey = dbKey;
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

    public void setDbInt(Integer dbInt) {
        this.dbInt = dbInt;
    }

    public Integer getDbInt() {
        return dbInt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JknEvent jknEvent = (JknEvent) o;

        if (eventId != null ? !eventId.equals(jknEvent.eventId) : jknEvent.eventId != null) return false;
        if (eventType != null ? !eventType.equals(jknEvent.eventType) : jknEvent.eventType != null) return false;
        if (dbKey != null ? !dbKey.equals(jknEvent.dbKey) : jknEvent.dbKey != null) return false;
        if (dbInt != null ? !dbInt.equals(jknEvent.dbInt) : jknEvent.dbInt != null) return false;
        if (dbContent != null ? !dbContent.equals(jknEvent.dbContent) : jknEvent.dbContent != null) return false;
        return eventStatus != null ? eventStatus.equals(jknEvent.eventStatus) : jknEvent.eventStatus == null;

    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (dbKey != null ? dbKey.hashCode() : 0);
        result = 31 * result + (dbInt != null ? dbInt.hashCode() : 0);
        result = 31 * result + (dbContent != null ? dbContent.hashCode() : 0);
        result = 31 * result + (eventStatus != null ? eventStatus.hashCode() : 0);
        return result;
    }
}
