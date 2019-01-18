package xd.dl.yl;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HMacMD5 {

	/**
	 * 计算参数的md5信息
	 * 
	 * @author 尹星
	 * @param str
	 *            待处理的字节数组
	 * @return md5摘要信息
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] md5(byte[] str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str);
		return md.digest();
	}

	/**
	 * 将待加密数据data，通过密钥key，使用hmac-md5算法进行加密，然后返回加密结果。 参照rfc2104 HMAC算法介绍实现。
	 * 
	 * @author 尹星
	 * @param key
	 *            密钥
	 * @param data
	 *            待加密数据
	 * @return 加密结果
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getHmacMd5Bytes(byte[] key, byte[] data)
			throws NoSuchAlgorithmException {
		/*
		 * HmacMd5 calculation formula: H(K XOR opad, H(K XOR ipad, text))
		 * HmacMd5 计算公式：H(K XOR opad, H(K XOR ipad, text))
		 * H代表hash算法，本类中使用MD5算法，K代表密钥，text代表要加密的数据 ipad为0x36，opad为0x5C。
		 */
		int length = 64;
		byte[] ipad = new byte[length];
		byte[] opad = new byte[length];
		for (int i = 0; i < 64; i++) {
			ipad[i] = 0x36;
			opad[i] = 0x5C;
		}
		byte[] actualKey = key; // Actual key.
		byte[] keyArr = new byte[length]; // Key bytes of 64 bytes length
		/*
		 * If key's length is longer than 64,then use hash function to digest it
		 * and use the result as actual key.
		 * 如果密钥长度，大于64字节，就使用哈希算法，计算其摘要，作为真正的密钥。
		 */
		if (key.length > length) {
			actualKey = md5(key);
		}
		for (int i = 0; i < actualKey.length; i++) {
			keyArr[i] = actualKey[i];
		}

		/*
		 * append zeros to K 如果密钥长度不足64字节，就使用0x00补齐到64字节。
		 */
		if (actualKey.length < length) {
			for (int i = actualKey.length; i < keyArr.length; i++)
				keyArr[i] = 0x00;
		}

		/*
		 * calc K XOR ipad 使用密钥和ipad进行异或运算。
		 */
		byte[] kIpadXorResult = new byte[length];
		for (int i = 0; i < length; i++) {
			kIpadXorResult[i] = (byte) (keyArr[i] ^ ipad[i]);
		}

		/*
		 * append "text" to the end of "K XOR ipad" 将待加密数据追加到K XOR ipad计算结果后面。
		 */
		byte[] firstAppendResult = new byte[kIpadXorResult.length + data.length];
		for (int i = 0; i < kIpadXorResult.length; i++) {
			firstAppendResult[i] = kIpadXorResult[i];
		}
		for (int i = 0; i < data.length; i++) {
			firstAppendResult[i + keyArr.length] = data[i];
		}

		/*
		 * calc H(K XOR ipad, text) 使用哈希算法计算上面结果的摘要。
		 */
		byte[] firstHashResult = md5(firstAppendResult);

		/*
		 * calc K XOR opad 使用密钥和opad进行异或运算。
		 */
		byte[] kOpadXorResult = new byte[length];
		for (int i = 0; i < length; i++) {
			kOpadXorResult[i] = (byte) (keyArr[i] ^ opad[i]);
		}

		/*
		 * append "H(K XOR ipad, text)" to the end of "K XOR opad" 将H(K XOR
		 * ipad, text)结果追加到K XOR opad结果后面
		 */
		byte[] secondAppendResult = new byte[kOpadXorResult.length
				+ firstHashResult.length];
		for (int i = 0; i < kOpadXorResult.length; i++) {
			secondAppendResult[i] = kOpadXorResult[i];
		}
		for (int i = 0; i < firstHashResult.length; i++) {
			secondAppendResult[i + keyArr.length] = firstHashResult[i];
		}

		/*
		 * H(K XOR opad, H(K XOR ipad, text)) 对上面的数据进行哈希运算。
		 */
		byte[] hmacMd5Bytes = md5(secondAppendResult);

		return hmacMd5Bytes;

	}

	/**
	 * 9588加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	public static String encodeMac(String data, String key) throws Exception {
		if (data == null || key == null) {
			throw new Exception("reqxml == null || key == null");
		}
		byte[] bytes = getHmacMd5Bytes(key.getBytes("utf-8"),
				data.getBytes("utf-8"));
		String reqhash = new sun.misc.BASE64Encoder().encode(bytes);
		reqhash = dealForUrl(reqhash);
//		System.out.println("reqhash=" + reqhash);
		return reqhash;
	}

	/**
	 * 为了http传输而作的处理
	 * 
	 * @param reqhash
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String dealForUrl(String reqhash) {
		return URLEncoder.encode(reqhash);
	}

	public static void main(String[] args) throws Exception {
		 String str = "{\"channelNo\":\"Q000101\",\"spCode\":\"S0000014\",\"appCode\":\"Y01401\",\"orderNO\":\"2017000140\",\"serialNO\":\"9b11ffd9-5542-426d-ac69-3fa140be43da\",\"goodsCode\":\"1523\",\"goodsName\":\"测试\"一句话描述”\",\"goodType\":\"1\",\"returnFlag\":0,\"model\":4,\"userName\":\"g%2F47WBf8iByCgGwHqVVjrQ%3D%3D\",\"mobile\":\"g%2F47WBf8iByCgGwHqVVjrQ%3D%3D\",\"orderDate\":\"2017-06-23 16:10:19\",\"data\":[{\"subOrderNO\":\"2017000140\",\"price\":15,\"actualPrice\":15,\"planDate\":\"2017-07-23 16:10:24\"}],\"price\":15,\"number\":1,\"frontUrl\":\"https://www.myxtour.com:8443/xtour/toOrderList.action\",\"backUrl\":\"https://www.myxtour.com:8443/xtour/toBackstageCallBack.action\"}";
		 System.out.println("secStr==="
		 + encodeMac(str, "QqXhdp2P67ZF8lLSslC4dg%3D%3D"));
	}
}