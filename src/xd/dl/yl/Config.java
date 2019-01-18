package xd.dl.yl;

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
	//停车场ID
	public static String park_id;
	//停车场名称
	public static String park_name;
	//银联接口地址
	public static String yinlian_baseUrl = "http://unicontent.cn/zhtxproxy/v1.0/parking/standard";
	//应用商户代码
	public static String yinlian_appId = "JSYSTCH";
	//秘钥
	public static String yinlian_sign = "demoEwUt%2BQecIMjU6d5s0OQ%3D%3D";
	//支付结果回调地址
	public static String yinlian_paycallback;
	//退款结果回调地址
	public static String yinlian_refundcallback;
	
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

			park_id = prop.getProperty("park_id");
			park_name = prop.getProperty("park_name");
			yinlian_baseUrl = prop.getProperty("yinlian_baseUrl");
			yinlian_appId = prop.getProperty("yinlian_appId");
			yinlian_sign = prop.getProperty("yinlian_sign");
			yinlian_paycallback = prop.getProperty("yinlian_paycallback");
			yinlian_refundcallback = prop.getProperty("yinlian_refundcallback");
			
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

	public static void main(String[] args) {
		System.out.println(park_id);
	}
}
