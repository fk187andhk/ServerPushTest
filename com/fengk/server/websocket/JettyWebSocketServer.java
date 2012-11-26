package com.fengk.server.websocket;

/**
 * The servlet container for WebSocketServer which has implemented the WebSocketServlet
 * @author fengk
 */

import java.util.Date;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.fengk.constants.Constants;
import com.fengk.message.ResultRecorder;

public class JettyWebSocketServer {
    private Server server;
    
    public static WebSocketServerQueue messageQueue = new WebSocketServerQueue();
    
    public static int totalReadHttpNum = 0;
    
    public static final ResultRecorder logger = 
            new ResultRecorder("websocket_" + new Date().getTime() + ".csv");
    
    public JettyWebSocketServer() {
        server = new Server(Constants.WEBSOCKET_SERVER_PORT);
    }
    
    public void configure() {
        /**
         * Because the Jetty server just has one handler one time
         * So you should use one handler with many different servlets
         */
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase(Constants.SERVER_ROOT_DIRECTORY);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        /**
         * Set the static resource handler by the context
         */
        context.addServlet(
                new ServletHolder(new DefaultServlet()), "/");
        /**
         * Set the read message servlet
         */
        context.addServlet(
                new ServletHolder(new WebSocketMessageServlet()), 
                Constants.WEBSOCKET_READ_MESSAGE);
        /**
         * Set the write message servlet
         */
        context.addServlet(
                new ServletHolder(new GeneratorMessageServlet()), 
                Constants.WEBSOCKET_WRITE_MESSAGE);
        
        server.setHandler(context);
    }
    
    public void startServer() {
        try {
            logger.initialize();
            System.out.println("WebSocket server listens on port " + Constants.WEBSOCKET_SERVER_PORT);
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stopServer() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
