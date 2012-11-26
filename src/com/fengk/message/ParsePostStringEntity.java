package com.fengk.message;

/**
 * Used to parse the HTTP Post request parameters and put them into InstantMessage
 * String is like this: fromId=fengkai&toId=fengkai&sendDate=12345&content=Interesting
 * @author fengk
 */

import com.fengk.constants.Constants;

public class ParsePostStringEntity {
    
    public static InstantMessage parseStringToInstantMessage(String postString) {
        InstantMessage message = new InstantMessage();
        
        String[] params = postString.split(Constants.HTTP_POST_SEPARATOR);
        for (int i = 0; i < params.length; i++) {
            String[] content = params[i].split(Constants.HTTP_POST_PARAMS);
            // System.out.println("Params[0]=" + content[0] + " Params[1]=" + content[1] 
            //        + " Length:" + content[1].length());
            if (content[0].toLowerCase().equals("id")) {
                message.setId(Long.valueOf(content[1]).longValue());
            }
            else if (content[0].toLowerCase().equals("fromid")) {
                message.setFromId(content[1]);
            } 
            else if (content[0].toLowerCase().equals("toid")) {
                message.setToId(content[1]);
            } 
            else if (content[0].toLowerCase().equals("senddate")) {
                message.setSendDate(content[1]);
            } 
            else if (content[0].toLowerCase().equals("content")) {
                int pos = content[1].indexOf(Constants.HTTP_STRING_END);
                if (pos < 0) {
                    message.setContent(content[1]);
                }
                else {
                    message.setContent(content[1].substring(0, pos));
                }
            }
            else {
                System.err.println("Wrong post params: " + content);
            }
        }
        
        return message;
    }
}
