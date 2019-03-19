package com.guava.proxy.setup;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppSetup {
    public static void main(String[] args) {
        System.out.println("guava1.0");
        AnnotationConfigApplicationContext context = null;

        try {
            context = new AnnotationConfigApplicationContext(AppConfig.class);
            //IndexPage application = context.getBean(IndexPage.class);
            //application.getRules("http://www.bismara22.com/getSiteRule.php");

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}
