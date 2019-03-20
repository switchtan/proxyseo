package com.guava.proxy.regex;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class GetCss {
    public String getC(String html){
//        Document doc= Jsoup.parse(html);
//        Elements e1=doc.select("head");
//        String headTagHtml=e1.html();
        String regex="(?<=href=\").*?\\.css";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(html);
        int i=1;
        while (matcher.find()) {
            System.out.println(matcher.start() + "-" + matcher.end());
            html=html.replace(matcher.group(),String.valueOf(i)+".css");
            i=i+1;
        }
        return html;
        //System.out.println(url.substring(45,128));
    }
}
