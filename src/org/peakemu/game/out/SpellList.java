/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out;

import org.peakemu.objects.player.Player;
import org.peakemu.world.SpellLevel;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpellList {
    private Player player;

    public SpellList(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        
        sb.append("SL");
        
        for(SpellLevel ss : player.getSpells().values()){
            sb.append(ss.getSpellID()).append('~').append(ss.getLevel()).append('~').append(player.getSpellPosition(ss.getSpellID())).append(';');
        }
        
        return sb.toString();
    }
    
    
}
