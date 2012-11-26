package com.fengk.server.polling;

import com.fengk.message.InstantMessage;
import com.fengk.message.MessageQueue;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The message queue for polling server
 * @author fengk
 */
public class PollingServerQueue implements MessageQueue {
    
    private List<InstantMessage> queue;
    
    public PollingServerQueue() {
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
