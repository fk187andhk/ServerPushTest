package com.fengk.server;
/**
 * Used to download javascript files from root directory
 * @author fengk
 */

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.fengk.constants.Constants;

public class StaticResourceServer implements Runnable {
    private Server server;
    
    @Override
    public void run() {
        server = new Server(Constants.RESOURCE_SERVER_PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase(Constants.SCRIPT_RESOURCE_DIRECTORY);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.addServlet(new ServletHolder(new DefaultServlet()), "/*");
        server.setHandler(context);
        
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
