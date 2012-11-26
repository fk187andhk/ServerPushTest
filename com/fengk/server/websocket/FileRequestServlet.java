package com.fengk.server.websocket;

/**
 * Servlet for downloading HTML, CSS or JavaScript to browser 
 * @author fengk
 */

import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fengk.constants.Constants;

public class FileRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /**
         * Return file to browser
         */
        String target = request.getRequestURI();
        
        System.out.println("Get request from " + target);
        
        final File file = new File(
                Constants.SERVER_ROOT_DIRECTORY, 
                URLDecoder.decode(target, "UTF-8"));
        
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            String entity = 
                    "<html><body><h1>File " + file.getPath() + " not found</h1></body></html>";
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            try {
                PrintWriter writer = response.getWriter();
                writer.write(entity);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.err.println("File " + file.getPath() + " not found");
        } else if (!file.canRead() || file.isDirectory()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            String entity = "<html><body><h1>Access denied</h1></body></html>";
            try {
                PrintWriter writer = response.getWriter();
                writer.write(entity);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.err.println("Cannot read file " + file.getPath());
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                PrintWriter writer = response.getWriter();
                Reader reader = new InputStreamReader(new FileInputStream(file));
                char[] buffer = new char[Constants.DEFAULT_BUFFER_BYTE_LENGTH];
                while ((reader.read(buffer)) != -1) {
                    writer.write(String.valueOf(buffer));
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Serving file " + file.getPath());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(Constants.OPERATION_SUCCESS);
        System.err.println("Get wrong HTTP method GET from: " + request.getRemoteHost());
    }
}
