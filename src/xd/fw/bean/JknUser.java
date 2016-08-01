package xd.fw.bean;

import org.hibernate.annotations.Tables;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_jkn_user")
public class JknUser {
    @Id
    private Integer userId;

    private String userName;
    private Integer referrer;
    private String telephone;
    private Byte vip;
    private Byte userLevel;
    private Byte areaLevel;
    private Integer count;
    private Integer countOne;
    private Integer countTwo;
    private Integer countThree;
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    public Byte getVip() {
        return vip;
    }

    public void setVip(Byte vip) {
        this.vip = vip;
    }

    public Byte getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Byte userLevel) {
        this.userLevel = userLevel;
    }

    public Byte getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Byte areaLevel) {
        this.areaLevel = areaLevel;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCountOne() {
        return countOne;
    }

    public void setCountOne(Integer countOne) {
        this.countOne = countOne;
    }

    public Integer getCountTwo() {
        return countTwo;
    }

    public void setCountTwo(Integer countTwo) {
        this.countTwo = countTwo;
    }

    public Integer getCountThree() {
        return countThree;
    }

    public void setCountThree(Integer countThree) {
        this.countThree = countThree;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JknUser jknUser = (JknUser) o;

        if (userId != null ? !userId.equals(jknUser.userId) : jknUser.userId != null) return false;
        if (userName != null ? !userName.equals(jknUser.userName) : jknUser.userName != null) return false;
        if (referrer != null ? !referrer.equals(jknUser.referrer) : jknUser.referrer != null) return false;
        if (telephone != null ? !telephone.equals(jknUser.telephone) : jknUser.telephone != null) return false;
        if (vip != null ? !vip.equals(jknUser.vip) : jknUser.vip != null) return false;
        if (userLevel != null ? !userLevel.equals(jknUser.userLevel) : jknUser.userLevel != null) return false;
        if (areaLevel != null ? !areaLevel.equals(jknUser.areaLevel) : jknUser.areaLevel != null) return false;
        if (count != null ? !count.equals(jknUser.count) : jknUser.count != null) return false;
        if (countOne != null ? !countOne.equals(jknUser.countOne) : jknUser.countOne != null) return false;
        if (countTwo != null ? !countTwo.equals(jknUser.countTwo) : jknUser.countTwo != null) return false;
        if (countThree != null ? !countThree.equals(jknUser.countThree) : jknUser.countThree != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (referrer != null ? referrer.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (vip != null ? vip.hashCode() : 0);
        result = 31 * result + (userLevel != null ? userLevel.hashCode() : 0);
        result = 31 * result + (areaLevel != null ? areaLevel.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (countOne != null ? countOne.hashCode() : 0);
        result = 31 * result + (countTwo != null ? countTwo.hashCode() : 0);
        result = 31 * result + (countThree != null ? countThree.hashCode() : 0);
        return result;
    }
}
