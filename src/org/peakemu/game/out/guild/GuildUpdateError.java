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
public class GuildUpdateError {
    final static public char NOT_ENOUGHT_RIGHTS_FROM_GUILD = 'd';
    final static public char CANT_BANN_FROM_GUILD_NOT_MEMBER = 'a';
    
    private char error;

    public GuildUpdateError(char error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "gKE" + error;
    }
    
    
}
