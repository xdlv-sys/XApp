package xd.fw;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;

public class I18n {

    static Logger logger = Logger.getLogger(I18n.class);
    static Properties properties;
    static File webInfoDir;

    static {
        properties = new Properties();
        String i18nFile = "/i18n.properties";
        try (InputStreamReader reader = new InputStreamReader(I18n.class.getResourceAsStream(
                i18nFile), FwUtil.UTF8)) {
            properties.load(reader);
            webInfoDir = new File(I18n.class.getResource(i18nFile).toURI().toURL().getFile()).getParentFile().getParentFile();
        } catch (Exception e) {
            logger.error("can not load i18n files", e);
        }
    }

    public static File getWebInfDir(){
        return webInfoDir;
    }

    public static String getI18n(String key) {
        return properties.getProperty(key, key);
    }

    public static void setI18n(String key, String value){
        properties.setProperty(key, value);
    }
}
