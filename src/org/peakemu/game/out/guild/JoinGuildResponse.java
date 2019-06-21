/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JoinGuildResponse {
    final static public String GUILD_JOIN_ALREADY_IN_GUILD = "Ea";
    final static public String GUILD_JOIN_NO_RIGHTS = "Ed";
    final static public String GUILD_JOIN_UNKNOW = "Eu";
    final static public String GUILD_JOIN_OCCUPED = "Eo";
    final static public String GUILD_JOIN_REFUSED = "Er";
    final static public String GUILD_JOIN_CANCEL = "Ec";
    final static public String A_JOIN_YOUR_GUILD = "Ka";
    final static public String YOUR_R_NEW_IN_GUILD = "Kj";
    
    private String msg;
    private String extra = "";

    public JoinGuildResponse(String msg) {
        this.msg = msg;
    }

    public JoinGuildResponse(String msg, String extra) {
        this.msg = msg;
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "gJ" + msg + extra;
    }
    
    
}
