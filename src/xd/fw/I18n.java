package xd.fw;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class I18n {

    static Logger logger = Logger.getLogger(I18n.class);
    static Properties properties;

    static {
        properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(I18n.class.getResourceAsStream(
                "/i18n.properties"), FwUtil.UTF8)) {
            properties.load(reader);
        } catch (IOException e) {
            logger.error("can not load i18n files", e);
        }
    }

    public static String getI18n(String key) {
        return properties.getProperty(key, key);
    }

    public static void setI18n(String key, String value){
        properties.setProperty(key, value);
    }
}
