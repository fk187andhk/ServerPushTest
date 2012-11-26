package com.fengk.server.streaming;

/**
 * The control class of JettyStreamingServer
 * @author fengk
 */

public class StreamingServer extends Thread {

    private JettyStreamingServer streamingServer = null;
    
    public void stopServer() {
        if (streamingServer != null) {
            streamingServer.stopServer();
        }
    }
    
    @Override
    public void run() {
        streamingServer = new JettyStreamingServer();
        streamingServer.configure();
        streamingServer.startServer();
    }

}
