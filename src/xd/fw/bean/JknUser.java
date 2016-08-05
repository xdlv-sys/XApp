package xd.fw.bean;

import org.hibernate.annotations.Tables;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_jkn_user")
public class JknUser {
    @Id
    private int userId;

    private String userName;
    private Integer referrer;
    private String telephone;
    private byte vip;
    private byte userLevel;
    private byte areaLevel;
    private int consumedCount;
    private int count;
    private int countOne;
    private int countTwo;
    private int countThree;
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getReferrer() {
        return referrer;
    }

    public void setReferrer(Integer referrer) {
        this.referrer = referrer;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public byte getVip() {
        return vip;
    }

    public void setVip(byte vip) {
        this.vip = vip;
    }

    public byte getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(byte userLevel) {
        this.userLevel = userLevel;
    }

    public byte getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(byte areaLevel) {
        this.areaLevel = areaLevel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(int count){
        this.count += count;
    }

    public int getCountOne() {
        return countOne;
    }

    public void setCountOne(int countOne) {
        this.countOne = countOne;
    }

    public int getCountTwo() {
        return countTwo;
    }

    public void setCountTwo(int countTwo) {
        this.countTwo = countTwo;
    }

    public int getCountThree() {
        return countThree;
    }

    public void setCountThree(int countThree) {
        this.countThree = countThree;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public int getConsumedCount() {
        return consumedCount;
    }

    public void setConsumedCount(int consumedCount) {
        this.consumedCount = consumedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JknUser user = (JknUser) o;

        if (userId != user.userId) return false;
        if (vip != user.vip) return false;
        if (userLevel != user.userLevel) return false;
        if (areaLevel != user.areaLevel) return false;
        if (consumedCount != user.consumedCount) return false;
        if (count != user.count) return false;
        if (countOne != user.countOne) return false;
        if (countTwo != user.countTwo) return false;
        if (countThree != user.countThree) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        if (referrer != null ? !referrer.equals(user.referrer) : user.referrer != null) return false;
        if (telephone != null ? !telephone.equals(user.telephone) : user.telephone != null) return false;
        return regDate != null ? regDate.equals(user.regDate) : user.regDate == null;

    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (referrer != null ? referrer.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (int) vip;
        result = 31 * result + (int) userLevel;
        result = 31 * result + (int) areaLevel;
        result = 31 * result + consumedCount;
        result = 31 * result + count;
        result = 31 * result + countOne;
        result = 31 * result + countTwo;
        result = 31 * result + countThree;
        result = 31 * result + (regDate != null ? regDate.hashCode() : 0);
        return result;
    }
}
