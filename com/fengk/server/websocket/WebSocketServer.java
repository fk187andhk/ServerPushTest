package com.fengk.server.websocket;

/**
 * Used to start the jetty server as a separate thread
 * @author fengk
 */

public class WebSocketServer extends Thread {
    private JettyWebSocketServer websocketServer = null;
    
    public void stopServer() {
        if (websocketServer != null) {
            websocketServer.stopServer();
        }
    }
    
    @Override
    public void run() {
        websocketServer = new JettyWebSocketServer();
        websocketServer.configure();
        websocketServer.startServer();
    }
}
