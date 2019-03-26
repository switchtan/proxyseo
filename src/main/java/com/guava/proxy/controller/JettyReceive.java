package com.guava.proxy.controller;


import com.guava.proxy.Util.HttpHelper;
import com.guava.proxy.Util.Redis.RedisPoolUtil;
import com.guava.proxy.Util.SettingHelper;
import com.guava.proxy.regex.GetCss;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
@Component
public class JettyReceive extends AbstractHandler {
    private RedisPoolUtil cssCache=null;

    private SettingHelper settingHelper=new SettingHelper();
    JettyReceive(){
        if(cssCache==null) {
            System.out.println("redis 新建");
             cssCache = new RedisPoolUtil();
        }
    }

    @Override
    public void handle(String url, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String baseUrl_noroot=settingHelper.get("localhost");
        System.out.println(request.getHeader("Host"));
        System.out.println(baseUrl_noroot);
        String baseUrl=baseUrl_noroot+"/";
        if(url.indexOf(baseUrl_noroot)>-1){
            url=url.replace(baseUrl_noroot,"");
        }
        System.out.println("--------------------");
        boolean isReturned=false;
        System.out.println("get a request2:"+url);

//        Pattern p = Pattern.compile(".*\\.css");
//        Matcher m=p.matcher(url);
        //if(m.find()){

        if(url.indexOf(".css")>-1){
            System.out.println("found css:"+url);
            response.setContentType("text/css;charset=UTF-8");
            url=url.replace("/","");
            String csscode=cssCache.get(url);
            if (csscode==null) {
                response.getWriter().println(".guava{}");
            } else {
                response.getWriter().println(csscode);
            }
            baseRequest.setHandled(true);
            isReturned=true;

        }
        if(url.indexOf(".js")>-1 || url.indexOf(".png")>-1
                || url.indexOf(".php")>-1
                || url.indexOf(".jpg")>-1
                || url.indexOf(".gif")>-1 ){
            System.out.println("found js png php jpg");
            response.setContentType("application/javascript;charset=UTF-8");
            response.getWriter().println("var guava=0;");
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


            //response.setContentType("application/json");
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            HttpHelper httpHelper = new HttpHelper();
            try {
                System.out.println(baseUrl_noroot+url);
                System.out.println("begin to get cache");
                String html =cssCache.get(baseUrl_noroot+url);

                if(html!=null){

                    System.out.println("cache state:this html in cache");
                }else{
                    System.out.println("cache state:this html not in cache1");
                    System.out.println(html);
                    System.out.println("cache state:this html not in cache2");
                    printHeader(url,request,response);
                    html = httpHelper.sentGet(baseUrl_noroot+url);
                    GetCss getCss=new GetCss(cssCache);
                    html=getCss.getC(html,baseUrl_noroot);
                    html=html.replace(baseUrl_noroot,"");
                    String seted=cssCache.set(baseUrl_noroot+url,html);
                    System.out.println("cache html file:"+baseUrl_noroot+url);
                    System.out.println(seted);
                }


                response.getWriter().println(html);

                //response.flushBuffer();
                baseRequest.setHandled(true);
            } catch (Exception e) {
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
    private void printHeader(String url, HttpServletRequest request, HttpServletResponse response){

        System.out.println("url:" + url);
        //System.out.println("domain name:" + request.getHeader("From"));
        System.out.println("host name:" + request.getHeader("Host"));
        System.out.println("host name:" + response.getHeader("Location"));
        System.out.println("ip:" + getIpAdrress(request));
        //System.out.println("body:" + getBodyDate(request));
    }



}
