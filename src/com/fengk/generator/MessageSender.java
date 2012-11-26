package com.fengk.generator;

/**
 * Send new message to different servers by HTTP request
 * Try to execute multiple POST requests from different threads using a connection (still not implemented)
 * @author fengk
 */

import java.net.Socket;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;

import com.fengk.message.InstantMessage;

public class MessageSender {
    private String url;
    private String hostName;
    private int port;
    private HttpParams params;
    private HttpProcessor httpProcessor;
    
    public MessageSender(String url, String hostName, int port) {
        this.url = url;
        this.hostName = hostName;
        this.port = port;
        this.params = new SyncBasicHttpParams();
        
        /**
         * Using the HTTP 1.0, because we do not want to keep the connection alive
         */
        HttpProtocolParams.setVersion(this.params, HttpVersion.HTTP_1_0);
        HttpProtocolParams.setContentCharset(this.params, "UTF-8");
        HttpProtocolParams.setUserAgent(this.params, "MessageGenerator");
        HttpProtocolParams.setUseExpectContinue(this.params, true);
        
        this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
                // Required protocol interceptors
                new RequestContent(),
                new RequestTargetHost(),
                // Recommended protocol interceptors
                new RequestConnControl(),
                new RequestUserAgent(),
                new RequestExpectContinue()});
    }
    
    public void sendMessage(InstantMessage message) throws Exception {
        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
        HttpContext context = new BasicHttpContext(null);
        HttpHost host = new HttpHost(this.hostName, this.port);

        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
        
        try {
            HttpEntity requestBody = new StringEntity(message.toHttpPostString(), "UTF-8");
           
            if (!conn.isOpen()) {
                Socket socket = new Socket(host.getHostName(), host.getPort());
                conn.bind(socket, params);
            }
            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", url);
            request.setEntity(requestBody);
            // System.out.println(">> Request URI: " + request.getRequestLine().getUri());
            // System.out.println(">> HttpEntity: " + requestBody.getContentLength() + ": " + requestBody);
            
            request.setParams(params);
            httpexecutor.preProcess(request, httpProcessor, context);

            HttpResponse response = httpexecutor.execute(request, conn, context);
            response.setParams(params);
            httpexecutor.postProcess(response, httpProcessor, context);

            // System.out.println("<< Response: " + response.getStatusLine());
            
            if (!connStrategy.keepAlive(response, context)) {
                conn.close();
            } else {
                // System.out.println("Connection kept alive...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
