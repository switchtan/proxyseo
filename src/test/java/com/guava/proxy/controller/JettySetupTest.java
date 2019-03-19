package com.guava.proxy.controller;

import org.eclipse.jetty.util.log.Log;
import org.junit.Test;

import static org.junit.Assert.*;

public class JettySetupTest {

    @Test
    public void run() {
        Log.getProperties().setProperty("org.eclipse.jetty.util.log.announce", "false");
        JettySetup jettySetup = new JettySetup();
        jettySetup.run();
    }
}