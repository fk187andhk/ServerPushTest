package com.fengk.message;

import java.util.List;

/**
 * Interface for the message queue used in different applications
 * @author fengk
 */
public interface MessageQueue {
    
    /**
     * Push one message into the queue
     * @param mesage
     */
    public void enterQueue(InstantMessage mesage);
    
    /**
     * Get all the unread messages from the queue
     * @return message
     */
    public List<InstantMessage> deleteQueue();
    
    /**
     * Clear all the messages in the queue
     */
    public void clearQueue();
    
    /**
     * Judge whether there are new messages coming
     * @return true or false
     */
    public boolean hasNewMessage();
}
