package xd.fw;

import com.alipay.api.internal.util.XmlUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xd.dl.bean.PayOrder;
import xd.fw.bean.wx.UnifiedOrder;
import xd.fw.bean.wx.WxOrder;
import xd.fw.service.IConst;

import java.util.*;

/**
 * Created by xd on 2016/6/29.
 */
public class WxUtil {
    static Logger log = LoggerFactory.getLogger(WxUtil.class);
    final static String data = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String getRandomStringByLength(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(data.length());
            sb.append(data.charAt(number));
        }
        return sb.toString();
    }

    public static String getSign(Object o, String key) throws IllegalAccessException {
        List<String> list = new ArrayList<String>();
        invokeBeanFields(o, (f, v) -> {
            if (v != null && StringUtils.isNotBlank(v.toString())) {
                list.add(f.getName() + "=" + v + "&");
            }
        });
        return getSign(list, key);
    }

    public static boolean verify(Element rootElement, String key) {
        NodeList childNodes = rootElement.getChildNodes();
        Node node;
        Map<String, String> params = new HashMap<>(childNodes.getLength());
        for (int i = 0; i < childNodes.getLength(); i++) {
            node = childNodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                continue;
            }
            params.put(node.getNodeName(), XmlUtils.getElementValue(rootElement, node.getNodeName()));
        }
        return verify(params, key);
    }

    public static boolean verify(Map<String, String> params, String key) {
        List<String> list = new ArrayList<String>();
        String sign = null;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().equals("sign")) {
                sign = entry.getValue();
                continue;
            }
            if (StringUtils.isBlank(entry.getValue())) {
                continue;
            }
            list.add(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return getSign(list, key).equals(sign);
    }

    public static String getSign(List<String> params, String key) {
        int size = params.size();
        String[] arrayToSort = params.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        sb.append("key=").append(key);
        return MD5.MD5Encode(sb.toString()).toUpperCase();
    }

    public static String constructUnifiedOrderXml(Object object) throws IllegalAccessException {
        StringBuffer buffer = new StringBuffer(256);
        buffer.append("<xml>");

        invokeBeanFields(object, (f, v) -> {
            if (v != null && StringUtils.isNotBlank(v.toString())) {
                buffer.append("<").append(f.getName()).append(">"
                ).append(v).append("</").append(f.getName()).append(">");
            }
        });

        buffer.append("</xml>");
        return buffer.toString();
    }

    public static void invokeBeanFields(Object o, FwUtil.BeanFieldProcess p) {
        try {
            FwUtil.invokeBeanFields(o, p);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getOpenId(String appId, String secret, String code) throws Exception {
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s" +
                        "&secret=%s&code=%s&grant_type=authorization_code"
                , appId, secret, code);
        String retJson = HttpClientTpl.get(url);
        JSONObject json = JSONObject.fromObject(retJson);
        return (String) json.get("openid");
    }

    public static WxOrder unifiedOrder(String appId, String mchId, String wxKey, String openId, String body, String outTradeNo,
                                       float money, String notifyUrl, String limitPay) throws Exception {
        UnifiedOrder unifiedOrder = new UnifiedOrder();

        unifiedOrder.setAppid(appId);
        unifiedOrder.setMch_id(mchId);
        unifiedOrder.setNonce_str(WxUtil.getRandomStringByLength(16));
        unifiedOrder.setBody(body);
        unifiedOrder.setOut_trade_no(outTradeNo);
        unifiedOrder.setTotal_fee((int) (money * 100));
        unifiedOrder.setSpbill_create_ip(ServletActionContext.getRequest().getRemoteAddr());
        unifiedOrder.setNotify_url(notifyUrl);
        unifiedOrder.setLimit_pay(limitPay);
        unifiedOrder.setOpenid(openId);
        unifiedOrder.setSign(WxUtil.getSign(unifiedOrder, wxKey));

        String xml = WxUtil.constructUnifiedOrderXml(unifiedOrder);
        log.info("unifiedOrder request xml:" + xml);
        String retXml = HttpClientTpl.postJson("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
        log.info("unifiedOrder response xml:" + retXml);
        Element rootEle = XmlUtils.getRootElementFromString(retXml);
        String returnCode = XmlUtils.getElementValue(rootEle, "return_code");
        String resultCode = XmlUtils.getElementValue(rootEle, "result_code");
        String prePayId = XmlUtils.getElementValue(rootEle, "prepay_id");
        WxOrder wxOrder = null;
        if (IConst.SUCCESS_FLAG.equals(returnCode) && IConst.SUCCESS_FLAG.equals(resultCode)) {
            //wxOrder is used to launch wx pay
            wxOrder = new WxOrder();
            wxOrder.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
            wxOrder.setNonceStr(WxUtil.getRandomStringByLength(32));
            wxOrder.setAppId(appId);
            wxOrder.setPrePayId(prePayId);
            List<String> params = new ArrayList<>();
            params.add("appId=" + wxOrder.getAppId() + "&");
            params.add("timeStamp=" + wxOrder.getTimeStamp() + "&");
            params.add("nonceStr=" + wxOrder.getNonceStr() + "&");
            params.add("package=" + wxOrder.getPackage() + "&");
            params.add("signType=" + wxOrder.getSignType() + "&");

            wxOrder.setPaySign(WxUtil.getSign(params, wxKey));
        } else {
            log.warn("failed to create order, please check the reason");
        }
        return wxOrder;
    }
}
