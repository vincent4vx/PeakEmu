/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.conquest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.fight.ConquestFight;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.PlayerFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PrismDefenders {
    final private int id;
    final private boolean add;
    final private Collection<Player> defenders;

    public PrismDefenders(int id, boolean add, Collection<Player> defenders) {
        this.id = id;
        this.add = add;
        this.defenders = defenders;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(defenders.size() * 64);
        
        sb.append("CP").append(add ? '+' : '-');
        sb.append(Integer.toString(id, 36));
        
        for (Player defender : defenders) {
            sb.append('|');
            
            sb.append(Integer.toString(defender.getSpriteId(), 36)).append(';');
            
            if(!add)
                continue;
            
            sb.append(defender.getName()).append(';')
              .append(defender.getGfxID()).append(';')
              .append(defender.getLevel()).append(';')
              .append(Integer.toString(defender.getColor1(), 36)).append(';')
              .append(Integer.toString(defender.getColor2(), 36)).append(';')
              .append(Integer.toString(defender.getColor3(), 36)).append(';')
              .append("0") //reservist
            ;
        }
        
        return sb.toString();
    }
    
    static private PrismDefenders allDefenders(ConquestFight fight, boolean add){
        int id = fight.getPrism().getPrismUniqueId();
        Collection<Player> defenders = new ArrayList<>();
        
        for(Fighter fighter : fight.getDefendersTeam().getFighters()){
            if(fighter instanceof PlayerFighter)
                defenders.add(fighter.getPlayer());
        }
        
        return new PrismDefenders(id, add, defenders);
    }
    
    static public PrismDefenders addDefender(Prism prism, Player defender){
        return new PrismDefenders(prism.getPrismUniqueId(), true, Collections.singleton(defender));
    }
    
    static public PrismDefenders addAllDefenders(ConquestFight fight){
        return allDefenders(fight, true);
    }
    
    static public PrismDefenders removeAllDefenders(ConquestFight fight){
        return allDefenders(fight, false);
    }
}
