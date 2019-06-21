/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.team.FightTeam;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightCreated {
    private Fight fight;

    public FightCreated(Fight fight) {
        this.fight = fight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        
        sb.append("Gc+").append(fight.get_id()).append(';').append(fight.get_type().ordinal());
        
        for(FightTeam team : fight.getTeams()){
            sb.append('|').append(team.getId()).append(';').append(team.getCell()).append(';').append(team.getType()).append(';').append(team.getAlignement());
        }
        
        return sb.toString();
    }
}
