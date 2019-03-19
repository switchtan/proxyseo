package com.guava.proxy.controller;

import org.eclipse.jetty.server.Server;

public class JettySetup {
    public void run(){
        try {
//            System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
//            System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
            Server server = new Server(12345);
            server.setHandler(new JettyReceive());
            server.start();
            server.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
