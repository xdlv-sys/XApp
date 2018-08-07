package xd.dl.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.HttpClientTpl;
import xd.fw.action.BaseAction;

import static xd.dl.action.DLBaseAction._convertCharset;
import static xd.dl.action.DLBaseAction.convertUtf8;

@Action("/cloud")
public class CloudAction extends BaseAction {
    //Logger logger = LoggerFactory.getLogger(CloudAction.class);
    @Value("${center_url:http://p.hjcpay.com/}")
    String centerUrl;
    String url;
    String json;

    Object ret;

    @Value("${park_id}")
    String parkId;

    @Value("${cloud_charset:gb2312}")
    String charset;

    @Action("json")
    public String cloud() throws Exception{
        JSONObject jsonObj = JSONObject.fromObject(_convertCharset(json, charset, "utf8"));
        jsonObj.put("parkId", parkId);

        String json = HttpClientTpl.postJson(centerUrl + url, jsonObj.toString(), null);
        log.info("json: {}", json);
        if (json.startsWith("[")) {
            ret = JSONArray.fromObject(json);
        } else {
            ret = JSONObject.fromObject(json);
            if (((JSONObject) ret).has("success") && !((JSONObject) ret).getBoolean("success")){
                success = false;
            }
        }
        ret = ret.toString();
        return SUCCESS;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Object getRet() {
        return ret;
    }
}
