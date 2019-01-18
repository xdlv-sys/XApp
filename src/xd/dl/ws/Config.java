package xd.dl.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
* 配置文件    
* 项目名称：cms   
* 类名称：Config   
* 类描述：   
* 创建人：cww  
* 创建时间：2018年9月25日 上午10:01:48   
* 修改人：cww 
* 修改时间：2018年9月25日 上午10:01:48   
* 修改备注：   
* @version 1.0    
*
 */
public class Config {
	//金鹰WebService地址
	public static String baseUrlJY;
	
	//类加载的时候执行
	static {
		init();
	}

	//加载属性
	private static void init() {
		InputStream in = null;
		try {
			Properties prop = new Properties();
			//getClassLoader()很重要
			in = Config.class.getClassLoader().getResourceAsStream("/app.properties");   
			prop.load(in);

			baseUrlJY = prop.getProperty("baseUrlJY");
			
			in.close();
			in = null;
		} catch (Exception e) {
			e.printStackTrace();
			if( in != null ){
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
