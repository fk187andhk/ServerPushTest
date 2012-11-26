package com.fengk.constants;

import java.io.File;

/**
 * Used to store the constants used in the system
 * @author fengk
 */
public class Constants {
    public static int SYSTEM_RUNNING_TIME   = 0;
    /**
     * Message generator side constants
     */
    public static String GENERATOR_HOST     = "localhost";
    public static String SERVER_HOST        = "localhost";
    public static final String OPERATION_SUCCESS   = "success";
    public static final String OPERATION_FAILED    = "failed";
    
    public static final String HTTP_POST_SEPARATOR      = "&";
    public static final String HTTP_POST_PARAMS         = "=";
    public static final String DOMAIN_PORT_SEPARATOR    = ":";
    public static final String HTTP_STRING_END          = "$";
    
    public static int DEFAULT_SERVER_NUM          = 4;
    public static int DEFAULT_POISSON_LAMDA       = 5;
    
    public static final String[] DEFAULT_MESSAGE_CONTENT = {
        "Hello, World!",
        "You jump, I jump!",
        "Show me the money",
        "You are the CSS to my HTML",
        "I am THE ONE!"
    };
    
    /**
     * Web server side constants
     */
    public static int DEFAULT_LONG_POLLING_HOLD_TIME  = 60;
    public static int DEFAULT_LONG_POLLING_SLEEP_TIME = 1;
    public static int DEFAULT_STREAMING_HOLD_TIME     = 60;
    public static int DEFAULT_STREAMING_SLEEP_TIME    = 1;
    public static int DEFAULT_BUFFER_BYTE_LENGTH      = 1024;
    
    public static int GENERATOR_HTTP_SEND_PORT    = 8000;
    public static int POLLING_SERVER_PORT         = 8001;
    public static int LONG_POLLING_SERVER_PORT    = 8002;
    public static int STREAMING_SERVER_PORT       = 8003;
    public static int WEBSOCKET_SERVER_PORT       = 8004;
    public static int RESOURCE_SERVER_PORT        = 8005;
    
    public static final String GENERATOR_CLENT_NAME         = "MESSAGE_GENERATOR";
    public static final String POLLING_CLIENT_NAME          = "POLLING_CLIENT";
    public static final String LONG_POLLING_CLIENT_NAME     = "LONG_POLLING_CLIENT";
    public static final String STREAMING_CLIENT_NAME        = "STREAMING_CLIENT";
    public static final String WEBSOCKET_CLIENT_NAME        = "WEBSOCKET_CLIENT";
    
    public static final String WEBSOCKET_CLIENT_MESSAGE     = "GET NEW MESSAGE";
    
    public static final String POLLING_INDEX_PAGE           = "/polling/*";
    public static final String POLLING_READ_MESSAGE         = "/message/polling_read";
    public static final String POLLING_WRITE_MESSAGE        = "/message/polling_write";
    
    public static final String LONG_POLLING_INDEX_PAGE      = "/longpolling/*";
    public static final String LONG_POLLING_READ_MESSAGE    = "/message/longpolling_read";
    public static final String LONG_POLLING_WRITE_MESSAGE   = "/message/longpolling_write";
    
    public static final String STREAMING_INDEX_PAGE         = "/streaming/*";
    public static final String STREAMING_READ_MESSAGE       = "/message/streaming_read";
    public static final String STREAMING_WRITE_MESSAGE      = "/message/streaming_write";
    
    public static final String WEBSOCKET_INDEX_PAGE         = "/websocket/*";
    public static final String WEBSOCKET_READ_MESSAGE       = "/message/websocket_read";
    public static final String WEBSOCKET_WRITE_MESSAGE      = "/message/websocket_write";
    
    /**
     * Result writes to CSV files
     */
    public static final String CSV_FILE_SEPARATOR   = ",";
    public static final String CONFIG_FILE_NAME     = "config.xml";
    
    public static String SERVER_ROOT_DIRECTORY       =
            "E:" + File.separator + "server" + File.separator + "server_root";
    public static String SCRIPT_RESOURCE_DIRECTORY    =
            SERVER_ROOT_DIRECTORY + File.separator + "javascripts";
    public static String LOGS_DIRECTORY_PATH      = 
            SERVER_ROOT_DIRECTORY + File.separator + "logs";
    public static String CONFIG_DIRECTORY_PATH    = 
            SERVER_ROOT_DIRECTORY + File.separator + "config";
    public static String CONFIG_FILE_PATH         = 
            CONFIG_DIRECTORY_PATH + File.separator + CONFIG_FILE_NAME;
}
