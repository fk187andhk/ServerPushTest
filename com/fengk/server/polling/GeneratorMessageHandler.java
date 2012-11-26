package com.fengk.server.polling;

/**
 * Class used to handle the write message request from the message generator
 * @author fengk
 */

import java.io.IOException;
import java.util.Locale;
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
import com.fengk.message.ParsePostStringEntity;

public class GeneratorMessageHandler implements HttpRequestHandler {
    private PollingServerQueue queue;
    
    public GeneratorMessageHandler(PollingServerQueue queue) {
        super();
        this.queue = queue;
    }
    
    public void handle(HttpRequest request, HttpResponse response, HttpContext context)
        throws IOException, HttpException {

        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }
        // String target = URLDecoder.decode(request.getRequestLine().getUri(), "UTF-8");
        // System.out.println("The target URL is " + target);
        
        /**
         * If the request has entities, maybe it is a POST request
         */
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            String entityString = EntityUtils.toString(entity);
            // System.out.println("Incoming entity content (string): " + entityString);
            
            InstantMessage message = ParsePostStringEntity.parseStringToInstantMessage(entityString);
            message.setSendDate(String.valueOf(new Date().getTime()));
            queue.enterQueue(message);
            
            response.setStatusCode(HttpStatus.SC_OK);
            StringEntity responseEntity = new StringEntity(Constants.OPERATION_SUCCESS, ContentType.create("text/html", "UTF-8"));
            response.setEntity(responseEntity);
        }
        else {
            response.setStatusCode(HttpStatus.SC_OK);
            StringEntity responseEntity = new StringEntity(Constants.OPERATION_FAILED, ContentType.create("text/html", "UTF-8"));
            response.setEntity(responseEntity);
            System.err.println("Get wrong method request: " + request);
        }
    }
}
