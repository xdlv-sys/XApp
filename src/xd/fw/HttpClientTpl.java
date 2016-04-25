package xd.fw;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpClientTpl {
    public static interface Processor {
        Object process(HttpEntity entity) throws Exception;
    }
    static class StringProcessor implements Processor{
        @Override
        public Object process(HttpEntity entity) throws Exception {
            return EntityUtils.toString(entity);
        }
    }
    static StringProcessor stringProcessor = new StringProcessor();

    public static String postJson(String url,String json) throws Exception{
        return (String)execute(url,true,null,json,stringProcessor);
    }
    public static String post(String url, String[][] params) throws Exception {
        return (String)execute(url,true,params,null,stringProcessor);
    }

    public static String get(String url) throws Exception {
        return (String)execute(url,false,null,null, stringProcessor);
    }

    public static Object post(String url, String[][] params, Processor processor) throws Exception {
        return execute(url,true,params,null,processor);
    }

    public static Object get(String url, Processor processor) throws Exception {
        return execute(url,false,null,null, processor);
    }

    static Object execute(String url, boolean post,String[][] params,String json, Processor processor) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try{
            HttpUriRequest request;
            if (post){
                request = new HttpPost(url);
                if (params != null){
                    List <NameValuePair> nvps = new ArrayList <NameValuePair>();
                    for (String[] param : params){
                        nvps.add(new BasicNameValuePair(param[0], param[1]));
                    }

                    ((HttpPost)request).setEntity(new UrlEncodedFormEntity(nvps));
                }
                if (json != null){
                    StringEntity jsonEntity = new StringEntity(json, "UTF-8");   // 中文乱码在此解决
                    jsonEntity.setContentType("application/json");
                    ((HttpPost)request).setEntity(jsonEntity);
                }
            } else {
                request = new HttpGet(url);
            }
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode()
                    !=  HttpStatus.SC_OK){
                throw new Exception("http status is wrong:" + statusLine);
            }
            entity = response.getEntity();
            return processor.process(entity);
        } finally {
            if (entity != null){
                try{
                    EntityUtils.consume(entity);
                }catch (Exception e){}
            }
            if (response != null){
                try{
                    response.close();
                }catch (Exception e){}
            }
            httpclient.close();
        }
    }
    public static void main(String[] args) throws Exception {
        Object get = get("http://httpbin.org/get", new Processor() {
            @Override
            public Object process(HttpEntity entity) throws Exception {
                return EntityUtils.toString(entity);
            }
        });

        System.out.println(get);
        Object post = post("http://httpbin.org/post", new String[][]{
                {"name", "vip"},
                {"password", "secret"}
        }, new Processor() {
            public Object process(HttpEntity entity) throws Exception {
                return EntityUtils.toString(entity);
            }
        });
        System.out.println(post);
    }

}
