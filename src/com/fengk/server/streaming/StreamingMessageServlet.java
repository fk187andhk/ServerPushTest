package com.fengk.server.streaming;

/**
 * Class used to handle the read message request from HTTP client like browser
 * @author fengk
 */

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fengk.constants.Constants;
import com.fengk.message.InstantMessage;
import com.fengk.message.ReturnMessageList;

public class StreamingMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public StreamingMessageServlet() {
        super();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        ReturnMessageList messageList = new ReturnMessageList();
        
        int waitingTime = 0;
        String jsonString, responseString;
        
        response.getWriter().print("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        
        while (waitingTime < Constants.DEFAULT_STREAMING_HOLD_TIME) {
            waitingTime++;
            /**
             * Judge whether there are new messages
             */
            if (JettyStreamingServer.messageQueue.hasNewMessage()) {
                List<InstantMessage> unreadMessages = JettyStreamingServer.messageQueue.deleteQueue();
                String timeNow = String.valueOf(new Date().getTime());
                for (InstantMessage message : unreadMessages) {
                    //System.out.println("Streaming: read message content " + message);
                    message.setClientGetDate(timeNow);
                    long delay = Long.valueOf(timeNow) - Long.valueOf(message.getSendDate());
                    JettyStreamingServer.logger.writeLine(message.getId(), delay);
                }
                messageList.setMessageList(unreadMessages);
            }
            
            jsonString = "\'" + messageList.toJsonString() + "\'";
            
            responseString = 
                    "<script type='text/javascript'>" + 
                    "window.parent.dealIframeMessage(" + 
                    jsonString + 
                    ")" + 
                    "</script>";
            
            response.getWriter().print(responseString);
            response.getWriter().flush();
            messageList.clearMessageList();
            
            try {
                Thread.sleep(Constants.DEFAULT_STREAMING_SLEEP_TIME * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        messageList.clearMessageList();
        messageList.setLast(true);
        jsonString = "\'" + messageList.toJsonString() + "\'";
        responseString = 
                "<script type='text/javascript'>" + 
                "window.parent.dealIframeMessage(" + 
                jsonString + 
                ");" + 
                "</script>";
        response.getWriter().print(responseString);
        response.getWriter().print("</head><body></body></html>");
        response.getWriter().flush();
        messageList.clearMessageList();
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        System.err.println("StreamingServer: Get wrong HTTP method POST from: " + request.getRemoteHost());
    }
}
