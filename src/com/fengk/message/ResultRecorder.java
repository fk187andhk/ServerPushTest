package com.fengk.message;

/**
 * Used to record the delay time into a csv file
 * @author fengk
 */

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import com.fengk.constants.Constants;

public class ResultRecorder {
    private String fileName;
    private File csvFile;
    private BufferedWriter logger;
    
    public ResultRecorder(String fileName) {
        this.fileName = fileName;
    }
    
    public void initialize() {
        try {
            csvFile = new File(Constants.LOGS_DIRECTORY_PATH + File.separator + this.fileName);
            logger = new BufferedWriter(new FileWriter(csvFile, true));
            StringBuilder title = new StringBuilder();
            title.append("id,delay");
            logger.write(title.toString());
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeLine(long id, long delay) {
        StringBuilder line = new StringBuilder();
        line.append(String.valueOf(id));
        line.append(Constants.CSV_FILE_SEPARATOR);
        line.append(String.valueOf(delay));
        try {
            logger.newLine();
            logger.write(line.toString());
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        if (logger != null) {
            try {
                logger.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.err.println("The logger handler is null");
        }
    }
}
