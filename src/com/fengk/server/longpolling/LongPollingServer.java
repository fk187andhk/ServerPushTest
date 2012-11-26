package com.fengk.server.longpolling;

/**
 * The web server used for HTTP Long Polling Schema based on HttpCore
 * @author fengk
 */

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import com.fengk.constants.Constants;
import com.fengk.message.ResultRecorder;

public class LongPollingServer implements Runnable {
    private LongPollingServerQueue messageQueue;
    private ServerSocket serverSocket;
    private HttpParams params;
    private HttpService httpService;
    
    public static final ResultRecorder logger = 
            new ResultRecorder("longpolling_" + new Date().getTime() + ".csv");
    
    public LongPollingServer() {
        messageQueue = new LongPollingServerQueue();
        serverSocket = null;
        params = null;
        httpService = null;
    }
    
    @Override
    public void run() {
        try {
            LongPollingServer.logger.initialize();
            this.serverSocket = new ServerSocket(Constants.LONG_POLLING_SERVER_PORT);
            this.params = new SyncBasicHttpParams();
            this.params
                .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 60000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");
            
            // Set up the HTTP protocol processor
            HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
                    new ResponseDate(),
                    new ResponseServer(),
                    new ResponseContent(),
                    new ResponseConnControl()
            });
            
            // Set up request handlers
            HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
            registry.register("/favicon.ico", new FileRequestHandler());
            registry.register(Constants.LONG_POLLING_INDEX_PAGE, new FileRequestHandler());
            registry.register(Constants.LONG_POLLING_READ_MESSAGE, new LongPollingMessageHandler(messageQueue));
            registry.register(Constants.LONG_POLLING_WRITE_MESSAGE, new GeneratorMessageHandler(messageQueue));
            
            // Set up the HTTP service
            this.httpService = new HttpService(
                    httpproc,
                    new DefaultConnectionReuseStrategy(),
                    new DefaultHttpResponseFactory(),
                    registry,
                    this.params);
            
            System.out.println("Long polling server listens on port " + this.serverSocket.getLocalPort());
            while (!Thread.interrupted()) {
                try {
                    // Set up HTTP connection
                    Socket socket = this.serverSocket.accept();
                    DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                    // System.out.println("Incoming connection from " + socket.getInetAddress());
                    conn.bind(socket, this.params);

                    // Start worker thread
                    Thread t = new WorkerThread(this.httpService, conn);
                    t.setDaemon(true);
                    t.start();
                } catch (InterruptedIOException ex) {
                    break;
                } catch (IOException e) {
                    System.err.println("I/O error initialising connection thread: " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Exception when server is starting: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public class WorkerThread extends Thread {
        private HttpService httpService;
        private HttpServerConnection conn;
        
        public WorkerThread(HttpService httpService, HttpServerConnection conn) {
            super();
            this.httpService = httpService;
            this.conn = conn;
        }
        
        @Override
        public void run() {
            // System.out.println("New connection thread");
            HttpContext context = new BasicHttpContext(null);
            try {
                this.httpService.handleRequest(this.conn, context);
            } catch (ConnectionClosedException ex) {
                System.err.println("Client closed connection");
            } catch (IOException ex) {
                System.err.println("I/O error: " + ex.getMessage());
                ex.printStackTrace(); 
            } catch (HttpException ex) {
                System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
                ex.printStackTrace(); 
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {}
            }
        }
    }
}
