/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.network.out;

import org.peakemu.common.util.StringUtil;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Message {
    private boolean isServer;
    private int messageId;
    private Object[] messageArgs;
    private String name;

    public Message(boolean isServer, int messageId, String[] messageArgs) {
        this.isServer = isServer;
        this.messageId = messageId;
        this.messageArgs = messageArgs;
    }

    public Message(boolean isServer, int messageId, Object... messageArgs) {
        this.isServer = isServer;
        this.messageId = messageId;
        this.messageArgs = messageArgs;
    }

    public boolean isIsServer() {
        return isServer;
    }

    public void setIsServer(boolean isServer) {
        this.isServer = isServer;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(String[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "M" + (isServer ? "1" : "0") + messageId + "|" + StringUtil.join(messageArgs, ";") + (name == null ? "" : "|" + name);
    }
    
    
}
