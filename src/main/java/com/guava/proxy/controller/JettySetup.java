package com.guava.proxy.controller;

import org.eclipse.jetty.server.Server;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
@Component
public class JettySetup {
    public void run(){
        try {
//            System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
//            System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
            setLogProperty();
            Server server = new Server(12345);
            server.setHandler(new JettyReceive());
            server.start();
            server.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setLogProperty(){
        java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(Level.WARNING);
        java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(Level.WARNING);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
    }
}
