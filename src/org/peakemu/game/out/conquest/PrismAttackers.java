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
public class PrismAttackers {

    final private int id;
    final private boolean add;
    final private Collection<Player> attackers;

    public PrismAttackers(int id, boolean add, Collection<Player> attackers) {
        this.id = id;
        this.add = add;
        this.attackers = attackers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(attackers.size() * 32);

        sb.append("Cp").append(add ? '+' : '-');
        sb.append(Integer.toString(id, 36));

        for (Player defender : attackers) {
            sb.append('|');

            sb.append(Integer.toString(defender.getSpriteId(), 36)).append(';');

            if (!add) {
                continue;
            }

            sb.append(defender.getName()).append(';')
              .append(defender.getLevel()).append(';')
                ;
        }

        return sb.toString();
    }

    static private PrismAttackers allAttackers(ConquestFight fight, boolean add) {
        int id = fight.getPrism().getPrismUniqueId();
        Collection<Player> attackers = new ArrayList<>();

        for (Fighter fighter : fight.getAttackersTeam().getFighters()) {
            if (fighter instanceof PlayerFighter) {
                attackers.add(fighter.getPlayer());
            }
        }

        return new PrismAttackers(id, add, attackers);
    }
    
    static public PrismAttackers addAttacker(Prism prism, Player attacker){
        return new PrismAttackers(prism.getPrismUniqueId(), true, Collections.singleton(attacker));
    }
    
    static public PrismAttackers removeAllAttackers(ConquestFight fight){
        return allAttackers(fight, false);
    }
    
    static public PrismAttackers addAllAttackers(ConquestFight fight){
        return allAttackers(fight, true);
    }
}
