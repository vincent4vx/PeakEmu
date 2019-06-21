/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.group;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GroupInvitationError {
    final static public char PARTY_ALREADY_IN_GROUP = 'a';
    final static public char PARTY_FULL = 'f';
    final static public char CANT_FIND_ACCOUNT_OR_CHARACTER = 'n';
    
    private char error;
    private String extra;

    public GroupInvitationError(char error, String extra) {
        this.error = error;
        this.extra = extra;
    }

    public GroupInvitationError(char error) {
        this.error = error;
        extra = "";
    }

    @Override
    public String toString() {
        return "PIE" + error + extra;
    }
    
    
}
