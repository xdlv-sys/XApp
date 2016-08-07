package xd.fw;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpClientTpl {

    static Logger logger = Logger.getLogger(HttpClientTpl.class);
    public static final String UTF8 = "UTF-8";
    static CloseableHttpClient httpclient = createHttpClient();

    private static CloseableHttpClient createHttpClient() {
        HttpClientConnectionManager manager = (HttpClientConnectionManager) FwUtil.getBean("manager");
        if (manager == null){
            PoolingHttpClientConnectionManager m = new PoolingHttpClientConnectionManager();
            m.setMaxTotal(100);
            m.setDefaultMaxPerRoute(50);
            manager = m;
        }
        return HttpClients.custom().setConnectionManager(
                manager
        ).build();
    }


    public interface Processor {
        Object process(HttpEntity entity) throws Exception;
    }

    final static Processor stringProcessor = (entity) -> EntityUtils.toString(entity, Consts.UTF_8);

    public static String postJson(String url, String json) throws Exception {
        return (String) execute(url, true, null, json, stringProcessor);
    }

    public static String post(String url, String[][] params) throws Exception {
        return (String) execute(url, true, params, null, stringProcessor);
    }

    public static String get(String url) throws Exception {
        return (String) execute(url, false, null, null, stringProcessor);
    }

    public static Object post(String url, String[][] params, Processor processor) throws Exception {
        return execute(url, true, params, null, processor);
    }

    public static Object get(String url, Processor processor) throws Exception {
        return execute(url, false, null, null, processor);
    }

    static Object execute(String url, boolean post, String[][] params, String json, Processor processor) throws Exception {

        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            HttpUriRequest request;
            logger.debug(url);
            if (post) {
                request = new HttpPost(url);
                //request.addHeader("Content-Type","text/html;charset=" + UTF8);
                if (params != null) {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    for (String[] param : params) {
                        nvps.add(new BasicNameValuePair(param[0], param[1]));
                    }

                    ((HttpPost) request).setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
                }
                if (json != null) {
                    StringEntity jsonEntity = new StringEntity(json, Consts.UTF_8);   // 中文乱码在此解决
                    jsonEntity.setContentType("application/json");
                    ((HttpPost) request).setEntity(jsonEntity);
                }
            } else {
                request = new HttpGet(url);
            }
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode()
                    != HttpStatus.SC_OK) {
                throw new Exception("http status is wrong:" + statusLine);
            }
            entity = response.getEntity();
            Object res = processor.process(entity);
            logger.debug(res);
            return res;
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (Exception e) {
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
            //httpclient.close();
        }
    }

    public void test(String args) throws Exception {
        Object get = get("http://httpbin.org/get");

        System.out.println(get);
        Object post = post("http://httpbin.org/post", new String[][]{
                {"name", "vip"},
                {"password", "secret"}
        }, entity -> EntityUtils.toString(entity));

        post("http://localhost:8080/accept!accept.cmd", new String[][]{
                {"Carnumber", "中国人民123"}
        });
        System.out.println(post);
    }

}
