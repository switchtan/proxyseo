package com.guava.proxy.setup;

import com.guava.proxy.controller.JettySetup;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppSetup {
    public static void main(String[] args) {
        System.out.println("guava1.0");
        AnnotationConfigApplicationContext context = null;

        try {
            context = new AnnotationConfigApplicationContext(AppConfig.class);
            JettySetup application = context.getBean(JettySetup.class);
            application.run();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}
