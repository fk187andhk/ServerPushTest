package com.fengk.message;

/**
 * The basic class for instant messages
 * @author fengk
 */

import com.alibaba.fastjson.JSON;
import com.fengk.constants.Constants;

public class InstantMessage {
    private long id;
    private String fromId;
    private String toId;
    private String sendDate;
    private String clientGetDate;
    private String content;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getFromId() {
        return fromId;
    }
    
    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
    
    public String getToId() {
        return toId;
    }
    
    public void setToId(String toId) {
        this.toId = toId;
    }
    
    public String getSendDate() {
        return this.sendDate;
    }
    
    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
    
    public String getClientGetDate() {
        return clientGetDate;
    }
    
    public void setClientGetDate(String clientGetDate) {
        this.clientGetDate = clientGetDate;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return "Date: " + sendDate + " message from " + fromId + " to " + toId + ": " + content;
    }
    
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
    
    public String toHttpPostString() {
        StringBuilder postString = new StringBuilder();
        postString.append("id=" + String.valueOf(id));
        postString.append(Constants.HTTP_POST_SEPARATOR);
        postString.append("fromId=" + fromId);
        postString.append(Constants.HTTP_POST_SEPARATOR);
        postString.append("toId=" + toId);
        postString.append(Constants.HTTP_POST_SEPARATOR);
        postString.append("content=" + content);
        postString.append(Constants.HTTP_STRING_END);
        return postString.toString();
    }
}
