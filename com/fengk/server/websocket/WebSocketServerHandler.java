package com.fengk.server.websocket;

/**
 * An instant message server which is using WebSocket protocol by Jetty 9
 * @author fengk
 */

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.FutureCallback;
import org.eclipse.jetty.websocket.core.api.WebSocketConnection;
import org.eclipse.jetty.websocket.core.api.WebSocketException;
import org.eclipse.jetty.websocket.core.api.WebSocketListener;

import com.fengk.constants.Constants;
import com.fengk.message.InstantMessage;
import com.fengk.message.ReturnMessageList;

public class WebSocketServerHandler implements WebSocketListener {
    private String type;
    private WebSocketConnection conn;
    
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int length) {
        /**
         * The binary messages
         */
        System.out.println("Get binary message: " + payload);
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        WebSocketConnectionMap.removeConnection(type);
        System.out.println("WebSocket close status code: " + statusCode);
        
    }
    
    @Override
    public void onWebSocketConnect(WebSocketConnection connection) {
        this.conn = connection;
        JettyWebSocketServer.totalReadHttpNum += 1;
    }
    
    @Override
    public void onWebSocketException(WebSocketException error) {
        error.printStackTrace();
    }
    
    @Override
    public void onWebSocketText(String message) {
        if (conn == null) {
            return;
        }
        
        if (Constants.WEBSOCKET_CLIENT_MESSAGE.toLowerCase().equals(message)) {
            /**
             * Initialization message coming from browser
             */
            if (!WebSocketConnectionMap.containsKey(Constants.WEBSOCKET_CLIENT_NAME)) {
                WebSocketConnectionMap.addConnection(Constants.WEBSOCKET_CLIENT_NAME, this.conn);
            }
            
            /**
             * Return the stored messages
             */
            ReturnMessageList messageList = new ReturnMessageList();
            if (JettyWebSocketServer.messageQueue.hasNewMessage()) {
                List<InstantMessage> unreadMessages = JettyWebSocketServer.messageQueue.deleteQueue();
                String timeNow = String.valueOf(new Date().getTime());
                for (InstantMessage instantMessage : unreadMessages) {
                    instantMessage.setClientGetDate(timeNow);
                    long delay = Long.valueOf(timeNow) - Long.valueOf(instantMessage.getSendDate());
                    JettyWebSocketServer.logger.writeLine(instantMessage.getId(), delay, JettyWebSocketServer.totalReadHttpNum);
                }
                messageList.setMessageList(unreadMessages);
            }
            
            String jsonString = messageList.toJsonString();
            
            try {
                String context = null;
                Callback<String> callback = new FutureCallback<String>();
                this.conn.write(context, callback, jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.err.println("Get wrong message from WebSocket client: " + message);
        }
    }

}
