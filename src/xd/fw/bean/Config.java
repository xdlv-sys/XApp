package xd.fw.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_jkn_config")
public class Config {
    @Id
    private String configName;
    private String configValue;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Config config = (Config) o;

        if (configName != null ? !configName.equals(config.configName) : config.configName != null) return false;
        if (configValue != null ? !configValue.equals(config.configValue) : config.configValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = configName != null ? configName.hashCode() : 0;
        result = 31 * result + (configValue != null ? configValue.hashCode() : 0);
        return result;
    }
}
