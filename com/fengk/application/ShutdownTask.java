package com.fengk.application;

/**
 * Used to shutdown the system when the time is OK
 * @author fengk
 */

import java.util.TimerTask;

import com.fengk.constants.Constants;
import com.fengk.server.streaming.StreamingServer;
import com.fengk.server.websocket.WebSocketServer;

public class ShutdownTask extends TimerTask {
    
    private String startTime = null;
    private Thread pollingThread = null;
    private Thread longPollingThread = null;
    private StreamingServer streamingThread = null;
    private WebSocketServer websocketThread = null;
    private Thread generatorThread = null;
    
    public ShutdownTask(String startTime) {
        super();
        this.startTime = startTime;
    }
    
    public void setPollingThread(Thread pollingThread) {
        this.pollingThread = pollingThread;
    }
    
    public void setLongPollingThread(Thread longPollingThread) {
        this.longPollingThread = longPollingThread;
    }
    
    public void setStreamingThread(StreamingServer streamingThread) {
        this.streamingThread = streamingThread;
    }
    
    public void setWebsocketThread(WebSocketServer websocketThread) {
        this.websocketThread = websocketThread;
    }
    
    public void setGeneratorThread(Thread generatorThread) {
        this.generatorThread = generatorThread;
    }
    
    @Override
    public void run() {
        System.out.println("--------------------------------------");
        try {
            pollingThread.interrupt();
            pollingThread = null;
            longPollingThread.interrupt();
            longPollingThread = null;
            streamingThread.stopServer();
            streamingThread = null;
            websocketThread.stopServer();
            websocketThread = null;
            generatorThread.interrupt();
            generatorThread = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (pollingThread != null) {
                try {
                    pollingThread.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (longPollingThread != null) {
                try {
                    longPollingThread.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (streamingThread != null) {
                streamingThread.stopServer();
            }
            if (websocketThread != null) {
                websocketThread.stopServer();
            }
            if (generatorThread != null) {
                try {
                    generatorThread.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Time to close the application!");
        System.out.println("Start Time: " + startTime);
        System.out.println("Running Time: " + Constants.SYSTEM_RUNNING_TIME + "mins");
        System.out.println("--------------------------------------");
    }
}
