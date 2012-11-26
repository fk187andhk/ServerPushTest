package com.fengk.generator;

/**
 * Used to generate messages randomly in order to send to different servers
 * Wants to generate messages (random number) by Poisson Distribution
 * @author fengk
 */

import java.util.Date;
import java.util.Random;

import com.fengk.constants.Constants;
import com.fengk.message.InstantMessage;
import com.fengk.message.ResultRecorder;

public class MessageGenerator implements Runnable {
    private long messageNum;
    private String[] serverHostList;
    private String[] clientNameList;
    private String[] serverUrlList;
    private int[] serverPortList;
    
    public static final ResultRecorder logger = 
            new ResultRecorder("generator_" + new Date().getTime() + ".csv");
    
    public MessageGenerator() {
        messageNum = 0;
        serverHostList  = new String[Constants.DEFAULT_SERVER_NUM];
        clientNameList  = new String[Constants.DEFAULT_SERVER_NUM];
        serverUrlList   = new String[Constants.DEFAULT_SERVER_NUM];
        serverPortList  = new int[Constants.DEFAULT_SERVER_NUM];
        
        for (int i = 0; i < Constants.DEFAULT_SERVER_NUM; i++) {
            serverHostList[i] = Constants.SERVER_HOST;
        }
        
        clientNameList[0]   = Constants.POLLING_CLIENT_NAME;
        clientNameList[1]   = Constants.LONG_POLLING_CLIENT_NAME;
        clientNameList[2]   = Constants.STREAMING_CLIENT_NAME;
        clientNameList[3]   = Constants.WEBSOCKET_CLIENT_NAME;
        
        serverUrlList[0]    = Constants.POLLING_WRITE_MESSAGE;
        serverUrlList[1]    = Constants.LONG_POLLING_WRITE_MESSAGE;
        serverUrlList[2]    = Constants.STREAMING_WRITE_MESSAGE;
        serverUrlList[3]    = Constants.WEBSOCKET_WRITE_MESSAGE;
        
        serverPortList[0]   = Constants.POLLING_SERVER_PORT;
        serverPortList[1]   = Constants.LONG_POLLING_SERVER_PORT;
        serverPortList[2]   = Constants.STREAMING_SERVER_PORT;
        serverPortList[3]   = Constants.WEBSOCKET_SERVER_PORT;
    }
    
    @Override
    public void run() {
        System.out.println("Message Generator starts working");
        System.out.println("--------------------------------");
        logger.initialize();
        
        while (!Thread.interrupted()) {
            
            messageNum++;
            InstantMessage message = new InstantMessage();
            message.setId(messageNum);
            message.setFromId("Message Generator Robot");
            message.setSendDate(String.valueOf(new Date().getTime()));
            message.setContent(generateMessageContent());
            //System.out.println("The generator message content length: " + message.getContentLength());
            /**
             * Sends message to every server
             */
            for (int i = 0; i < Constants.DEFAULT_SERVER_NUM; i++) {
                message.setToId(clientNameList[i]);
                MessageSender sender = new MessageSender(serverUrlList[i], serverHostList[i], serverPortList[i]);
                try {
                    sender.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            double lamda = Integer.valueOf(Constants.DEFAULT_POISSON_LAMDA).doubleValue();
            long sleepTime = Double.valueOf(poissonRandom(lamda)).longValue();
            /**
             * Record the sleep time, judge whether it is a Poisson Distribution
             */
            logger.writeLine(messageNum, sleepTime);
            
            try {
                Thread.sleep(sleepTime * 1000);
                System.out.println(messageNum + ") sleep time: " + sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String generateMessageContent() {
        int length = Constants.DEFAULT_MESSAGE_CONTENT.length;
        int seq = Math.abs(new Random().nextInt()) % length;
        return Constants.DEFAULT_MESSAGE_CONTENT[seq];
    }
    
    private double poissonRandom(double lamda) {
        double x = 0, b = 1, c = Math.exp(-lamda), u = 0;
        do {
            u = Math.random();
            b *= u;
            if (b >= c) {
                x++;
            }
        } while (b >= c);
        return x;
    }
}
