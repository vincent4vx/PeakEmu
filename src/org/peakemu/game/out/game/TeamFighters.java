/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import java.util.Collection;
import java.util.Collections;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.team.FightTeam;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class TeamFighters {
    final private String id;
    final private boolean add;
    final private Collection<Fighter> fighters;

    public TeamFighters(String id, boolean add, Collection<Fighter> fighters) {
        this.id = id;
        this.add = add;
        this.fighters = fighters;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(24 * fighters.size());
        
        sb.append("Gt").append(id);
        
        for (Fighter fighter : fighters) {
            sb.append('|').append(add ? '+' : '-');
            
            sb.append(fighter.getSpriteId()).append(';')
              .append(fighter.getPacketsName()).append(';')
              .append(fighter.get_lvl())
            ;
        }
        
        return sb.toString();
    }

    static public TeamFighters allTeam(FightTeam team){
        return new TeamFighters(team.getId(), true, team.getFighters());
    }
    
    static public TeamFighters addFighter(FightTeam team, Fighter fighter){
        return new TeamFighters(team.getId(), true, Collections.singleton(fighter));
    }
    
    static public TeamFighters removeFighter(FightTeam team, Fighter fighter){
        return new TeamFighters(team.getId(), false, Collections.singleton(fighter));
    }
}
