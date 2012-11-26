package com.fengk.server.streaming;

/**
 * Class used to handle the write message request from the message generator
 * @author fengk
 */
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fengk.constants.Constants;
import com.fengk.message.InstantMessage;
import com.fengk.message.ParsePostStringEntity;

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
        System.err.println("StreamingServer: Get wrong HTTP method GET from: " + request.getRemoteHost());
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
        //System.out.println("Streaming: the generator message content length: " + message.getContentLength());
        
        JettyStreamingServer.messageQueue.enterQueue(message);
    }
}
