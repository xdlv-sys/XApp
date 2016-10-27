package xd.dl.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.DlConst;
import xd.dl.bean.ParkInfo;

public class EntryAction extends ParkBaseAction implements DlConst {

    @Value("${redirect_url}")
    String redirectUrl;

    String parkId;
    String watchId;
    ParkInfo parkInfo;

    public String execute() throws Exception {
        parkInfo = parkService.get(ParkInfo.class,parkId);

        if (weixinBroswer()) {
            return "weixin";
        }
        setRetAttribute("parkId", parkId);
        setRetAttribute("watchId", watchId);
        setRetAttribute("parkName", parkInfo.getParkName());
        return INDEX;
    }

    public String getWeixinUrl() {
        return String.format("https://open.weixin.qq.com/connect/oauth2" +
                "/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base" +
                "&state=%s#wechat_redirect", parkInfo.getAppId(), redirectUrl
                , parkId + (StringUtils.isNotBlank(watchId) ? "-" + watchId : ""));

    }

    /*public String getAlipayUrl() {
        return String.format("https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=%s&redirect_uri=%s");
    }*/

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public void setWatchId(String watchId) {
        this.watchId = watchId;
    }

    public void setP(String p) {
        //001,0001
        String[] ps = p.split("-");
        setParkId(ps[0]);
        if (ps.length > 1){
            setWatchId(ps[1]);
        }
    }

    public static void main(String[] args){
        System.out.println("001".split("-")[0]);
    }
}
