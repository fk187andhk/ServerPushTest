package com.fengk.message;

/**
 * The messages list which is returned to HTTP client or browser
 * @author fengk
 */

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fengk.message.InstantMessage;

public class ReturnMessageList {
    private int count;
    private boolean last;
    private List<InstantMessage> list;
    
    public ReturnMessageList() {
        count = 0;
        last = false;
        list = null;
    }
    
    public int getCount() {
        return count;
    }
    
    public boolean getLast() {
        return last;
    }
    
    public void setLast(boolean last) {
        this.last = last;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public void clearMessageList() {
        count = 0;
        last = false;
        list = null;
    }
    
    public List<InstantMessage> getMessageList() {
        return list;
    }
    
    public void setMessageList(List<InstantMessage> list) {
        this.list = list;
        setCount(list.size());
    }
    
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
