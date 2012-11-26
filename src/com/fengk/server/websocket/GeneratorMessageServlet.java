package com.fengk.server.websocket;

/**
 * Used to receive and store message from message generator
 * @author fengk
 */

import java.io.IOException;
import java.io.BufferedReader;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.FutureCallback;
import org.eclipse.jetty.websocket.core.api.WebSocketConnection;

import com.fengk.constants.Constants;
import com.fengk.message.InstantMessage;
import com.fengk.message.ParsePostStringEntity;
import com.fengk.message.ReturnMessageList;

public class GeneratorMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public GeneratorMessageServlet() {
        super();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(Constants.OPERATION_SUCCESS);
        System.err.println("Get wrong HTTP method GET from: " + request.getRemoteHost());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String postString;
        char[] messageBuffer = new char[Constants.DEFAULT_BUFFER_BYTE_LENGTH];
        try {
            /**
             * Get the HTTP Post string from the reader stream
             */
            BufferedReader reader = request.getReader();
            reader.read(messageBuffer);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        postString = String.valueOf(messageBuffer);
        InstantMessage message = ParsePostStringEntity.parseStringToInstantMessage(postString);
        message.setSendDate(String.valueOf(new Date().getTime()));
        
        //System.out.println("Get message from generator: " + postString);
        JettyWebSocketServer.messageQueue.enterQueue(message);
        
        /**
         * Return the stored messages
         */
        if (WebSocketConnectionMap.containsKey(Constants.WEBSOCKET_CLIENT_NAME)) {
            ReturnMessageList messageList = new ReturnMessageList();
            if (JettyWebSocketServer.messageQueue.hasNewMessage()) {
                List<InstantMessage> unreadMessages = JettyWebSocketServer.messageQueue.deleteQueue();
                String timeNow = String.valueOf(new Date().getTime());
                for (InstantMessage instantMessage : unreadMessages) {
                    instantMessage.setClientGetDate(timeNow);
                    long delay = Long.valueOf(timeNow) - Long.valueOf(instantMessage.getSendDate());
                    JettyWebSocketServer.logger.writeLine(instantMessage.getId(), delay);
                }
                messageList.setMessageList(unreadMessages);
            }
            
            String jsonString = messageList.toJsonString();
            
            try {
                String context = null;
                Callback<String> callback = new FutureCallback<String>();
                WebSocketConnection conn = WebSocketConnectionMap.getConnection(
                        Constants.WEBSOCKET_CLIENT_NAME);
                conn.write(context, callback, jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(Constants.OPERATION_SUCCESS);
    }
}
