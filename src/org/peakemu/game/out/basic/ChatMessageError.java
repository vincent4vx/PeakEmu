/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.basic;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChatMessageError {
    final static public char SYNTAX_ERROR = 'S';
    final static public char USER_NOT_CONNECTED = 'f';
    final static public char USER_NOT_CONNECTED_BUT_TRY_SEND_EXTERNAL = 'e';
    final static public char USER_NOT_CONNECTED_EXTERNAL_NACK = 'n';
        
    private char error;
    private String extra;

    public ChatMessageError(char error, String extra) {
        this.error = error;
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "cME" + error + extra;
    }
    
    
}
