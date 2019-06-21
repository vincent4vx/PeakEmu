/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.account;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CharacterList {
    final private Collection<Player> chars = new ArrayList<>(5);
    private long aboTime = 31536000000l;

    public long getAboTime() {
        return aboTime;
    }

    public void setAboTime(long aboTime) {
        this.aboTime = aboTime;
    }

    public void addCharacter(Player character){
        chars.add(character);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(256);
        
        sb.append("ALK").append(aboTime).append('|').append(chars.size());
        
        for (Player aChar : chars) {
            sb.append(aChar.parseALK());
        }
        
        return sb.toString();
    }
}
