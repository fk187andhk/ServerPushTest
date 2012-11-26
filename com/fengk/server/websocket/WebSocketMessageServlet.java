package com.fengk.server.websocket;

/**
 * Used to configure the WebSocket server based on Jetty 9
 * @author fengk
 */

import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.eclipse.jetty.websocket.server.WebSocketServlet;

public class WebSocketMessageServlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public void configure(WebSocketServerFactory factory) {
        /**
         * Register a socket class as default
         */
        factory.register(WebSocketServerHandler.class);
    }
}
