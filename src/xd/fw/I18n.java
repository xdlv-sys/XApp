package xd.fw;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class I18n {

	static Logger logger = Logger.getLogger(I18n.class);
	static Properties properties;
	//static Map<String, String> i18nMap = new HashMap<String, String>();
	static {
		properties = new Properties();
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(I18n.class.getResourceAsStream(
					"/i18n.properties"),FwUtil.UTF8);
			properties.load(reader);
		} catch (IOException e) {
			logger.error("can not load i18n files" , e);
		} finally {
			try {
				if (reader != null){
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		
	}
	public static String getI18n(String key){
		return properties.getProperty(key, key);
	}
}
