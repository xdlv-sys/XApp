package xd.fw;

import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by exiglvv on 7/13/2016.
 */
public class T {
    public static void main(String[] args) throws Exception {
        String wx = "nonce_str=7ab29e0z81ofvjr8, sign=19D6C8FF14DBCB9334252DF0AED9105A, fee_type=CNY, mch_id=1360170902" +
                ", cash_fee=1, total_fee=1, time_end=20160726165637, transaction_id=4004372001201607269828674858" +
                ", bank_type=CFT, openid=oR3ufwTYMTdUQnTDv-415U-iOUME, #text=\n" +
                ", device_info=WEB, out_trade_no=16562920160726158, appid=wxc2b584fcefc93605, trade_type=JSAPI, result_code=SUCCESS, is_subscribe=Y, return_code=SUCCESS";
        Matcher matcher2 = Pattern.compile("(\\S*?)=(\\S*?),").matcher(wx);
        Map<String, String> requestParams2 = new HashMap<>();

        while (matcher2.find()) {
            System.out.println(matcher2.group(1) + "=" + matcher2.group(2));
            requestParams2.put(matcher2.group(1), matcher2.group(2));
        }

        System.out.println(WxUtil.verify(requestParams2, "njfzcrjyxgs201606292120101234567"));

        Element element = XmlUtils.getRootElementFromString("<xml>" +
                "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<return_msg><![CDATA[OK]]></return_msg>\n" +
                "<appid><![CDATA[wxc2b584fcefc93605]]></appid>\n" +
                "<mch_id><![CDATA[1360170902]]></mch_id>\n" +
                "<device_info><![CDATA[WEB]]></device_info>\n" +
                "<nonce_str><![CDATA[bVVikdKpyLcSqbBM]]></nonce_str>\n" +
                "<sign><![CDATA[F63731516C564ED20F1A712135B5F8A1]]></sign>\n" +
                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "<prepay_id><![CDATA[wx201607261656302c9d90f22a0672340026]]></prepay_id>\n" +
                "<trade_type><![CDATA[JSAPI]]></trade_type>\n" +
                "</xml>");
        requestParams2 = new HashMap<>();
        for (int j=0;j<element.getChildNodes().getLength();j++){
            Node item = element.getChildNodes().item(j);

            System.out.println(item.getNodeType() + ":" + item.getNodeName() + "=" + XmlUtils.getElementValue(element, item.getNodeName()));
            requestParams2.put(item.getNodeName(),XmlUtils.getElementValue(element, item.getNodeName()));
        }
        requestParams2.remove("#text");
        System.out.println(WxUtil.verify(requestParams2, "njfzcrjyxgs201606292120101234567"));

        String content = "payment_type=[1],subject=[test_pay],trade_no=[2016071321001004220250115216],buyer_email=[kkjattuu@163.com]" +
                ",gmt_create=[2016-07-13 15:11:16],notify_type=[trade_status_sync],quantity=[1]" +
                ",out_trade_no=[15105920160713999],seller_id=[2088421331911805],notify_time=[2016-07-13 15:11:17],body=[body_pay_test]" +
                ",trade_status=[TRADE_SUCCESS],is_total_fee_adjust=[N],total_fee=[0.01],gmt_payment=[2016-07-13 15:11:17]" +
                ",seller_email=[13735247@qq.com],price=[0.01],buyer_id=[2088102441037224]" +
                ",notify_id=[d9006a7d63b83140ce47b636c4a8dbfhp6],use_coupon=[N],sign_type=[RSA]" +
                ",sign=[dnC+GtmGQJQQ3HHOtvsyWQ3Bh1r6EEQstnn4iwnJgmhuB8wY45oSQZ6F3mxsh9b8LPKqoyoEn9xJGCh9UsKJSRX/KkNx9HZs6Ar0qbybMKrzxT6ajGcT1+86e7+iMp647D9cGqsBFRpgdVCao/jadE++dLXtb8LyuSAcbIPMorg=]";

        String sign = "dnC+GtmGQJQQ3HHOtvsyWQ3Bh1r6EEQstnn4iwnJgmhuB8wY45oSQZ6F3mxsh9b8LPKqoyoEn9xJGCh9UsKJSRX/KkNx9HZs6Ar0qbybMKrzxT6ajGcT1+86e7+iMp647D9cGqsBFRpgdVCao/jadE++dLXtb8LyuSAcbIPMorg=";

        content = "payment_type=[1],subject=[南京公共],trade_no=[2016071321001004220253424145],buyer_email=[kkjattuu@163.com],gmt_create=[2016-07-13 14:55:59],notify_type=[trade_status_sync],quantity=[1],out_trade_no=[14554320160713783],seller_id=[2088421331911805],notify_time=[2016-07-13 14:56:00],body=[body_pay_test],trade_status=[TRADE_SUCCESS],is_total_fee_adjust=[N],total_fee=[0.01],gmt_payment=[2016-07-13 14:56:00],seller_email=[13735247@qq.com],price=[0.01],buyer_id=[2088102441037224],notify_id=[e14f4fe17f4f7de1cf35f37b938c264hp6],use_coupon=[N],sign_type=[RSA]" +
                ",sign=[UWwuDjd66P3FN5wkQyk3I0ubpqIkuIJQLV8fPM8Jf9rRsO19sG0c/ZPNEB1YeBNkVd8X+uR7xznjmFV" +
                "HjgMDbD8vEus4l0HyEDVFKlUWAP23VvioAGiOaO+m0mBa5sep1nAKSGEnqpmCGx+oow2UjpFDHVccn+jxeS/stxsgfPM=],";

        sign = "UWwuDjd66P3FN5wkQyk3I0ubpqIkuIJQLV8fPM8Jf9rRsO19sG0c/ZPNEB1YeBNkVd8X+uR7xznjmFVHjgMDbD8vEus4l0HyEDVFKlUWAP23VvioAGiOaO+m0mBa5sep1nAKSGEnqpmCGx+oow2UjpFDHVccn+jxeS/stxsgfPM=";
        Matcher matcher = Pattern.compile("(\\S*?)=\\[(.*?)\\],").matcher(content);
        Map<String, String[]> requestParams = new HashMap<>();

        while (matcher.find()) {
            System.out.println(matcher.group(1) + "=" + matcher.group(2));
            requestParams.put(matcher.group(1), new String[]{matcher.group(2)});
        }
        requestParams.put("sign", new String[]{sign});
        Map<String, String> params = new HashMap<>();
        Iterator iterator = requestParams.keySet().iterator();
        StringBuffer value = new StringBuffer();
        String tmpValue;
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            for (int i = 0; i < values.length; i++) {
                value.append(values[i]);
                if (i < values.length - 1) {
                    value.append(",");
                }
            }
            tmpValue = value.toString();
            tmpValue = new String(tmpValue.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, tmpValue);
            value.setLength(0);
        }

        //System.out.println(AlipaySignature.rsaCheckV1(params, Alipayut.aliPayPublicKey, AlipayConstants.CHARSET_UTF8));
    }
}
