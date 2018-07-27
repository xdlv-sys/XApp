package xd.dl.action;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.HttpClientTpl;
import xd.fw.action.BaseAction;
@Action("/cloud")
public class CloudAction extends BaseAction {
    @Value("${center_url:http://p.hjcpay.com/}")
    String centerUrl;
    String url;
    String json;

    String ret;

    @Action("cloud")
    public String cloud() throws Exception{
        ret = HttpClientTpl.postJson(centerUrl + url, json, null);
        return SUCCESS;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getRet() {
        return ret;
    }
}
