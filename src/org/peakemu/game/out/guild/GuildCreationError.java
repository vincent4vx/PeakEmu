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
public class GuildCreationError {
    private String error;
    
    final static public String GUILD_CREATE_ALLREADY_USE_NAME = "an";
    final static public String GUILD_CREATE_ALLREADY_USE_EMBLEM = "ae";
    final static public String GUILD_CREATE_ALLREADY_IN_GUILD = "a";

    public GuildCreationError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "gCE" + error;
    }
    
    
}
