package com.fengk.application;

/**
 * Start of web servers and message generator
 * @author fengk
 */

import java.util.Timer;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.fengk.constants.Constants;
import com.fengk.constants.ConstantsPrinter;
import com.fengk.constants.ConstantsReader;
import com.fengk.generator.MessageGenerator;
import com.fengk.server.polling.PollingServer;
import com.fengk.server.longpolling.LongPollingServer;
import com.fengk.server.streaming.StreamingServer;
import com.fengk.server.websocket.WebSocketServer;

public class ServerRunning {
    
    public static void main(String[] args) {
        
        System.out.println("Read Configuration");
        ConstantsReader reader = new ConstantsReader(Constants.CONFIG_FILE_PATH);
        reader.init();
        try {
            reader.readConfiguration();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Constants & Parameters");
        ConstantsPrinter.printClassField("com.fengk.constants.Constants");
        
        Thread pollingThread = new Thread(new PollingServer());
        pollingThread.setDaemon(false);
        pollingThread.start();
        
        Thread longPollingThread = new Thread(new LongPollingServer());
        longPollingThread.setDaemon(false);
        longPollingThread.start();
        
        StreamingServer streamingThread = new StreamingServer();
        streamingThread.setDaemon(false);
        streamingThread.start();
        
        WebSocketServer websocketThread = new WebSocketServer();
        websocketThread.setDaemon(false);
        websocketThread.start();
        
        /*
            Thread resourceThread = new Thread(new StaticResourceServer());
            resourceThread.setDaemon(false);
            resourceThread.start();
        */
        
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Thread generatorThread = new Thread(new MessageGenerator());
        generatorThread.setDaemon(false);
        generatorThread.start();
        
        if (Constants.SYSTEM_RUNNING_TIME != 0) {
            Date startDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = dateFormat.format(startDate);
            
            ShutdownTask task = new ShutdownTask(startTime);
            task.setGeneratorThread(generatorThread);
            task.setLongPollingThread(longPollingThread);
            task.setPollingThread(pollingThread);
            task.setStreamingThread(streamingThread);
            task.setWebsocketThread(websocketThread);
            
            Timer terminator = new Timer();
            terminator.schedule(
                    task, 
                    Constants.SYSTEM_RUNNING_TIME * 60 * 1000);
        }
    }
}
