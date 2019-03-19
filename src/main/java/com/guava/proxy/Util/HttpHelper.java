package com.guava.proxy.Util;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

@Component
public class HttpHelper {
    private CloseableHttpClient     httpClient;
    private HttpClientContext       context;
    private CloseableHttpResponse   res;
    private CookieStore             cookieStore;
    public HttpHelper(){
        // 全局请求设置
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        // 创建cookie store的本地实例
        cookieStore = new BasicCookieStore();
        // 创建HttpClient上下文
        context = HttpClientContext.create();
        context.setCookieStore(cookieStore);

        // 创建一个HttpClient
        this.httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig)
                .setDefaultCookieStore(cookieStore).build();

        res = null;
    }
    public void setCookie(BasicCookieStore cookie){
        context.setCookieStore(cookie);
    }
    public String sentGet(String url) throws IOException, InterruptedException {
        Thread.sleep(3000);
        HttpGet get = new HttpGet(url);

        get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36");
        try {
            res = httpClient.execute(get, context);
            String contentLogin = EntityUtils.toString(res.getEntity());
            //System.out.println(contentLogin);
            res.close();
            return contentLogin;
        }catch (SocketTimeoutException e){
            e.printStackTrace();
            return "";
        }

        // 获取常用Cookie,包括_xsrf信息
        //System.out.println("访问知乎首页后的获取的常规Cookie:===============");
        /*for (Cookie c : cookieStore.getCookies()) {
            System.out.println(c.getName() + ": " + c.getValue());
        }*/



    }
    public void sentPost(String url,List<NameValuePair> valuePairs) throws IOException {
        //List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
        entity.setContentType("application/x-www-form-urlencoded");
        // 创建一个post请求
        HttpPost post = new HttpPost(url);
        // 注入post数据
        post.setEntity(entity);
        res = httpClient.execute(post, context);

        // 打印响应信息，查看是否登陆是否成功
        System.out.println("打印响应信息===========");
        String contentLogin = EntityUtils.toString(res.getEntity());
        System.out.println(contentLogin);
        res.close();

        //System.out.println("登陆成功后,新的Cookie:===============");
        /*for (Cookie c : context.getCookieStore().getCookies()) {
            System.out.println(c.getName() + ": " + c.getValue());
        }*/
    }
}
