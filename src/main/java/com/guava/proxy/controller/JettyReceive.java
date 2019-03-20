package com.guava.proxy.controller;


import com.guava.proxy.Util.HttpHelper;
import com.guava.proxy.regex.GetCss;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class JettyReceive extends AbstractHandler {

    @Override
    public void handle(String url, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        boolean isReturned=false;
        Pattern p = Pattern.compile("\\.css$");
        Matcher m=p.matcher(url);
        if(m.find()){
            response.getWriter().println(".guava{}");
            //response.flushBuffer();
            baseRequest.setHandled(true);
            isReturned=true;
        }

        if(isReturned==false) {
            printHeader(url,request);
            //response.setContentType("application/json");
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            HttpHelper httpHelper = new HttpHelper();
            try {
                String html = httpHelper.sentGet("https://www.36kr.com/");
                GetCss getCss=new GetCss();
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

}
