package com.guava.proxy.controller;


import com.guava.proxy.Util.HttpHelper;
import com.guava.proxy.regex.GetCss;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
@Component
public class JettyReceive extends AbstractHandler {
    private CacheManager cm=null;
    private Cache<String, String> cssCache=null;
    JettyReceive(){
        if(cm==null) {
            cm = this.getCacheManager();
        }
        cssCache = cm.getCache("cssCache", String.class, String.class);

    }
    @Override
    public void handle(String url, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String baseUrl="https://www.36kr.com/";

        boolean isReturned=false;
        System.out.println("get a request2:"+url);

//        Pattern p = Pattern.compile(".*\\.css");
//        Matcher m=p.matcher(url);
        //if(m.find()){

        if(url.indexOf(".css")>-1){
            System.out.println("found css:"+url);
            response.setContentType("text/css;charset=UTF-8");
            url=url.replace("/","");
            if(cssCache.containsKey(url)){
                response.getWriter().println(cssCache.get(url));
            }else {
                response.getWriter().println(".guava{}");
            }
            baseRequest.setHandled(true);
            isReturned=true;

        }
        if(isReturned==false && url.indexOf(".ico")>-1){
            response.getWriter().println(".guava{}");
            baseRequest.setHandled(true);
            isReturned=true;
        }
        if(isReturned==false) {
            System.out.println("no css file");

            printHeader(url,request);
            //response.setContentType("application/json");
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            HttpHelper httpHelper = new HttpHelper();
            try {
                String html = httpHelper.sentGet("https://www.36kr.com/");
                GetCss getCss=new GetCss(cssCache);
                html=getCss.getC(html);

                response.getWriter().println(html);

                //response.flushBuffer();
                baseRequest.setHandled(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // 从请求中获取数据
    public String getBodyDate(HttpServletRequest request) {
        String bodyStr =null;
        BufferedReader br=null ;

        try {
            // 设置编码
            request.setCharacterEncoding("UTF-8");
            // 创建输入流
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            // 获取body内容
            String line = "";
            StringBuffer buf = new StringBuffer();
            while ((line = br.readLine()) !=null) {
                buf.append(line);
            }
            bodyStr = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bodyStr;
    }

    /**
     * 获取request的客户端IP地址
     *
     * @param request
     * @return
     */
    private static String getIpAdrress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip ==null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip ==null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip ==null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip ==null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip ==null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    private void printHeader(String url,HttpServletRequest request){
        System.out.println("url:" + url);
        System.out.println("domain name:" + request.getHeader("From"));
        System.out.println("host name:" + request.getHeader("Host"));
        System.out.println("ip:" + getIpAdrress(request));
        //System.out.println("body:" + getBodyDate(request));
    }

    private CacheManager getCacheManager(){
        PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(new File( "myData")))
                .withCache("cssCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .heap(10, EntryUnit.ENTRIES)
                                        .offheap(1, MemoryUnit.MB)
                                        .disk(20, MemoryUnit.MB, true)
                        )
                ).build(true);
        return persistentCacheManager;
    }


}
