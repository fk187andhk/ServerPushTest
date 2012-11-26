package com.fengk.server.streaming;

/**
 * The instant message queue for HTTP streaming server
 * @author fengk
 */

import com.fengk.message.MessageQueue;
import com.fengk.message.InstantMessage;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class StreamingServerQueue implements MessageQueue {

    private List<InstantMessage> queue;
    
    public StreamingServerQueue() {
        queue = new ArrayList<InstantMessage>();
    }
    
    @Override
    public void enterQueue(InstantMessage message) {
        synchronized (queue) {
            queue.add(message);
        }
    }
    
    @Override
    public List<InstantMessage> deleteQueue() {
        synchronized (queue) {
            List<InstantMessage> unreadMessages = new ArrayList<InstantMessage>();
            Iterator<InstantMessage> iterator = queue.iterator();
            while (iterator.hasNext()) {
                unreadMessages.add(iterator.next());
            }
            clearQueue();
            return unreadMessages;
        }
    }
    
    @Override
    public void clearQueue() {
        queue.clear();
    }
    
    @Override
    public boolean hasNewMessage() {
        return !queue.isEmpty();
    }
}
