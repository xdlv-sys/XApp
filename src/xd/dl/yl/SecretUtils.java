package xd.dl.yl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * SecretUtils {3DES加密解密的工具类 }
 * 
 * @author William
 * @date 2013-04-19
 */
public class SecretUtils {

	// 定义加密算法，有DES、DESede(即3DES)、Blowfish
	private static final String Algorithm = "DESede";

	// private static final String PASSWORD_CRYPT_KEY =
	// "tndEwUt%2BQecIMjU6d5s0OQ%3D%3D";

	// private static final String PASSWORD_IV = "0102030405060708";

	
	
	/**
	 * 加密方法
	 * 
	 * @param src
	 *            源数据的字节数组
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String encryptMode(byte[] src, String key) {
		try {
			SecretKey securekey = new SecretKeySpec(build3DesKey(key),
					Algorithm); // 生成密钥
			// IvParameterSpec iv = new IvParameterSpec(PASSWORD_IV.getBytes(),
			// 0, 15);
			// cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
			// return new String(Hex.encodeHex(cipher.doFinal(str.getBytes())));
			Cipher cipher = Cipher.getInstance(Algorithm); // 实例化负责加密/解密的Cipher工具类
			cipher.init(Cipher.ENCRYPT_MODE, securekey); // 初始化为加密模式

			BASE64Encoder enc = new BASE64Encoder();
			String result = URLEncoder.encode(enc.encode(cipher.doFinal(src)));// 进行BASE64编码
			String newResult = filter(result); // 去掉加密串中的换行符
			return newResult;
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密函数
	 * 
	 * @param src
	 *            密文的字节数组
	 * @return
	 * @throws ValidataException
	 */
	@SuppressWarnings("deprecation")
	public static String decryptMode(String message, String key) {
		String desMessage = null;
		try {
			BASE64Decoder dec = new BASE64Decoder();
			byte[] messageBytes = dec.decodeBuffer(URLDecoder.decode(message));
			SecretKey securekey = new SecretKeySpec(build3DesKey(key),
					Algorithm);
			// IvParameterSpec iv = new IvParameterSpec(PASSWORD_IV.getBytes());
			// c1.init(Cipher.DECRYPT_MODE, securekey,iv); // 初始化为解密模式
			Cipher cipher = Cipher.getInstance(Algorithm);
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			byte[] resultByte = cipher.doFinal(messageBytes);
			desMessage = new String(resultByte);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}

		if (StringUtils.isEmpty(desMessage)) {
			System.out.println("解密失败");
		}

		return desMessage;
	}

	/*
	 * 根据字符串生成密钥字节数组
	 * 
	 * @param keyStr 密钥字符串
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] build3DesKey(String keyStr)
			throws UnsupportedEncodingException {
		byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
		byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组

		/*
		 * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		 */
		if (key.length > temp.length) {
			// 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, temp.length);
		} else {
			// 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	}

	/**
	 * 去掉加密字符串换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String filter(String str) {
		String output = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (asc != 10 && asc != 13) {
				sb.append(str.subSequence(i, i + 1));
			}
		}
		output = new String(sb);
		return output;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String msg = "{\"appId\":\"SHZHTCH\",\"plateNumber\":\"苏A22388\",\"timestamp\":1543371709}";
		System.out.println("【加密前】：" + msg);

		// 加密
		String secretArr = SecretUtils.encryptMode(msg.getBytes(), "demoEwUt%2BQecIMjU6d5s0OQ%3D%3D");
		System.err.println("【加密后】：" + secretArr);

		String message = "oO%2BqWukvM%2Brpa46HFLkdwA%3D%3D";
		// // 解密
		String myMsgArr = SecretUtils.decryptMode(secretArr, "demoEwUt%2BQecIMjU6d5s0OQ%3D%3D");
		System.out.println("【解密后】：" + myMsgArr);
	}
}