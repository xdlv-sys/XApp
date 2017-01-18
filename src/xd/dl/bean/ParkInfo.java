package xd.dl.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "t_park_info")
public class ParkInfo {
    @Id
    private String parkId;
    private String parkName;
    private String appId;
    private String secret;
    private String mchId;
    private String wxKey;
    private String limitPay;
    private String partnerId;
    private String aliAppId;
    private String aliShaRsaKey;
    private String aliPublicKey;
    private Short proxyState;
    private Integer freeCount;
    private int proxyVersion;

    public ParkInfo(String parkId, String parkName, String appId
            , String limitPay, String partnerId, Integer freeCount
            , Short proxyState) {
        this.parkId = parkId;
        this.parkName = parkName;
        this.appId = appId;
        this.limitPay = limitPay;
        this.partnerId = partnerId;
        this.freeCount = freeCount;
        this.proxyState = proxyState;
    }

    private Timestamp lastUpdate;

    public ParkInfo(){}


    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getWxKey() {
        return wxKey;
    }

    public void setWxKey(String wxKey) {
        this.wxKey = wxKey;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getAliShaRsaKey() {
        return aliShaRsaKey;
    }

    public void setAliShaRsaKey(String aliShaRsaKey) {
        this.aliShaRsaKey = aliShaRsaKey;
    }

    public Short getProxyState() {
        return proxyState;
    }

    public void setProxyState(Short proxyState) {
        this.proxyState = proxyState;
    }

    public Integer getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(Integer freeCount) {
        this.freeCount = freeCount;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setAliAppId(String aliAppId) {
        this.aliAppId = aliAppId;
    }

    public String getAliAppId() {
        return aliAppId;
    }

    public int getProxyVersion() {
        return proxyVersion;
    }

    public void setProxyVersion(int proxyVersion) {
        this.proxyVersion = proxyVersion;
    }

    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public void setAliPublicKey(String aliPublicKey) {
        this.aliPublicKey = aliPublicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkInfo parkInfo = (ParkInfo) o;

        if (parkId != null ? !parkId.equals(parkInfo.parkId) : parkInfo.parkId != null) return false;
        if (parkName != null ? !parkName.equals(parkInfo.parkName) : parkInfo.parkName != null) return false;
        if (appId != null ? !appId.equals(parkInfo.appId) : parkInfo.appId != null) return false;
        if (secret != null ? !secret.equals(parkInfo.secret) : parkInfo.secret != null) return false;
        if (mchId != null ? !mchId.equals(parkInfo.mchId) : parkInfo.mchId != null) return false;
        if (wxKey != null ? !wxKey.equals(parkInfo.wxKey) : parkInfo.wxKey != null) return false;
        if (limitPay != null ? !limitPay.equals(parkInfo.limitPay) : parkInfo.limitPay != null) return false;
        if (partnerId != null ? !partnerId.equals(parkInfo.partnerId) : parkInfo.partnerId != null) return false;
        if (aliAppId != null ? !aliAppId.equals(parkInfo.aliAppId) : parkInfo.aliAppId != null) return false;
        if (aliShaRsaKey != null ? !aliShaRsaKey.equals(parkInfo.aliShaRsaKey) : parkInfo.aliShaRsaKey != null)
            return false;
        if (proxyState != null ? !proxyState.equals(parkInfo.proxyState) : parkInfo.proxyState != null) return false;
        if (freeCount != null ? !freeCount.equals(parkInfo.freeCount) : parkInfo.freeCount != null) return false;
        return lastUpdate != null ? lastUpdate.equals(parkInfo.lastUpdate) : parkInfo.lastUpdate == null;

    }

    @Override
    public int hashCode() {
        int result = parkId != null ? parkId.hashCode() : 0;
        result = 31 * result + (parkName != null ? parkName.hashCode() : 0);
        result = 31 * result + (appId != null ? appId.hashCode() : 0);
        result = 31 * result + (secret != null ? secret.hashCode() : 0);
        result = 31 * result + (mchId != null ? mchId.hashCode() : 0);
        result = 31 * result + (wxKey != null ? wxKey.hashCode() : 0);
        result = 31 * result + (limitPay != null ? limitPay.hashCode() : 0);
        result = 31 * result + (partnerId != null ? partnerId.hashCode() : 0);
        result = 31 * result + (aliAppId != null ? aliAppId.hashCode() : 0);
        result = 31 * result + (aliShaRsaKey != null ? aliShaRsaKey.hashCode() : 0);
        result = 31 * result + (proxyState != null ? proxyState.hashCode() : 0);
        result = 31 * result + (freeCount != null ? freeCount.hashCode() : 0);
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        return result;
    }
}
