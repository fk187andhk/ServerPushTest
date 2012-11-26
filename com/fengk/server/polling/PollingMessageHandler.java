package com.fengk.server.polling;

/**
 * Class used to handle the read message request from HTTP client like browser
 * @author fengk
 */

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.List;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

import com.fengk.constants.Constants;
import com.fengk.message.InstantMessage;
import com.fengk.message.ReturnMessageList;

public class PollingMessageHandler implements HttpRequestHandler {
    
    private PollingServerQueue queue;
    
    public PollingMessageHandler(PollingServerQueue queue) {
        super();
        this.queue = queue;
    }
    
    public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) 
        throws HttpException, IOException {
        
        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }
        String target = URLDecoder.decode(request.getRequestLine().getUri(), "UTF-8");
        // System.out.println("The target URL is " + target);
        
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            byte[] entityContent = EntityUtils.toByteArray(entity);
            System.out.println("Incoming entity content (bytes): " + entityContent.length);
        }
        
        if (target.equals(Constants.POLLING_READ_MESSAGE)) {
            /**
             * HTTP client or browser wants to read message
             */
            PollingServer.totalReadHttpNum += 1;
            response.setStatusCode(HttpStatus.SC_OK);
            
            ReturnMessageList messageList = new ReturnMessageList();
            List<InstantMessage> unreadMessages = queue.deleteQueue();
            String timeNow = String.valueOf(new Date().getTime());
            for (InstantMessage message : unreadMessages) {
                message.setClientGetDate(timeNow);
                long delay = Long.valueOf(timeNow) - Long.valueOf(message.getSendDate());
                PollingServer.logger.writeLine(message.getId(), delay, PollingServer.totalReadHttpNum);
            }
            messageList.setMessageList(unreadMessages);
            String jsonString = messageList.toJsonString();
            // System.out.println("Return JSON String: " + jsonString);
            
            StringEntity entity = new StringEntity(
                    jsonString,
                    ContentType.create("text/html", "UTF-8"));
            response.setEntity(entity);
        }
        else {
            response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            System.err.println("Get wrong URL: " + target);
        }
    }
}
