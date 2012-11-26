package com.fengk.constants;

/**
 * Read the configuration from XML file which is under server root directory
 * @author fengk
 */

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.fengk.constants.Constants;

public class ConstantsReader {
    
    public final static String SYSTEM_NAME      = "system";
    public final static String STSTEM_TIME      = "time";
    
    public final static String GENERATOR_NAME   = "generator";
    public final static String GENERATOR_HOST   = "host";
    public final static String GENERATOR_PORT   = "port";
    public final static String GENERATOR_LAMDA  = "poisson-lamda";
    
    public final static String SERVERS_NAME     = "servers";
    public final static String SERVERS_HOST     = "host";
    public final static String SERVERS_ROOT     = "root";
    public final static String SERVERS_NUMBER   = "number";
    
    public final static String SERVERS_POLLING_PORT             = "polling-port";
    public final static String SERVERS_LONGPOLLING_PORT         = "longpolling-port";
    public final static String SERVERS_STREAMING_PORT           = "streaming-port";
    public final static String SERVERS_WEBSOCKET_PORT           = "websocket-port";
    public final static String SERVERS_LONGPOLLING_HOLD_TIME    = "longpolling-hold-time";
    public final static String SERVERS_LONGPOLLING_SLEEP_TIME   = "longpolling-sleep-time";
    public final static String SERVERS_STREAMING_HOLD_TIME      = "streaming-hold-time";
    public final static String SERVERS_STREAMING_SLEEP_TIME     = "streaming-sleep-time";
    
    private Document xmlDoc = null;
    private String filePath = null;
    
    public ConstantsReader(String filePath) {
        this.filePath = filePath;
    }
    
    public void init() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            try {
                xmlDoc = builder.parse(filePath);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (SAXException ex) {
                ex.printStackTrace();
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void readConfiguration() throws Exception {
        System.out.println("--------------------------------------");
        System.out.println("Configuration file: " + filePath);
        
        NodeList systemNodeList     = xmlDoc.getElementsByTagName(SYSTEM_NAME);
        NodeList generatorNodeList  = xmlDoc.getElementsByTagName(GENERATOR_NAME);
        NodeList serversNodeList    = xmlDoc.getElementsByTagName(SERVERS_NAME);
        
        Node systemFatherNode       = systemNodeList.item(0);
        Node generatorFatherNode    = generatorNodeList.item(0);
        Node serversFatherNode      = serversNodeList.item(0);
        
        System.out.println("Read configs from father node " + systemFatherNode.getNodeName());
        NodeList systemChildrenList = systemFatherNode.getChildNodes();
        for (int i = 0; i < systemChildrenList.getLength(); i++) {
            Node child = systemChildrenList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String name = child.getNodeName();
                if (name.equals(STSTEM_TIME)) {
                    Constants.SYSTEM_RUNNING_TIME = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else {
                    System.err.println("Error: unknown child: " + name);
                }
                System.out.println(child.getNodeName() + "=" + child.getFirstChild().getNodeValue());
            }
        }
        
        System.out.println("Read configs from father node " + generatorFatherNode.getNodeName());
        NodeList generatorChildrenList = generatorFatherNode.getChildNodes();
        for (int i = 0; i < generatorChildrenList.getLength(); i++) {
            Node child = generatorChildrenList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String name = child.getNodeName();
                if (name.equals(GENERATOR_HOST)) {
                    Constants.GENERATOR_HOST = child.getFirstChild().getNodeValue();
                }
                else if (name.equals(GENERATOR_PORT)) {
                    Constants.GENERATOR_HTTP_SEND_PORT = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(GENERATOR_LAMDA)) {
                    Constants.DEFAULT_POISSON_LAMDA = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else {
                    System.err.println("Error: unknown child: " + name);
                }
                System.out.println(child.getNodeName() + "=" + child.getFirstChild().getNodeValue());
            }
        }
        
        System.out.println("Read configs from father node " + serversFatherNode.getNodeName());
        NodeList serversChildrenList = serversFatherNode.getChildNodes();
        for (int i = 0; i < serversChildrenList.getLength(); i++) {
            Node child = serversChildrenList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String name = child.getNodeName();
                if (name.equals(SERVERS_HOST)) {
                    Constants.SERVER_HOST = child.getFirstChild().getNodeValue();
                }
                else if (name.equals(SERVERS_ROOT)) {
                    Constants.SERVER_ROOT_DIRECTORY = child.getFirstChild().getNodeValue();
                    Constants.SCRIPT_RESOURCE_DIRECTORY = Constants.SERVER_ROOT_DIRECTORY + File.separator + "javascripts";
                    Constants.LOGS_DIRECTORY_PATH       = Constants.SERVER_ROOT_DIRECTORY + File.separator + "logs";
                    Constants.CONFIG_DIRECTORY_PATH     = Constants.SERVER_ROOT_DIRECTORY + File.separator + "config";
                    Constants.CONFIG_FILE_PATH          = Constants.CONFIG_DIRECTORY_PATH + File.separator + Constants.CONFIG_FILE_NAME;
                }
                else if (name.equals(SERVERS_NUMBER)) {
                    Constants.DEFAULT_SERVER_NUM = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(SERVERS_LONGPOLLING_HOLD_TIME)) {
                    Constants.DEFAULT_LONG_POLLING_HOLD_TIME = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(SERVERS_LONGPOLLING_SLEEP_TIME)) {
                    Constants.DEFAULT_LONG_POLLING_SLEEP_TIME = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(SERVERS_STREAMING_HOLD_TIME)) {
                    Constants.DEFAULT_STREAMING_HOLD_TIME = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(SERVERS_STREAMING_SLEEP_TIME)) {
                    Constants.DEFAULT_STREAMING_SLEEP_TIME = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(SERVERS_POLLING_PORT)) {
                    Constants.POLLING_SERVER_PORT = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(SERVERS_LONGPOLLING_PORT)) {
                    Constants.LONG_POLLING_SERVER_PORT = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(SERVERS_STREAMING_PORT)) {
                    Constants.STREAMING_SERVER_PORT = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else if (name.equals(SERVERS_WEBSOCKET_PORT)) {
                    Constants.WEBSOCKET_SERVER_PORT = Integer.valueOf(child.getFirstChild().getNodeValue());
                }
                else {
                    System.err.println("Error: unknown child: " + name);
                }
                System.out.println(child.getNodeName() + "=" + child.getFirstChild().getNodeValue());
            }
        }
        
        System.out.println("--------------------------------------");
    }
}
