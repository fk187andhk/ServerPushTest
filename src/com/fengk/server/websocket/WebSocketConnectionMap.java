package com.fengk.server.websocket;

/**
 * Used to store all the WebSocket connection handler
 * @author fengk
 */

import java.util.Map;
import java.util.HashMap;

import org.eclipse.jetty.websocket.core.api.WebSocketConnection;

public class WebSocketConnectionMap {
    
    private static Map<String, WebSocketConnection> connectionMap = new HashMap<String, WebSocketConnection>();
    
    public static void addConnection(String type, WebSocketConnection conn) {
        synchronized (connectionMap) {
            connectionMap.put(type, conn);
        }
    }
    
    public static void removeConnection(String type) {
        synchronized (connectionMap) {
            connectionMap.remove(type);
        }
    }
    
    public static boolean containsKey(String type) {
        return connectionMap.containsKey(type);
    }
    
    public static boolean containsValue(WebSocketConnection conn) {
        return connectionMap.containsValue(conn);
    }
    
    public static WebSocketConnection getConnection(String type) {
        return connectionMap.get(type);
    }
}
